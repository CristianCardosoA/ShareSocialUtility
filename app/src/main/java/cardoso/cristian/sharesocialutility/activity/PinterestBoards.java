package cardoso.cristian.sharesocialutility.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import java.util.List;

import cardoso.cristian.sharesocialutility.R;

/**
 * Created by macbook on 22/10/16.
 */

public class PinterestBoards extends AppCompatActivity {

    private final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
    private String imageUriToShare;
    private String textToShare;
    private String link;
    final Context context = this;
    Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinterest_boards);
        returnIntent = new Intent(this,PinterestShare.class);
        //Pinterest
        imageUriToShare = getIntent().getExtras().getString("imageUriToShare");
        textToShare = getIntent().getExtras().getString("textToShare");
        link = getIntent().getExtras().getString("link");
        getMyBorads();
    }

    public void getMyBorads(){

        PDKClient.getInstance().getMyBoards(BOARD_FIELDS,new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.getData().toString());
                final List<PDKBoard> boards = response.getBoardList();
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setTitle(R.string.select_board_pinterest);
                String[] myBoardsTitle = new String[boards.size()];
                for (int ePos = 0; ePos < boards.size(); ePos++) {
                    myBoardsTitle[ePos] = boards.get(ePos).getName();
                }
                b.setItems(myBoardsTitle, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setupPinterestShare(boards.get(which).getUid());
                    }
                });
                b.setPositiveButton(getString(R.string.cancel), new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(Activity.RESULT_FIRST_USER,returnIntent);
                                finish();
                            }
                        });
                b.setCancelable(false);
                b.show();
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }

    public void setupPinterestShare(String boardId) {
        PDKClient.getInstance().createPin(textToShare, boardId, imageUriToShare, (link == null) ? imageUriToShare : link, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.getData().toString());
                Toast.makeText(getApplicationContext(),getString(R.string.pin_success), Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
                Toast.makeText(getApplicationContext(),getString(R.string.error_message_invalid_operation), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}