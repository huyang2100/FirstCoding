package net.sourceforge.simcpux.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import net.sourceforge.simcpux.dbhelper.BookStoreDBHelper;

public class BookStoreContentProvider extends ContentProvider {
    private static UriMatcher uriMatcher;
    private static final int BOOK_DIR = 1;
    private static final int BOOK_ITEM = 2;
    private static final int CATEGORY_DIR = 3;
    private static final int CATEGORY_ITEM = 4;
    private static final String authority = "net.sourceforge.simcpux.provider";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(authority, BookStoreDBHelper.TABLE_BOOK, BOOK_DIR);
        uriMatcher.addURI(authority, BookStoreDBHelper.TABLE_BOOK + "/#", BOOK_ITEM);
        uriMatcher.addURI(authority, BookStoreDBHelper.TABLE_CATEGORY, CATEGORY_DIR);
        uriMatcher.addURI(authority, BookStoreDBHelper.TABLE_CATEGORY + "/#", CATEGORY_ITEM);
    }

    private BookStoreDBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = new BookStoreDBHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delete = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                db = dbHelper.getWritableDatabase();
                delete = db.delete(BookStoreDBHelper.TABLE_BOOK, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                db = dbHelper.getWritableDatabase();
                delete = db.delete(BookStoreDBHelper.TABLE_BOOK, "_id = ?", new String[]{uri.getPathSegments().get(1)});
                break;
            case CATEGORY_DIR:
                db = dbHelper.getWritableDatabase();
                delete = db.delete(BookStoreDBHelper.TABLE_CATEGORY, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                db = dbHelper.getWritableDatabase();
                delete = db.delete(BookStoreDBHelper.TABLE_CATEGORY, "_id = ?", new String[]{uri.getPathSegments().get(1)});
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return delete;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri = null;
        long id = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                db = dbHelper.getWritableDatabase();
                id = db.insert(BookStoreDBHelper.TABLE_BOOK, null, values);
                returnUri = Uri.parse("content://" + authority + "/" + BookStoreDBHelper.TABLE_BOOK + "/" + id);
                break;

            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                db = dbHelper.getWritableDatabase();
                id = db.insert(BookStoreDBHelper.TABLE_BOOK, null, values);
                returnUri = Uri.parse("content://" + authority + "/" + BookStoreDBHelper.TABLE_BOOK + "/" + id);
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                db = dbHelper.getWritableDatabase();
                cursor = db.query(BookStoreDBHelper.TABLE_BOOK, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                db = dbHelper.getWritableDatabase();
                cursor = db.query(BookStoreDBHelper.TABLE_BOOK, projection, "_id=?", new String[]{uri.getPathSegments().get(1)}, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                db = dbHelper.getWritableDatabase();
                cursor = db.query(BookStoreDBHelper.TABLE_CATEGORY, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                db = dbHelper.getWritableDatabase();
                cursor = db.query(BookStoreDBHelper.TABLE_CATEGORY, projection, "_id=?", new String[]{uri.getPathSegments().get(1)}, null, null, sortOrder);
                break;
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int update = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                db = dbHelper.getWritableDatabase();
                update = db.update(BookStoreDBHelper.TABLE_BOOK, values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                db = dbHelper.getWritableDatabase();
                update = db.update(BookStoreDBHelper.TABLE_BOOK, values, "_id = ?", new String[]{uri.getPathSegments().get(1)});
                break;
            case CATEGORY_DIR:
                db = dbHelper.getWritableDatabase();
                update = db.update(BookStoreDBHelper.TABLE_CATEGORY, values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                db = dbHelper.getWritableDatabase();
                update = db.update(BookStoreDBHelper.TABLE_CATEGORY, values, "_id = ?", new String[]{uri.getPathSegments().get(1)});
                break;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return update;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd." + authority + "." + BookStoreDBHelper.TABLE_BOOK;
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd." + authority + "." + BookStoreDBHelper.TABLE_BOOK;
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd." + authority + "." + BookStoreDBHelper.TABLE_CATEGORY;
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd." + authority + "." + BookStoreDBHelper.TABLE_CATEGORY;
        }
        return "";
    }
}
