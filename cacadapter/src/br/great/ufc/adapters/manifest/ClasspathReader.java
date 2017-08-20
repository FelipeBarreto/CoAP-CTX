package br.great.ufc.adapters.manifest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ClasspathReader {
	
	private BufferedReader reader;
	
	private String[] loccamLibs = {"android.jar", "loccam.jar"};
	
	public ClasspathReader() {
		try {
			reader = new BufferedReader(new FileReader(".classpath"));
		} catch (FileNotFoundException e) {
			System.out.println("Classpath file not Found");
		}
	}
	
	public String getLibsPath(){
		String libsPath = "";
		if(reader != null){
			try {
				while(reader.ready()){
					String line = reader.readLine();
					if(line.contains("kind=\"lib\"")){
						if(!presentInLoCCAM(line)){
							String path = line.substring(line.indexOf("path=") + 6, line.length() - 3);
							if(!libsPath.isEmpty()){
								libsPath += " ";
							}
							libsPath += path;
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return libsPath;
	}

	private boolean presentInLoCCAM(String line) {
		for(String lib : loccamLibs){
			if(line.contains(lib)){
				return true;
			}
		}
		return false;
	}

}
