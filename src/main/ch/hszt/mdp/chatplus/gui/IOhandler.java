package ch.hszt.mdp.chatplus.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
/*
 * Author Patrik Keller
 * Klasse erzeugt ein Logfile und kann neue Strings anhängen
 */
public class IOhandler {
	FileWriter fw;
	
	public IOhandler(){
	}
	
	public void open(boolean gui){
		try {
			if (gui){
				JFrame frame = new JFrame("SaveIt");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JFileChooser dateiWahl = new JFileChooser();
				dateiWahl.showSaveDialog(frame);
				fw = new FileWriter(dateiWahl.getSelectedFile());
				BufferedWriter writer= new BufferedWriter(fw);
			} else
			{
				fw = new FileWriter(new File("C:\\temp\\logfile.txt"));
				BufferedWriter writer= new BufferedWriter(fw);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean append(String text){
		try {
			System.out.println(text+"\n");
			fw.append(text);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean close(){
		try {
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return true;
	}

}
