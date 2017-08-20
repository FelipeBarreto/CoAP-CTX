package br.great.ufc.adapters.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class JarBuilder {

	private ProcessBuilder builder;
	private Process proc;
	
	private InputStream reader;
	
	public JarBuilder() {
		File jarFolder = new File("build");
		if(!jarFolder.exists()){
			jarFolder.mkdirs();
		}
	}
	
	public void build(String jarName, String classPath){
		classPath = addPath(classPath);
		
		List<String> commands = new ArrayList<String>();
		commands.add("cacbuilder.bat");
		commands.add(jarName);
		commands.add(classPath);
		
		builder = new ProcessBuilder(commands);
		builder.redirectErrorStream(true);
		
		byte[] buffer = new byte[1024];
		try {
			proc = builder.start();
			proc.waitFor();
			reader = proc.getInputStream();
			while(reader.available() > 0){
				reader.read(buffer);
				System.out.println("CACAdapter: " + new String(buffer));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private String addPath(String classPath) {
		StringTokenizer tok = new StringTokenizer(classPath, " ");
		String pathClassPath = "";
		String libName = "";
		while(tok.hasMoreTokens()){
			libName = "../" + tok.nextToken();
			pathClassPath += libName;
		}
		
		return pathClassPath;
	}
}
