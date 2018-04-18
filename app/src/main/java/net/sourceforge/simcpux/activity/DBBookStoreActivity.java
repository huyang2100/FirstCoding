package net.sourceforge.simcpux.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.dbhelper.BookStoreDBHelper;

public class DBBookStoreActivity extends AppCompatActivity {

    private SQLiteDatabase storeDB;
    private ContentValues values;
    private BookStoreDBHelper storeDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbbook_store);
        storeDBHelper = new BookStoreDBHelper(DBBookStoreActivity.this);
        initLisenter();
    }

    private void initLisenter() {
        findViewById(R.id.btn_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB = storeDBHelper.getWritableDatabase();
            }
        });

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB = storeDBHelper.getWritableDatabase();
                values = new ContentValues();
                values.put(BookStoreDBHelper.KEY_AUTHOR, "易中天");
                values.put(BookStoreDBHelper.KEY_NAME, "品三国");
                values.put(BookStoreDBHelper.KEY_PAGENUMBER, 432);
                values.put(BookStoreDBHelper.KEY_PRICE, 45.34);
                storeDB.insert(BookStoreDBHelper.TABLE_BOOK, null, values);

                values.put(BookStoreDBHelper.KEY_AUTHOR, "陈秋实");
                values.put(BookStoreDBHelper.KEY_NAME, "白鹿原");
                values.put(BookStoreDBHelper.KEY_PAGENUMBER, 798);
                values.put(BookStoreDBHelper.KEY_PRICE, 65.88);
                storeDB.insert(BookStoreDBHelper.TABLE_BOOK, null, values);

                Toast.makeText(DBBookStoreActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB = storeDBHelper.getWritableDatabase();
                values = new ContentValues();
                values.put(BookStoreDBHelper.KEY_NAME, "品三国(2018全新改版)");
                values.put(BookStoreDBHelper.KEY_PRICE, 19.99);
                storeDB.update(BookStoreDBHelper.TABLE_BOOK, values, BookStoreDBHelper.KEY_NAME + " = ?", new String[]{"品三国"});

                Toast.makeText(DBBookStoreActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB = storeDBHelper.getWritableDatabase();
                storeDB.delete(BookStoreDBHelper.TABLE_BOOK, BookStoreDBHelper.KEY_PRICE + " > ?", new String[]{"30"});

                Toast.makeText(DBBookStoreActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB = storeDBHelper.getWritableDatabase();
                Cursor cursor = storeDB.query(BookStoreDBHelper.TABLE_BOOK, null, null, null, null, null, null);
                StringBuilder sb = new StringBuilder();
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex(BookStoreDBHelper.KEY_NAME));
                    String author = cursor.getString(cursor.getColumnIndex(BookStoreDBHelper.KEY_AUTHOR));
                    String price = cursor.getString(cursor.getColumnIndex(BookStoreDBHelper.KEY_PRICE));
                    sb.append(name).append("-").append(author).append("-").append(price).append("\r\n");
                }
                cursor.close();

                Toast.makeText(DBBookStoreActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DBBookStoreActivity.class);
        context.startActivity(intent);
    }
}
