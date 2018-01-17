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

    public void toSepia()
    {
        this.finalImage = Bitmap.createBitmap(this.getWidth(), this.getHeight(), this.startImage.getConfig());
        for(int i = 0 ; i < this.height ; i++){
            for(int j = 0 ; j < this.width ; j++ ) {
                int pixel = startImage.getPixel(j, i);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int newRed = (int) (0.393 * red + 0.769 * green + 0.168 * blue);
                int newGreen = (int) (0.349 * red + 0.689 * green + 0.168 * blue);
                int newBlue = (int) (0.272 * red + 0.534 * green + 0.131 * blue);
                if (newRed > 255) {
                    red = 255;
                } else {
                    red = newRed;
                }
                if (newGreen > 255) {
                    green = 255;
                } else {
                    green = newGreen;
                }
                if (newBlue > 255) {
                    blue = 255;
                } else {
                    blue = newBlue;
                }
                this.finalImage.setPixel(j,i,Color.rgb(red,green,blue));
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

    public void toSharpen()
    {
        this.finalImage = Bitmap.createBitmap(this.getWidth(), this.getHeight(), this.startImage.getConfig());
        for(int y = 0 ; y < this.height ; y++)
        {
            for( int  x = 0 ;  x < this.width ; x++)
            {
                int sumR = 0;
                int sumG = 0;
                int sumB = 0;
                int n = 0;
                //przejscie po sasiadach
                for (int ny = Math.max(0,y-1);
                     ny < Math.min(this.getHeight(),y + 1 + 1);
                     ny++)
                {
                    for ( int nx = Math.max(0,x -1);
                          nx < Math.min(this.getWidth(), x + 1 + 1);
                          nx++)
                    {
                        //dodawanie wartosci sasiadow ale bez pixela na ktorym sie pracuje)
                        if(nx != x  || ny != y)
                        {
                            int c = this.startImage.getPixel(nx, ny);
                            sumR += Color.red(c);
                            sumG += Color.green(c);
                            sumB += Color.blue(c);
                            n++;
                        }
                    }
                }
                // kolor obecnego pixela - suma kolorow sasiadow
                int c = this.startImage.getPixel(x, y);
                int red =  Color.red(c)*(n+1) - sumR;
                int green =  Color.green(c)*(n+1) - sumG;
                int blue =  Color.blue(c)*(n+1) - sumB;
                //przycinanie granic kolorow
                if (red > 255) {
                    red = 255;
                } else if (red< 0) {
                    red = 0;
                }

                if (green > 255) {
                    green = 255;
                } else if (green< 0) {
                    green = 0;
                }

                if (blue > 255) {
                    blue = 255;
                } else if (blue< 0) {
                    blue = 0;
                }
                //ustawienie nowego koloru
                //finalImage.setRGB(x, y, newColor.getRGB());
                this.finalImage.setPixel(x,y,Color.rgb(red,green,blue));

            }
        }
    }

    public void toEdgeDetection()
    {
        this.finalImage = Bitmap.createBitmap(this.getWidth(), this.getHeight(), this.startImage.getConfig());
        //delete this to make edge detection with colors
        this.toBlackWhite();
        //
        int Kernel[][] = {

                {1,0,-1},
                {2,0,-2},
                {1,0,-1}

        };
        int KernelSize = 3;
        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                int pixel = this.startImage.getPixel(i,j);
                int tempRed = Color.red(pixel);
                int tempGreen = Color.green(pixel);
                int tempBlue = Color.blue(pixel);
                for (int x = 0; x < KernelSize; ++x) {
                    for (int y = 0; y < KernelSize; ++y) {
                        int tempColor = this.startImage.getPixel((this.width + i + x) % this.width,
                                (this.height + j + y) % this.height);

                        int red = Color.red(tempColor);
                        int green = Color.green(tempColor);
                        int blue = Color.blue(tempColor);

                        red = (Color.red(pixel) + red * (Kernel[x][y]));
                        green = (Color.green(pixel) + green * (Kernel[x][y]));
                        blue = (Color.blue(pixel) + blue * (Kernel[x][y]));

                        tempRed = Math.min(Math.max(red, 0), 255);
                        tempGreen = Math.min(Math.max(green, 0), 255);
                        tempBlue = Math.min(Math.max(blue, 0), 255);

                        pixel = Color.rgb(tempRed,tempGreen,tempBlue);
                    }
                }
                this.finalImage.setPixel(i, j, Color.rgb(tempRed,tempGreen,tempBlue));
            }
        }
    }

}
