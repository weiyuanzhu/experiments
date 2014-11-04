package weiyuan.mymodule.app2.systemsettingtest.systemsettingtest;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.Locale;


public class MainActivity extends ActionBarActivity {


    @Override
    public void onAttachedToWindow() {
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
        super.onAttachedToWindow();
    }

    @Override
    public void onBackPressed() {
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLanguage(View view)
    {

        try {
            Class cls_ActivityManagerNative=Class.forName("android.app.ActivityManagerNative");
            Object activityManagerNative=cls_ActivityManagerNative.getDeclaredMethod("getDefault",null).invoke(null, null);
            Configuration configuration;
            configuration = (Configuration)activityManagerNative.getClass().getMethod("getConfiguration",null).invoke(activityManagerNative, null);
            configuration.locale= Locale.CHINESE;
            configuration.getClass().getDeclaredField("userSetLocale").setBoolean(configuration, true);

            cls_ActivityManagerNative.getMethod("updateConfiguration", Configuration.class).invoke(activityManagerNative, configuration);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.d("Focus debug", "Focus changed !");

        if(!hasFocus) {
            Log.d("Focus debug", "Lost focus !");

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

}


