/*  
    Accepts an image file as a command line argument.
    Adds the ARGB values of each pixel in the photo, then outputs which channel has the highest total value.
    Feb 8, 2019
*/

package mostcommoncolor;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/*
TODO:
    Verify command line arguments
    Check if coarseness = 0
    Clean code
*/


public class MostCommonColor {

    public static void main(String[] args){
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);
        commandLineArgs.set();
        boolean reportAverage = commandLineArgs.reportAverage();        //More verbose. Will print the average RGB value of the image
        boolean colorOnly = commandLineArgs.colorOnly();                //ONLY prints the color. Could be used for scripting.
        int coarseness = commandLineArgs.coarseness();
        
        Photo photo = new Photo(loadImage(args[0]));    //load in photo
        getCommonValue(photo,coarseness,reportAverage,colorOnly);
        
    }
    
    public static BufferedImage loadImage(String filename){
        //Loads an image given as an argument. Returns the bufferedImage
        File f = new File(filename);
        BufferedImage image = null;
        
        try{
             image = ImageIO.read(f);
        }catch(IOException e){
            System.out.println("Error: " + e);
            CommandLineArgs.failedToStart();
        }
        return image;
    }
    
    public static void getCommonValue(Photo photo, int coarseness, boolean reportAverage, boolean colorOnly){
        //Will iterate through every pixel in the photo and add the RGB values.
        // Finds the average values for each channel across every pixel in the photo.
        Pixel p = new Pixel();
    
        int height = photo.getHeight();
        int width = photo.getWidth();
        int totalPixels = 0;
        long aTotal = 0;
        long rTotal = 0;
        long gTotal = 0;
        long bTotal = 0;
        float aAvg;
        float rAvg;
        float gAvg;
        float bAvg;
        
        if(!colorOnly){
        System.out.println("\nHeight: " + height);
        System.out.println("Width: " + width);
        }
        
        for(int x = 0; x < width; x+= coarseness){
            for(int y = 0; y < height; y+= coarseness){
                if(!colorOnly){
                    System.out.print("X: " + x + " Y: " +y + "\r");
                }
                
                p.setPixel(photo.getRGB(x, y));
                aTotal += p.getA();
                rTotal += p.getR();
                gTotal += p.getG();
                bTotal += p.getB();
                totalPixels++;
            }
        }
        aAvg = aTotal/totalPixels;
        rAvg = rTotal/totalPixels;
        gAvg = gTotal/totalPixels;
        bAvg = bTotal/totalPixels;
        if(reportAverage){
            float checkedPercentage = (float)totalPixels/(width*height);
            String checkedPercentageFloat = String.format("%.3f",checkedPercentage * 100);
            System.out.println("Checked " + checkedPercentageFloat + "% of pixels");
        }
        findMostCommonValue(aAvg,rAvg,gAvg,bAvg,reportAverage,colorOnly);
    }
    
    public static void findMostCommonValue(float aAvg, float rAvg, float gAvg, float bAvg, boolean reportAverage, boolean colorOnly){
        float   maxValue = Math.max(rAvg,gAvg);
                maxValue = Math.max(maxValue,bAvg);
        
                
        if(!colorOnly){
            System.out.println("");
        }
        if(maxValue == rAvg){
            System.out.println("\nRed");
        }else if(maxValue == gAvg){
            System.out.println("\nGreen");
        }else if(maxValue == bAvg){
            System.out.println("\nBlue");
        }
        
        if(reportAverage == true){
            System.out.println("\na: " + aAvg + "\nr: " + rAvg + "\nb: " + bAvg + "\ng: " + gAvg);
        }
        
        if(!colorOnly){
            System.out.println("");
        }
    }
}
