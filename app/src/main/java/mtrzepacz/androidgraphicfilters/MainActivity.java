package mtrzepacz.androidgraphicfilters;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity {

    //statics
    public static final int MEDIA_TYPE_IMAGE = 0;
    //request take image code
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //request pick photo code
    private static final int REQUEST_IMAGE_PICK = 2;
    //reqiest pick filter
    private static final int REQUEST_FILTER_PICK = 3;

    //Declarations of layout components
    private Button buttonChooseImage, buttonSave, buttonOpenFilters,buttonCancel;
    private  ImageView imageView;
    private String currentPhotoPath;

    private FilteredImage filteredImage;
    private FilteredImage secondImage;
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
                selectImage();
            }
        });
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonOpenFilters = (Button) findViewById(R.id.buttonApplyFilter);
        buttonOpenFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filteredImage == null)
                    Toast.makeText(MainActivity.this, "Pick Image First", Toast.LENGTH_SHORT).show();
                else
                    openFilterList();
            }
        });
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
       if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
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
               filteredImage = new FilteredImage(rotatedBitmap);
           }
       }
       if( requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK)
       {
           Uri selectedImage = data.getData();
           try {
                InputStream is = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                filteredImage = new FilteredImage(bitmap);
                is.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
           Toast.makeText(this, "we made new filtered image!!", Toast.LENGTH_SHORT).show();
           imageView.setImageURI(selectedImage);
       }
       if(requestCode == REQUEST_FILTER_PICK && resultCode == RESULT_OK)
       {
           String pickedFilter = data.getExtras().getString("Filter Name");
           if(pickedFilter.equals("BlackWhite")) {
               Toast.makeText(this, "blackwhite", Toast.LENGTH_SHORT).show();
               filteredImage.toBlackWhite();
               Bitmap bitmap = filteredImage.getFinalImage();
               imageView.setImageBitmap(bitmap);
           }
           if(pickedFilter.equals("Negative"))
           {
               Toast.makeText(this, "negative", Toast.LENGTH_SHORT).show();
               filteredImage.toNegative();
               Bitmap bitmap = filteredImage.getFinalImage();
               imageView.setImageBitmap(bitmap);
           }
           if(pickedFilter.equals("GaussianBlur"))
           {
               Toast.makeText(this, "gaussian blur", Toast.LENGTH_SHORT).show();
               filteredImage.toGaussianBlur();
               Bitmap bitmap = filteredImage.getFinalImage();
               imageView.setImageBitmap(bitmap);
           }

       }
    }
    // rotating image for displaying it correctly
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    //getting file
    private File getOutputMediaFile(int type)
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

    private void selectImage()
    {
        final String[] options = {"Take Photo","Pick Image", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {
                    case 0:
                        takePicture();
                        break;
                    case 1:
                        getPhotoFromGallery();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    public void getPhotoFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_IMAGE_PICK);
    }
    //will be deleted later
    public void applyFilter(FilteredImage filteredImage)
    {
        Toast.makeText(this, "pre filtr ", Toast.LENGTH_SHORT).show();
        filteredImage.toBlackWhite();
        Toast.makeText(this, "filtered image == null ", Toast.LENGTH_SHORT).show();
        Bitmap bitmap = filteredImage.getFinalImage();
        Toast.makeText(this, "get final image", Toast.LENGTH_SHORT).show();
        imageView.setImageBitmap(bitmap);
    }
    public void openFilterList()
    {
        Intent intent = new Intent(this,listOfFilters.class);
        startActivityForResult(intent,REQUEST_FILTER_PICK);
    }

}
