package org.dave;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.dave.gp.Node;

public class GeneticImage {

	private Node function;
	private Image image;
	private int chosen, height, width;
	
	public GeneticImage(Node func, int h, int w) {
		function = func;
		height = h;
		width = w;
		image = createImage();
		chosen = 0;
	}
	
	public int getChosen(){
		return chosen;
	}
	
	public void isChosen(){
		chosen++;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public double evaluate(double[] arr){
		return function.evaluate(arr);
	}
	
	public Image getImage(){
		return image;
	}
	
	public Node getFunction(){
		return function;
	}
	
	public void setFunction(Node fun){
		function = fun;
	}
	
	private Image createImage() {
		int[] pixels = new int[width * height];
		
		for(int h = 0; h < height; h++)
        {
        	for(int w = 0; w < width; w++)
        	{
        		double[] dub = new double[2];
        		dub[0] = h;
        		dub[1] = w;
        		Double color = function.evaluate(dub);
        		pixels[h * width + w] = Math.abs(color.intValue() % 256);
        	}
        }
        return getImageFromArray(pixels);
	}
	
	private Image getImageFromArray(int[] pixels){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
        WritableRaster raster = image.getRaster();
        for(int h = 0; h < height; h++){
        	for(int w = 0; w < width; w++) {
        		raster.setSample(w, h, 0, pixels[h*width+w]);
        	}
        }
        return image;
	}
	
	public String toString(){
		return function.toString();
	}
}
