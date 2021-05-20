package com.mobdeve.cactus.mobdevemp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobdeve.cactus.mobdevemp.models.Progress;

public class ProgressDAOSQLImpl implements ProgressDAO {
    private SQLiteDatabase database;
    private ProgressDatabase progressDatabase;

    public ProgressDAOSQLImpl(Context context) {
        progressDatabase = new ProgressDatabase(context);
    }


    @Override
    public Progress getOneProgress(String username) {
        Progress oneProgress;
        database = progressDatabase.getReadableDatabase();
        String QUERY = "SELECT * FROM " + ProgressDatabase.TABLEPROGRESS + " WHERE " + ProgressDatabase.PROGRESS_USER + " = " + username;

        Cursor c = database.rawQuery(QUERY, null);

        if (c != null) {
            c.moveToFirst();
            oneProgress = new Progress(c.getString(c.getColumnIndex(ProgressDatabase.PROGRESS_USER)));
            oneProgress.setGold(c.getDouble(c.getColumnIndex(ProgressDatabase.PROGRESS_GOLD)));

            oneProgress.setCaplvl(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_CAPLVL)));
            oneProgress.setShirtlvl(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHIRTLVL)));
            oneProgress.setShortlvl(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHORTLVL)));
            oneProgress.setShoelvl(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHOELVL)));

            oneProgress.setCapshard(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_CAPSHARD)));
            oneProgress.setShirtshard(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHIRTSHARD)));
            oneProgress.setShortshard(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHORTSHARD)));
            oneProgress.setShoeshard(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHOESHARD)));

            oneProgress.setCapVal(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_CAPVAL)));
            oneProgress.setShirtVal(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHIRTVAL)));
            oneProgress.setShortVal(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHORTVAL)));
            oneProgress.setShoeVal(c.getInt(c.getColumnIndex(ProgressDatabase.PROGRESS_SHOEVAL)));
        } else {
            oneProgress = new Progress(c.getString(c.getColumnIndex(ProgressDatabase.PROGRESS_USER)));
        }

        database.close();
        return oneProgress;
    }

    @Override
    public void addProgress(Progress oneProgress) {
        database = progressDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ProgressDatabase.PROGRESS_USER, oneProgress.getUsername());
        values.put(ProgressDatabase.PROGRESS_GOLD, oneProgress.getGold());

        values.put(ProgressDatabase.PROGRESS_CAPLVL, oneProgress.getCaplvl());
        values.put(ProgressDatabase.PROGRESS_SHIRTLVL, oneProgress.getShirtlvl());
        values.put(ProgressDatabase.PROGRESS_SHORTLVL, oneProgress.getShortlvl());
        values.put(ProgressDatabase.PROGRESS_SHOELVL, oneProgress.getShoelvl());

        values.put(ProgressDatabase.PROGRESS_CAPSHARD, oneProgress.getCapshard());
        values.put(ProgressDatabase.PROGRESS_SHIRTSHARD, oneProgress.getShirtshard());
        values.put(ProgressDatabase.PROGRESS_SHORTSHARD, oneProgress.getShortshard());
        values.put(ProgressDatabase.PROGRESS_SHOESHARD, oneProgress.getShoeshard());

        values.put(ProgressDatabase.PROGRESS_CAPVAL, oneProgress.getCapVal());
        values.put(ProgressDatabase.PROGRESS_SHIRTVAL, oneProgress.getShirtVal());
        values.put(ProgressDatabase.PROGRESS_SHORTVAL, oneProgress.getShortVal());
        values.put(ProgressDatabase.PROGRESS_SHOEVAL, oneProgress.getShoeVal());

        long entry = database.insert(ProgressDatabase.TABLEPROGRESS, null, values);
        database.close();
    }
}