import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileIO {
	private static final String PATH_TO_FILE = "C:/Users/tuan.duong/Desktop/Nghi/landmarkData/";
	private static final String PATH_TO_FILE_2 = "C:/Users/tuan.duong/Desktop/Nghi/data/TestFExtracted/";
	FileOutputStream fos;
    DataOutputStream dos;
    FileInputStream fis;
    DataInputStream dis;
    public double min = 1.0;
    public double max = -1.0;
    
    public FileIO (FileInputStream fis, FileOutputStream fos) {
    	this.fis = fis;
    	this.fos = fos;
    	dos = new DataOutputStream(fos);
    	dis = new DataInputStream(fis);
    }
    
    public void close() throws IOException{
    	dis.close();
    	dos.close();
    }
    
    public void writeFile(int [] data) throws IOException {
        for (int i = 0; i < data.length; ++i) {
            System.out.print (data[i] +  " ");
            dos.writeInt(data[i]);
        }
        System.out.println ("\nDone writing.");
    }

    public double[][] readFile(int width, int height) throws IOException {
        double[][] readInData = new double[width][height];

        for (int i = 0; i < width; ++i) {
        	for (int j = 0; j < height; ++j) {
        		readInData[i][j] = dis.readDouble();
        		if (readInData[i][j] > max) {
        			max = readInData[i][j];
        		}
        		else if (readInData[i][j] < min) {
        			min = readInData[i][j];
        		}
                
                //System.out.print (readInData[i][j] + " ");
        	}
        	//System.out.println("");
        }
        //System.out.println("MAX = " + max);
		//System.out.println("MIN = " + min);
        dis.close();
        return readInData;
    }
    
    public int[][] retrieveImageData(double[][] data) {
    	int max = 0;
    	int min = 255;
    	int width = data.length;
    	int height = data[0].length;
    	int[][] imageData = new int[width][height];
    	for (int i = 0; i < width; ++i) {
    		for (int j = 0; j < height; ++j) {
    			imageData[i][j] = normalize(data[i][j]);
    			if (imageData[i][j] > max) max = imageData[i][j];
    			if (imageData[i][j] < min) min = imageData[i][j];
    			//System.out.print(imageData[i][j] + " ");
    		}
    		//System.out.println("");
    	}
    	System.out.println("max = " + max);
    	System.out.println("min = " + min);
    	return imageData;
    }
    
    private int normalize(double val) {
    	//TODO: floor or ceiling value?? 
    	System.out.println("tval = " + val);
    	System.out.println("\tmax = " + max);
    	System.out.println("\tmin = " + min);
    	int ret =  (int) ((val  - (min))*(255 - 1) / (max - min));
    	System.out.println("\tret = " + ret);
    	return ret;
    }
    
    private static void printArray (int [] array) {
    	for (int f : array) {
    		System.out.print (f + " ");
    	}
    	System.out.println ("");
    }
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int width = 92;
		int height = 112;
		for (int i = 0; i < 100; ++i) {
			String fileName = "imgtt"+i;
			FileOutputStream fos = new FileOutputStream(PATH_TO_FILE + "test2.dat");
			FileInputStream fis = new FileInputStream(PATH_TO_FILE_2 + fileName + ".dat");
			
			FileIO fileIO = new FileIO (fis, fos);
			
			//fileIO.writeFile(test1);
			double[][] result = fileIO.readFile(width, height);
			int[][] PixelArray = fileIO.retrieveImageData(result);
			System.out.println("MAX = " + fileIO.max);
			System.out.println("MIN = " + fileIO.min);
			fileIO.close();
			
		    ///////create Image from this PixelArray
	        BufferedImage bufferImage2=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	
	        for(int y=0;y<height;y++){
	            for(int x=0;x<width;x++){
	                int Pixel=PixelArray[x][y]<<16 | PixelArray[x][y] << 8 | PixelArray[x][y];
	                bufferImage2.setRGB(x, y,Pixel);
	            }
	        }
	
	         File outputfile = new File(PATH_TO_FILE + "retrieved" + fileName + ".jpg");
	            ImageIO.write(bufferImage2, "jpg", outputfile);
	        }
	}
}
