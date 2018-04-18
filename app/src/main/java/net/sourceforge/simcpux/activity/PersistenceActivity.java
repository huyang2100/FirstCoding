package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.constant.ConstantFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class PersistenceActivity extends AppCompatActivity {

    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persistence);

        et = findViewById(R.id.et);

        String etContent = load();
        if (!TextUtils.isEmpty(etContent)) {
            et.setText(etContent);
            et.setSelection(etContent.length());
        }
    }

    private String load() {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = openFileInput(ConstantFile.EDITTEXT);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onStop() {
        super.onStop();
        save(et.getText().toString());
    }

    private void save(String str) {
        try {
            FileOutputStream fos = openFileOutput(ConstantFile.EDITTEXT, Context.MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(str);

            bw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PersistenceActivity.class);
        context.startActivity(intent);
    }
}
