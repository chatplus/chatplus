package ch.hszt.mdp.chatplus.logic.concrete;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

/**
 * 
 * This is a helper-class that takes an Object
 * and serializes it to an xml-file.
 * 
 * @author pmurbach
 *
 */
public class MessageXMLSerializer {
	
	public void write(Object o, String filename){
		try{
		XMLEncoder encoder = 
			new XMLEncoder(
				new BufferedOutputStream(
						new FileOutputStream(filename)));
		encoder.writeObject(o);
		encoder.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	

	public Object read(String filename){
		try{
			XMLDecoder decoder = new XMLDecoder(
					new BufferedInputStream(
							new FileInputStream(filename)));
			Object o = decoder.readObject();
			decoder.close();
			return o;
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return null;
	}
		

}
