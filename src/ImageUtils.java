import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Sifan Ye (sye8)
//Helper Class for reading images

public class ImageUtils {
	
	public static Image loadImage(String filename){
		try{
			return ImageIO.read(new File(filename));
		}catch (IOException e) {
			System.out.println("Image Not Found");
			return null;
		}
	}
	
	
	
}
