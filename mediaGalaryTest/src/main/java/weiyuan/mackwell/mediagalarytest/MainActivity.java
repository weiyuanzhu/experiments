package weiyuan.mackwell.mediagalarytest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

    public static final int RESULT_LOAD_IMAGE = 10;
    public static final String TEST = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getImage(View view)
    {
        Log.i("test","button test");
        getImageFromIntent();
    }

    private void getImageFromIntent()
    {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("test", Integer.toString(requestCode));
        Log.i("test",Integer.toString(resultCode));

        if (resultCode==RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {

            Uri selectedImage = data.getData();
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(selectedImage);

            Log.i(TEST,selectedImage.toString());
        }


    }
}
