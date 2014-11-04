package com.mackwell.sqlitetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by weiyuan zhu on 10/10/14.
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "panelDB.db";
    public static final String TABLE_PANEL = "panels";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PANELLOCATION = "panel_location";
    public static final String COLUMN_PANELIP = "panel_ip";



    public MyDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PANEL
                + " (" + COLUMN_ID + " INTEGER, "
                + COLUMN_PANELLOCATION + " TEXT, " + COLUMN_PANELIP + " TEXT PRIMARY KEY NOT NULL" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANEL);
        onCreate(db);

    }


}
