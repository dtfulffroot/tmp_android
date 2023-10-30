package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] PERMISSIONS_STORAGE = new String[]{
            //Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };
    private  static final int REQUEST_CODE_STORAGE=1;
    ImageView iv_cal;
    TextView tv_cal;
    ActivityResultLauncher<Intent> mResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_select).setOnClickListener(this);
        iv_cal=findViewById(R.id.iv_cal);
        tv_cal=findViewById(R.id.tv_cal);
        mResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){
                    Intent intent=result.getData();
                    Uri picuri=intent.getData();
                    if(picuri!=null){
                        iv_cal.setImageURI(picuri);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picuri));
                            String tmp=Double.toString(GrayScaleConverter.getLightness(bitmap));
                            tv_cal.setText(tmp);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_select) {
            jumpToSettings();
            PermissonUtil.checkPermission(this,PERMISSIONS_STORAGE,REQUEST_CODE_STORAGE);
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            mResultLauncher.launch(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_STORAGE:
                if(PermissonUtil.checkGrant(grantResults)){
                    Log.d("111","读写权限获取成功");
                }else{
                    String txt="读写权限失败";
                    int duration=Toast.LENGTH_SHORT;
                    Toast toast= Toast.makeText(MainActivity.this,txt,duration);
                    toast.show();
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        //if (!Environment.isExternalStorageManager()){
                            //jumpToSettings();
                        //}
                    //}
                }
                break;
        }
    }

    private void jumpToSettings(){
//        Intent intent=new Intent();
//          Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.fromParts("package",getPackageName(),null));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
    }
}