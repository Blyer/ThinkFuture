package org.base.platform.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;

import org.base.platform.utils.cachehelper.DbHelper;

/**
 * Created by YinShengyi on 2016/11/29.
 */
public class DbCacheUtils extends SQLiteOpenHelper {
    public DbCacheUtils(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (db != null && !DbHelper.isTableExists(db, "db_cache")) {
            createTable(db);
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putString(String key, String value) {
        String sql = "insert or replace into db_cache(key, value) values('" + key + "','" + value + "')";
        DbHelper.exeSQL(getWritableDatabase(), sql);
    }

    public String getString(String key, String def) {
        String value = null;
        String sql = "select value from db_cache where key='" + key + "'";
        Cursor cursor = DbHelper.rawQuery(getReadableDatabase(), sql);
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                value = cursor.getString(0);
            }
        } catch (Exception e) {
        } finally {//必须写这里 解决某些手机rom问题 如小米
            if (cursor != null) {
                cursor.close();
            }
        }
        return value == null ? def : value;
    }

    public void putInt(String key, int value) {
        putString(key, String.valueOf(value));
    }

    public int getInt(String key, int def) {
        String value = getString(key, null);
        try {
            return value == null ? def : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public void putLong(String key, long value) {
        putString(key, String.valueOf(value));
    }

    public long getLong(String key, long def) {
        String value = getString(key, null);
        try {
            return value == null ? def : Long.parseLong(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public void putBoolean(String key, boolean value) {
        putString(key, BooleanToString(value));
    }

    public boolean getBoolean(String key, boolean def) {
        String value = getString(key, null);
        return value == null ? def : StringToBoolean(value, def);
    }

    private void createTable(SQLiteDatabase db) {
        String createBgConfig = "CREATE TABLE IF NOT EXISTS db_cache ("
                + "key varchar(50)  NOT NULL primary key," + "value TEXT  NOT NULL )";
        db.execSQL(createBgConfig);
    }

    private String BooleanToString(boolean bool) {
        return bool ? "1" : "0";
    }

    private boolean StringToBoolean(String value, boolean def) {
        if (value.equals("0")) {
            return false;
        } else if (value.equals("1")) {
            return true;
        } else {
            return def;
        }
    }

    private static final int MAX_CHANCE = 3;

    @Override
    public SQLiteDatabase getReadableDatabase() {

        for (int i = 0; i < MAX_CHANCE; ++i) {
            try {
                return super.getReadableDatabase();
            } catch (SQLiteException e) {
                if (DbHelper.isSQLiteDatabaseLockedException(e)) {
                    SystemClock.sleep(30);
                } else {
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        for (int i = 0; i < MAX_CHANCE; ++i) {
            try {
                return super.getWritableDatabase();
            } catch (SQLiteException e) {
                if (DbHelper.isSQLiteDatabaseLockedException(e)) {
                    SystemClock.sleep(30);
                } else {
                    break;
                }
            }
        }
        return null;
    }
}
