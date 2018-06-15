package integration.google.sunder.downloadfilefromserverusingretrofitlibraryexample;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;

    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.progress_text)
    TextView mProgressText;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        registerReceiver();

    }
    @OnClick(R.id.btn_download)
    public void downloadFile(){
        if(checkPermission()){
            startDownload();
            mProgressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
        } else {
           requestPermission();
            Toast.makeText(this,"Hello1",Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.i("Result",""+result);
        //PackageManager.PERMISSION_GRANTED==0
        //result==0
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }


    private void startDownload(){

        Intent intent = new Intent(this,DownloadService.class);
        startService(intent);

    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }
    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.i("requestCode",""+requestCode);
        //requestCode==1
        //PERMISSION_REQUEST_CODE==1
        Log.i("requestCode",""+permissions);
        Log.i("requestCode",""+grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //grantResults.length==1
                Log.i("requestCode",""+grantResults.length);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload();
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Intent.get()",""+intent.getAction());
            if(intent.getAction().equals(MESSAGE_PROGRESS)){
                Download download = intent.getParcelableExtra("download");
                Log.i("Intent.get()",""+download.getProgress());
                Log.i("Intent.get()",""+download.getTotalFileSize());
                mProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){
                    mProgressText.setText("File Download Complete");
                    mProgressBar.setVisibility(View.GONE);

                } else {
                    mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                }
            }
        }
    };

}
