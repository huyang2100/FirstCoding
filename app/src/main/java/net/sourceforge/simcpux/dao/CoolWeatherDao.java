package net.sourceforge.simcpux.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import net.sourceforge.simcpux.bean.AreaItem;
import net.sourceforge.simcpux.dbhelper.CoolWeatherDBHelper;

import java.util.ArrayList;

/**
 * Created by yanghu on 2018/4/12.
 */

public class CoolWeatherDao {
    private CoolWeatherDBHelper dbHelper;
    private SQLiteDatabase db;

    public CoolWeatherDao(Context context) {
        dbHelper = new CoolWeatherDBHelper(context);
    }

    /**
     * 保存省列表
     *
     * @param areaItems 省集合
     */
    public void saveProvinces(@NonNull ArrayList<AreaItem> areaItems) {
        db = dbHelper.getWritableDatabase();
        for (AreaItem areaItem : areaItems) {
            ContentValues values = new ContentValues();
            values.put(CoolWeatherDBHelper.KEY_NAME, areaItem.getName());
            values.put(CoolWeatherDBHelper.KEY_CODE, areaItem.getId());
            values.put(CoolWeatherDBHelper.KEY_LEVEL, areaItem.getCurLevel());
            db.insert(CoolWeatherDBHelper.TABLE_PROVINCE, null, values);
        }
        db.close();
    }


    /**
     * 保存市列表
     *
     * @param areaItems 市集合
     */
    public void saveCitys(@NonNull ArrayList<AreaItem> areaItems) {
        db = dbHelper.getWritableDatabase();
        for (AreaItem areaItem : areaItems) {
            ContentValues values = new ContentValues();
            values.put(CoolWeatherDBHelper.KEY_NAME, areaItem.getName());
            values.put(CoolWeatherDBHelper.KEY_CODE, areaItem.getId());
            values.put(CoolWeatherDBHelper.KEY_UP_CODE, areaItem.getUpCode());
            values.put(CoolWeatherDBHelper.KEY_LEVEL, areaItem.getCurLevel());
            db.insert(CoolWeatherDBHelper.TABLE_CITY, null, values);
        }
        db.close();
    }

    /**
     * 保存市列表
     *
     * @param areaItems 县集合
     */
    public void saveCountrys(@NonNull ArrayList<AreaItem> areaItems) {
        db = dbHelper.getWritableDatabase();
        for (AreaItem areaItem : areaItems) {
            ContentValues values = new ContentValues();
            values.put(CoolWeatherDBHelper.KEY_NAME, areaItem.getName());
            values.put(CoolWeatherDBHelper.KEY_CODE, areaItem.getId());
            values.put(CoolWeatherDBHelper.KEY_UP_CODE, areaItem.getUpCode());
            values.put(CoolWeatherDBHelper.KEY_LEVEL, areaItem.getCurLevel());
            values.put(CoolWeatherDBHelper.KEY_WEATHER_ID, areaItem.getWeather_id());
            db.insert(CoolWeatherDBHelper.TABLE_COUNTRY, null, values);
        }
        db.close();
    }

    /**
     * 获取省列表
     *
     * @return 省列表
     */
    public ArrayList<AreaItem> getProvinces() {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CoolWeatherDBHelper.TABLE_PROVINCE, null, null, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        ArrayList<AreaItem> areaItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            AreaItem province = new AreaItem();
            String name = cursor.getString(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_NAME));
            int code = cursor.getInt(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_CODE));
            int level = cursor.getInt(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_LEVEL));
            province.setCurLevel(level);
            province.setName(name);
            province.setId(code);
            areaItems.add(province);
        }
        cursor.close();
        db.close();
        return areaItems;
    }

    /**
     * 获取市列表
     *
     * @param upCode 所属的省代码
     * @return 市列表
     */
    public ArrayList<AreaItem> getCitys(int upCode) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CoolWeatherDBHelper.TABLE_CITY, null, CoolWeatherDBHelper.KEY_UP_CODE + "=?", new String[]{String.valueOf(upCode)}, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        ArrayList<AreaItem> areaItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            AreaItem areaItem = new AreaItem();
            String name = cursor.getString(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_NAME));
            int code = cursor.getInt(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_CODE));
            int level = cursor.getInt(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_LEVEL));
            areaItem.setCurLevel(level);
            areaItem.setName(name);
            areaItem.setId(code);
            areaItems.add(areaItem);
        }
        cursor.close();
        db.close();
        return areaItems;
    }

    /**
     * 获取市列表
     *
     * @param upCode 所属城市code
     * @param upCode
     * @return 县列表
     */
    public ArrayList<AreaItem> getCountrys(int upCode) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CoolWeatherDBHelper.TABLE_COUNTRY, null, CoolWeatherDBHelper.KEY_UP_CODE + "=?", new String[]{String.valueOf(upCode)}, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        ArrayList<AreaItem> areaItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            AreaItem areaItem = new AreaItem();
            String name = cursor.getString(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_NAME));
            int code = cursor.getInt(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_CODE));
            int level = cursor.getInt(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_LEVEL));
            String weatherid = cursor.getString(cursor.getColumnIndex(CoolWeatherDBHelper.KEY_WEATHER_ID));
            areaItem.setWeather_id(weatherid);
            areaItem.setCurLevel(level);
            areaItem.setName(name);
            areaItem.setId(code);
            areaItems.add(areaItem);
        }
        cursor.close();
        db.close();
        return areaItems;
    }
}
