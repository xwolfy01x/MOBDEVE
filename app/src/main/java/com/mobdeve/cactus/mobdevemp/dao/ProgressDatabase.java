package com.mobdeve.cactus.mobdevemp.dao;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProgressDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "progress.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLEPROGRESS = "table_progress";
    public static final String PROGRESS_ID = "id";
    public static final String PROGRESS_USER = "username";
    public static final String PROGRESS_LEVEL = "level";
    public static final String PROGRESS_GOLD = "gold";

    public static final String PROGRESS_CAPLVL = "capLvl";
    public static final String PROGRESS_SHIRTLVL = "shirtLvl";
    public static final String PROGRESS_SHORTLVL = "shortLvl";
    public static final String PROGRESS_SHOELVL = "shoeLvl";

    public static final String PROGRESS_CAPSHARD = "capShard";
    public static final String PROGRESS_SHIRTSHARD = "shirtShard";
    public static final String PROGRESS_SHORTSHARD = "shortShard";
    public static final String PROGRESS_SHOESHARD = "shoeShard";

    public static final String CREATE_PROGRESS_TABLE =
            "create table " + TABLEPROGRESS +
                    "("
                    + PROGRESS_ID + " integer primary key autoincrement, "
                    + PROGRESS_USER + " text, "
                    + PROGRESS_LEVEL + " integer, "
                    + PROGRESS_GOLD + " double, "
                    + PROGRESS_CAPLVL + " integer, "
                    + PROGRESS_SHIRTLVL + " integer, "
                    + PROGRESS_SHORTLVL + " integer, "
                    + PROGRESS_SHOELVL + " integer, "
                    + PROGRESS_CAPSHARD + " integer, "
                    + PROGRESS_SHIRTSHARD + " integer, "
                    + PROGRESS_SHORTSHARD + " integer, "
                    + PROGRESS_SHOESHARD + " integer) ";
    public ProgressDatabase (Context context) {super(context,DATABASE_NAME, null,DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_PROGRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(" DROP TABLE IF EXISTS " + TABLEPROGRESS);
        onCreate(db);
    }
}