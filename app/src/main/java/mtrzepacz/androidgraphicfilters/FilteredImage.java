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
    public FilteredImage(Bitmap bitmap)
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
        this.finalImage = Bitmap.createBitmap(this.getWidth(), this.getHeight(), this.startImage.getConfig());
          for(int i = 0 ; i < this.height ; i++){
            for(int j = 0 ; j < this.width ; j++ ){
               int pixel = startImage.getPixel(j,i);
               int red = Color.red(pixel);
               int green = Color.green(pixel);
               int blue = Color.blue(pixel);
               int average = (red + green + blue)/3;
               this.finalImage.setPixel(j,i,Color.rgb(average,average,average));
            }
        }
        this.startImage = this.finalImage;
    }

    public void toNegative()
    {
        this.finalImage = Bitmap.createBitmap(this.getWidth(), this.getHeight(), this.startImage.getConfig());
        for(int i = 0 ; i < this.height ; i++){
            for(int j = 0 ; j < this.width ; j++ ) {
                int pixel = startImage.getPixel(j, i);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int newRed = 255 - red;
                int newGreen = 255 - green;
                int newBlue = 255 - blue;
                this.finalImage.setPixel(j,i,Color.rgb(newRed,newGreen,newBlue));
            }
        }
        this.startImage = this.finalImage;
    }

    public void toGaussianBlur()
    {
        this.finalImage = Bitmap.createBitmap(this.getWidth(), this.getHeight(), this.startImage.getConfig());
        int kernelWidth = 3;
        int kernelHeight = 3;
        int Kernel[][] = {

                {1,2,1},
                {2,4,2},
                {1,2,1}

        };
        for(int i = 0 ; i < this.width ; i++){
            for(int j = 0 ; j < this.height; j++){
                int pixel = this.startImage.getPixel(i,j);
                int tempRed = Color.red(pixel);
                int tempGreen = Color.green(pixel);
                int tempBlue = Color.blue(pixel);
                    for(int x = 0 ; x < kernelHeight ; x++) {
                        for(int y = 0 ; y < kernelWidth ; y++){
                            int tempColor = (this.startImage.getPixel((this.width + i + x - (kernelWidth/2)) % this.width,
                                    (this.height + j + y  - (kernelWidth/2)) % this.height));
                             tempRed = Color.red(tempColor);
                             tempGreen = Color.green(tempColor);
                             tempBlue = Color.blue(tempColor);

                            // obliczenie nowego koloru
                            tempRed = (int)(Color.red(pixel) + tempRed * (Kernel[x][y])/16);
                            tempGreen = (int)(Color.green(pixel)+ tempGreen * (Kernel[x][y])/16);
                            tempBlue =  (int) (Color.blue(pixel) + tempBlue * (Kernel[x][y])/16);

                            //przycinanie koloru
                            if (tempRed > 255) {
                                tempRed = 255;
                            } else if (tempRed< 0) {
                                tempRed = 0;
                            }

                            if (tempGreen > 255) {
                                tempGreen = 255;
                            } else if (tempGreen< 0) {
                                tempGreen = 0;
                            }

                            if (tempBlue > 255) {
                                tempBlue = 255;
                            } else if (tempBlue< 0) {
                                tempBlue = 0;
                            }

                        }
                    }
                    this.finalImage.setPixel(i,j,Color.rgb(tempRed,tempGreen,tempBlue));
                   // this.finalImage.setPixel(i, j, Color.rgb(Color.red(pixel),Color.green(pixel),Color.blue(pixel)));
                }
            }
    }
}
