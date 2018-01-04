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
import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    //Declarations of layout components
    private Button buttonChooseImage, buttonSave, buttonOpenFilters,buttonCancel;
    private  ImageView imageView;
    private String currentPhotoPath;
    //request code for taking picture


    private Uri fileUri;

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
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
    //get picture which was taken and shows it in Imageview
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
       if(requestCode ==  REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
       {
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
            //   notifyMediaStoreScanner(imgFile);
           }
       }
    }
    //creating image file with unique name

    // rotating image for displaying it correctly
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    //getting file
    private File getOutputMediaFile( int type)
    {
        //location in external storage
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"APP FILES");
        if(!mediaStorageDir.exists())
        {
            if(!mediaStorageDir.mkdirs())
            {
                Toast.makeText(this, "filed to create directory for files", Toast.LENGTH_SHORT).show();
                return  null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-DD-HH-MM-SS").format(new Date());
        File mediaFile;
        if( type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
            currentPhotoPath = mediaFile.getAbsolutePath();
        }
        else
        {
            return  null;
        }
        return mediaFile;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    //adding picture to gallery
    private void addPicToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
