package cardoso.cristian.sharesocialutility.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnItemClickListener;
import java.util.List;
import cardoso.cristian.sharesocialutility.activity.PinterestShare;
import cardoso.cristian.sharesocialutility.adapters.ShareAdapter;
import cardoso.cristian.sharesocialutility.listeners.ShareClickListener;
import cardoso.cristian.sharesocialutility.object.Share;

public class ShareUtils {

    private Context context;
    private Activity activity;
    public static String FACEBOOK = "com.facebook.katana";
    public static String INSTAGRAM = "com.instagram.android";
    public static String TWITTER = "com.twitter";
    public static String PINTEREST = "com.pinterest";
    public static String WEIBO = "com.sina.weibo";
    public static String GOOGLE_PLUS = "com.google.android.apps.plus";
    public static String WECHAT = "com.tencent.mm";
    private ShareClickListener listener;
    private CallbackManager callbackManager;

    public ShareUtils(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public ShareClickListener getListener() {
        return listener;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void setCallbackManager(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
    }

    public void setOnShareClickListener(ShareClickListener listener) {
        this.listener = listener;
    }

    public void showShareDialog(@Nullable final Uri imageUriToShare, @Nullable final String longTextShare, final boolean hasLongText, @Nullable final String textToShare,
                                @Nullable final Uri contentToShare, @Nullable final Uri localImageUri,
                                final List<Share> apps, @Nullable final Bitmap imageBitmap){


        Holder holder = new GridHolder(3);
        ShareAdapter adapter = new ShareAdapter(context,apps);
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setAdapter(adapter)
                .setOnClickListener(null)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        if(listener != null)listener.onClickShareListener(dialog,item,view,position);
                        Share share = apps.get(position);
                        switch (share.getAplicacionName()) {
                            case "com.facebook.katana"://Facebook
                                if(isInstalledApp(ShareUtils.FACEBOOK)){
                                    if(contentToShare == null){
                                        setupFacebookShareIntent(imageBitmap,imageUriToShare, null);
                                    }else{
                                        setupFacebookShareIntent(imageBitmap,imageUriToShare,contentToShare);
                                    }
                                }else{
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + ShareUtils.FACEBOOK));
                                    context.startActivity(intent);
                                }
                                break;
                            case "com.pinterest"://Pinterest;
                                //if(isInstalledApp(ShareUtils.PINTEREST)){
                                    Intent intent = new Intent(context,PinterestShare.class);
                                    intent.putExtra("imageUriToShare", String.valueOf(imageUriToShare));
                                    intent.putExtra("link", (contentToShare == null) ? String.valueOf(imageUriToShare) : String.valueOf(contentToShare));
                                    if(hasLongText){
                                        intent.putExtra("textToShare", longTextShare);
                                    }else{
                                        intent.putExtra("textToShare", textToShare);
                                    }
                                    context.startActivity(intent);

                                //}else{
                                //    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + ShareUtils.PINTEREST));
                                //    context.startActivity(intent);
                                //}
                                break;
                            default:
                                intent = getShareIntent(share.getAplicacionName(),localImageUri,contentToShare,textToShare);
                                try{
                                    context.startActivity(intent);

                                }catch (ActivityNotFoundException e){

                                    e.printStackTrace();
                                }
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setExpanded(false)
                .create();
        dialog.show();
    }

    public void setupFacebookShareIntent(@Nullable Bitmap imageRecipe, @Nullable Uri imageUrlToShare, @Nullable Uri uriContentToShare) {
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(context);
        shareDialog = new ShareDialog(activity);
        if (ShareDialog.canShow(SharePhotoContent.class)) {

            if(imageRecipe != null){
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(imageRecipe)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);

            }else{
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(uriContentToShare)
                        .setImageUrl(imageUrlToShare)
                        .build();
                shareDialog.show(content);
            }
        }
    }

    public boolean isInstalledApp(String applicationName){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setType("image/*");
        boolean haveTheApp = false;
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().contains(applicationName)) {
                haveTheApp = true;
                break;
            }
        }
        return haveTheApp;
    }

    public Intent getShareIntent(String applicationName, @Nullable Uri localImageUri, @Nullable Uri contentToShare, String textToShare) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        boolean haveTheApp = false;
        if(localImageUri != null){
            intent.setType("image/*");
        }else{
            intent.setType("text/plain");
        }
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().contains(applicationName)) {
                intent.setPackage(info.activityInfo.packageName);
                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                if(contentToShare != null){
                    intent.putExtra(Intent.EXTRA_TEXT, textToShare + ": " + contentToShare.toString());
                }else{
                    intent.putExtra(Intent.EXTRA_TEXT, textToShare + " #MoninApp");
                }
                if(localImageUri != null){
                   intent.putExtra(Intent.EXTRA_STREAM, localImageUri);
               }
                haveTheApp = true;
                break;
            }
        }
        if(!haveTheApp){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + applicationName));
        }
        return intent;
    }

}
