package mtrzepacz.androidgraphicfilters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/**
 * Created by Mateusz on 2018-01-05.
 */

public class FilteredImage {

    private Bitmap startImage;
    private Bitmap finalImage;
    private int width;
    private int height;

    //constructor
    public  FilteredImage(Bitmap bitmap)
    {
        this.startImage = bitmap;
        this.finalImage = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public FilteredImage(String string)
    {
        this.startImage = BitmapFactory.decodeFile(string);
        this.finalImage = BitmapFactory.decodeFile(string);
        this.width = this.startImage.getWidth();
        this.height = this.startImage.getHeight();
    }

    //getters and setters
    public Bitmap getStartImage() {
        return startImage;
    }

    public void setStartImage(Bitmap startImage) {
        this.startImage = startImage;
    }

    public Bitmap getFinalImage() {
        return finalImage;
    }

    public void setFinalImage(Bitmap finalImage) {
        this.finalImage = finalImage;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void toBlackWhite()
    {
       for(int i = 0 ; i < this.height ; i++){
           for(int j = 0 ; j < this.width ; j++ ){
               int pixel = startImage.getPixel(j,i);
               int alfa = Color.alpha(pixel);
               int red = Color.red(pixel);
               int green = Color.green(pixel);
               int blue = Color.blue(pixel);
               int average = (red + green + blue)/3;
               this.finalImage.setPixel(j,i,Color.rgb(average,average,average));
           }
       }
    }
}
