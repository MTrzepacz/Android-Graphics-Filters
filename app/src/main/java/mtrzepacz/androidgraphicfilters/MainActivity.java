package mtrzepacz.androidgraphicfilters;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    //Declarations of layout components
    Button buttonChooseImage, buttonSave, buttonOpenFilters,buttonCancel;
    ImageView imageView;
    String currentPhotoPath;
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();


            } catch (IOException ex)
            {

            }
            if(photoFile != null)
            {
                Uri URIx = FileProvider.getUriForFile(this,"mtrzepacz.androidgraphicfilters",photoFile);
                Uri photoURI = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }
    //get picture which was taken and shows it in Imageview
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
       if(requestCode ==  REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
       {
           /* gets thumbnail only
          Bundle extras = data.getExtras();
          Bitmap image = (Bitmap) extras.get("data");
          imageView.setImageBitmap(image);
           */

           //getting full size image
           File imgFile = new File(currentPhotoPath);
           Bitmap rotatedBitmap = null;
           //rotating image because most of the time its saved as landscape
           if(imgFile.exists())
           {
               Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
               try {
                   //ExifInterface lets me check orientation
                   ExifInterface exifInterface = new ExifInterface(currentPhotoPath);
                   int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                           ExifInterface.ORIENTATION_UNDEFINED);
                   switch(orientation)
                   {
                       case ExifInterface.ORIENTATION_ROTATE_90:
                           rotatedBitmap = rotateImage(bitmap,90);
                                   break;
                       case ExifInterface.ORIENTATION_ROTATE_180:
                           rotatedBitmap = rotateImage(bitmap,180);
                           break;
                       case ExifInterface.ORIENTATION_ROTATE_270:
                           rotatedBitmap = rotateImage(bitmap,270);
                           break;
                       case ExifInterface.ORIENTATION_NORMAL:
                           default:
                               rotatedBitmap = bitmap;
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
               imageView.setImageBitmap(rotatedBitmap);
               addPicToGallery();
           }
       }
    }
    //creating image file with unique name
    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyy-MM-DD-HH-MM-SS").format(new Date());
        String imageFileName = " JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            currentPhotoPath = image.getAbsolutePath();
            return image;
    }

    // rotating image for displaying it correctly
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void addPicToGallery()
    {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(currentPhotoPath);
        Uri uri =  Uri.fromFile(file);
        intent.setData(uri);
        this.sendBroadcast(intent);

    }

}
