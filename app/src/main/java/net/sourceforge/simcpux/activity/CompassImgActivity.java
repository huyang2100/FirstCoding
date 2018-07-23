package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.constant.ConstantFile;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.util.Formatter;

public class CompassImgActivity extends BaseActivity {

    private TextView tv_before;
    private TextView tv_after;
    private static final String TAG = "CompassImgActivity";


    @Override
    protected int getResId() {
        return R.layout.activity_compass_img;
    }

    @NonNull
    @Override
    protected String[] requestPermissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void initData() {

        new AsyncTask<Void, Void, File>() {

            @Override
            protected File doInBackground(Void... voids) {

                try {
                    return saveBigImg2SD();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                if (file == null) {
                    Toast.makeText(getApplicationContext(), "文件打开出错！", Toast.LENGTH_SHORT).show();
                } else {
                    String sizeBefore = android.text.format.Formatter.formatFileSize(getApplicationContext(), file.length());
                    tv_before.setText(sizeBefore);

                    try {
                        File fileAfter = FileUtil.compress(file,1000,1000);
                        String sizeAfter = android.text.format.Formatter.formatFileSize(getApplicationContext(), fileAfter.length());
                        tv_after.setText(sizeAfter);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "文件没找到！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.execute();
    }



    private File saveBigImg2SD() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(), ConstantFile.DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, ConstantFile.BIG_IMG);
        FileOutputStream fos = new FileOutputStream(file);

        InputStream is = getAssets().open("pp.jpeg");
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) > 0) {
            fos.write(buff, 0, len);
        }

        fos.close();
        is.close();
        return file;
    }

    @Override
    protected void initView() {
        tv_before = findViewById(R.id.tv_before);
        tv_after = findViewById(R.id.tv_after);
        findViewById(R.id.btn_compass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CompassImgActivity.class);
    }
}
