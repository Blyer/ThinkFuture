package org.base.platform.utils.cachehelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by YinShengyi on 2016/11/29.
 */
public class DbHelper {
    private static final String TAG = "db";

    private static final int LOCK_RETRY_CHANCES = 3;

    public static final Cursor rawQuery(SQLiteDatabase db, String sql) {
        Cursor cursor = null;

        for (int chance = 0; chance < LOCK_RETRY_CHANCES; chance++) {
            boolean locked = false;

            try {
                cursor = db.rawQuery(sql, null);
            } catch (SQLiteException e) {
                e.printStackTrace();

                locked = isSQLiteDatabaseLockedException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (locked) {
                Log.w(TAG, "locked");
            }

            if (cursor != null || !locked) {
                break;
            }
        }

        return LockSafeCursor.wrap(cursor);
    }

    public static final void exeSQL(SQLiteDatabase db, String sql) {
        for (int chance = 0; chance < LOCK_RETRY_CHANCES; chance++) {
            boolean ok = false;
            boolean locked = false;

            try {
                db.execSQL(sql);
                ok = true;
            } catch (SQLiteException e) {
                e.printStackTrace();
                locked = isSQLiteDatabaseLockedException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (locked) {
                Log.w(TAG, "locked");
            }

            if (ok || !locked) {
                break;
            }
        }
    }


    public static boolean isTableExists(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type=? AND name=?", new String[]{"table", tableName});
        if (cursor == null || !cursor.moveToFirst()) {
            return false;
        }

        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }


    public static final boolean isSQLiteDatabaseLockedException(SQLiteException e) {
        String message = e.getMessage();

        return !TextUtils.isEmpty(message) && message.contains("lock");
    }
}
