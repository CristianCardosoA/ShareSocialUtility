package cardoso.cristian.sharesocialutility;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cardoso.cristian.sharesocialutility.object.Share;
import cardoso.cristian.sharesocialutility.utils.ShareUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Uri localImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "logo.png"));
        final ShareUtils shareUtils = new ShareUtils(this,this);
        final String contentUri = "https://github.com/CristianCardosoA";
        final String imageUri = "https://avatars2.githubusercontent.com/u/18292956?v=3&s=466";

        final List<Share> apps = new ArrayList<>();
        apps.add(new Share(R.mipmap.settings_facebook_icon,"Facebook",ShareUtils.FACEBOOK));
        apps.add(new Share(R.mipmap.settings_googleplus_icon,"Google Plus",ShareUtils.GOOGLE_PLUS));
        apps.add(new Share(R.mipmap.settings_instagram_icon,"Instagram",ShareUtils.INSTAGRAM));
        apps.add(new Share(R.mipmap.settings_pinterest_icon,"Pinterest",ShareUtils.PINTEREST));
        apps.add(new Share(R.mipmap.settings_twitter_icon,"Twitter",ShareUtils.TWITTER));

        findViewById(R.id.showDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.showShareDialog(Uri.parse(imageUri),"Visit my github personal profile",false,"Visit my github personal profile",Uri.parse(contentUri),localImageUri,apps,null);
            }
        });
    }
}
