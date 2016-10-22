package cardoso.cristian.sharesocialutility.activity;

/**
 * Created by macbook on 22/10/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import java.util.ArrayList;
import java.util.List;
import cardoso.cristian.sharesocialutility.R;

public class PinterestShare extends AppCompatActivity {
    private PDKClient pdkClient;
    private String imageUriToShare;
    private String textToShare;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinterest_share);
        imageUriToShare = getIntent().getExtras().getString("imageUriToShare");
        textToShare = getIntent().getExtras().getString("textToShare");
        link = getIntent().getExtras().getString("link");
        //Pinterest

        pdkClient = PDKClient.configureInstance(this, getString(R.string.app_id_pinterest));
        pdkClient.onConnect(this);

        if(pdkClient != null){
            onLoginSuccess();
        }else{
            onLogin();
        }
    }

    public void onLogin() {
        List scopes = new ArrayList<>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);

        pdkClient.login(this, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.getData().toString());
                onLoginSuccess();
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }

    private void onLoginSuccess() {
        Intent i = new Intent(this, PinterestBoards.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("imageUriToShare",imageUriToShare);
        i.putExtra("textToShare",textToShare);
        i.putExtra("link",link);
        startActivity(i);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pdkClient.onOauthResponse(requestCode, resultCode, data);
        finish();
        super.onActivityResult(requestCode, resultCode, data);
    }
}