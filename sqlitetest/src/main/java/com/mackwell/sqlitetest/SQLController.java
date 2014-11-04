package com.mackwell.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by weiyuan zhu on 13/10/14.
 */
public class SQLController {

    private SQLiteDatabase database;
    private MyDBHandler helper;

    public SQLController(Context context)
    {
        helper = new MyDBHandler(context);

    }

    public void open(){
        database = helper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }



    public Cursor readData() {
        String[] allColumns = new String[] { helper.COLUMN_ID,helper.COLUMN_PANELLOCATION,helper.COLUMN_PANELIP};
        Cursor c = database.query(helper.TABLE_PANEL, allColumns, null,null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void addProduct(Panel product){
        ContentValues values = new ContentValues();
        values.put(MyDBHandler.COLUMN_PANELLOCATION,product.get_panelLocation());
        values.put(MyDBHandler.COLUMN_PANELIP,product.get_ip());

        database.insert(MyDBHandler.TABLE_PANEL,null,values);
    }

    public Panel findProduct(String productName)
    {
        String query  = "SELECT * FROM " + MyDBHandler.TABLE_PANEL + " WHERE " + MyDBHandler.COLUMN_PANELLOCATION + " = \"" + productName  + "\"";

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Panel product = new Panel();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            product.set_id((Integer.parseInt(cursor.getString(0))));
            product.set_ip(cursor.getString(2));
            product.set_panelLocation(cursor.getString(1));

            cursor.close();

        }else{
            product = null;
        }

        db.close();
        return product;
    }

    public boolean deleteProduct(String panelIP){

        boolean result = false;

        String query = "SELECT * FROM " + MyDBHandler.TABLE_PANEL + " WHERE " + MyDBHandler.COLUMN_PANELIP + " = \"" + panelIP + "\"";


        Cursor cursor = database.rawQuery(query,null);

        Panel panel = new Panel();

        if(cursor.moveToFirst())
        {
            panel.set_ip(cursor.getString(2));

            database.delete(MyDBHandler.TABLE_PANEL, MyDBHandler.COLUMN_PANELIP + " = ?", new String[]{panel.get_ip()});
            cursor.close();

            result = true;

        }
        return result;

    }
}
