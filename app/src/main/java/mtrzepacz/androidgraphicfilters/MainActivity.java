package mtrzepacz.androidgraphicfilters;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //Declarations of layout components
    Button buttonChooseImage, buttonSave, buttonOpenFilters,buttonCancel;
    ImageView imageView;
    //request code for taking picture
    static final int REQUEST_IMAGE_CAPTURE = 1;

    Uri selectedImage;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting choose image button and creating onClickListener
        buttonChooseImage = (Button) findViewById(R.id.buttonChooseImage);
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        imageView = (ImageView) findViewById(R.id.imageView);
    }
    //calling android camera app to take picture
    private void takePicture()
    {
        Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }

    }
    //get picture which was taken and shows it in Imageview
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
       if(requestCode ==  REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
       {
           Bundle extras = data.getExtras();
           Bitmap image = (Bitmap) extras.get("data");
           imageView.setImageBitmap(image);
       }
    }
}
