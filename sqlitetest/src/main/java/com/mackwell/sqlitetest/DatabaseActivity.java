package com.mackwell.sqlitetest;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class DatabaseActivity extends ActionBarActivity {

    private final String TAG = "DatabaseTest";
    private EditText panelIP;
    private EditText panelLocation;

    SimpleCursorAdapter mAdapter;
    private ListView listView;

    private Cursor mCursor;
    private SQLController sqlControler;

    String[] from;
    int[] to;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        panelIP = (EditText) findViewById(R.id.editText);
        panelLocation = (EditText) findViewById(R.id.editText1);
        listView = (ListView) findViewById(R.id.listView);

        sqlControler = new SQLController(this);
        sqlControler.open();

        //cursor
        from = new String[]{
                MyDBHandler.COLUMN_ID,
                MyDBHandler.COLUMN_PANELLOCATION,
                MyDBHandler.COLUMN_PANELIP};

        mCursor = sqlControler.readData();

        to = new int[] { R.id.textView, R.id.textView2,R.id.textView3 };

        mAdapter = new SimpleCursorAdapter(
                DatabaseActivity.this, R.layout.row_layout, mCursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        mAdapter.notifyDataSetChanged();
        listView.setAdapter(mAdapter);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.database, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addProduct(View view)
    {
        Log.d(TAG, "Panel IP: " + panelIP.getText());
        Log.d(TAG, "Panel Location " + panelLocation.getText());

        Panel panel = new Panel(panelIP.getText().toString(), panelLocation.getText().toString());

        sqlControler.open();

        //insert into database
        sqlControler.addProduct(panel);

        panelIP.setText("");
        panelLocation.setText("");


        mCursor = sqlControler.readData();
        sqlControler.close();
//        mAdapter = new SimpleCursorAdapter(
//                DatabaseActivity.this, R.layout.row_layout, mCursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        mAdapter.changeCursor(mCursor);
        mAdapter.notifyDataSetChanged();
//        listView.setAdapter(mAdapter);



    }

    public void deleteProduct(View view)
    {
        Log.d(TAG, "Product name: " + panelIP.getText());
        sqlControler.open();

        String test = panelIP.getText().toString();
        if(test!=null && !test.equals("")){
            sqlControler.deleteProduct(test);
        }

        mCursor = sqlControler.readData();

        mAdapter.changeCursor(mCursor);
        mAdapter.notifyDataSetChanged();

        sqlControler.close();
    }

}
