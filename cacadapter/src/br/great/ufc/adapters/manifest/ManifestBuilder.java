package br.great.ufc.adapters.manifest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ManifestBuilder {
	
	private File manifest;
	private File manifestPath;
	private FileWriter out;
	
	private String newLine;
	
	public ManifestBuilder() {
		manifestPath = new File("META-INF/");
		manifest = new File("META-INF/MANIFEST.MF");
		
		newLine = System.getProperty("line.separator");
	}
	
	public void build(HashMap<String, String> properties){
		if(!manifestPath.exists()){
			manifestPath.mkdirs();
		}
		try {
			out = new FileWriter(manifest);
			for(String key : properties.keySet()){
				out.write(key + ": " + properties.get(key) + newLine);
			}
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
