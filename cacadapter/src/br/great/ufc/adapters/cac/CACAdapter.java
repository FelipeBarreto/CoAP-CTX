package br.great.ufc.adapters.cac;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import br.great.ufc.adapters.jar.JarBuilder;
import br.great.ufc.adapters.manifest.ClasspathReader;
import br.great.ufc.adapters.manifest.ManifestBuilder;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.loccam.ipublisher.IPublisher;
import br.ufc.loccam.isensor.ISensor;

public abstract class CACAdapter implements ISensor{
	
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	private boolean running;
	
	/**
	 * Method called after CAC is started.
	 * @param platform The context of the application that started the CAC.
	 * @param publisher The communication interface with SySSU_Android.
	 */
	public abstract void onStart(Context platform, IPublisher publisher);
	
	/**
	 * Method called before the CAC is stopped. 
	 */
	public abstract void onStop();
	
	/**
	 * Used to dynamically load ISensor Class.
	 * @return The full className. Example: com.example.myISensorClass.
	 */
	public abstract String getClassName();
	
	/**
	 * Used to filter Android version incompatible CACs. 
	 * @return The string containing the minimal version of Android Platform. Example: 2.3.3.
	 */
	public abstract String getMinimalVersion();
	
	/**
	 * Used to identify what kind of contextual information this CAC publish.
	 * @return The string containing the context key. Example: context.device.temperature.
	 */
	public abstract String getContextKey();
	
	/**
	 * Used to identify the symbolic name of the CAC.
	 * @return The String containing the symbolic name. Example: TemperatureCAC.
	 */
	public abstract String getName();
	
	/**
	 * Used to create the dependency property of the CAC.
	 * @return The list containing the dependencies (Context Keys).
	 */
	public abstract List<String> getDependencies();
	
	/**
	 * Called by LoCCAM to start a CAC.
	 * @param platform The context of the application that started the CAC.
	 * @param publisher The communication interface with SySSU_Android.
	 */
	@Override
	public void start(Context platform, IPublisher publisher) {
		running = true;
		onStart(platform, publisher);
	}

	/**
	 * Called by LoCCAM to stop a CAC.
	 */
	@Override
	public void stop() {
		onStop();
		running = false;
	}
	
	/**
	 * Get all keys previously registered by setProperty method.
	 * @return The array of keys.
	 */
	@Override
	public String[] getPropertiesKeys() {
		return (String[]) properties.keySet().toArray();
	}

	/**
	 * Get the property identified by the specific key, if there is no associate key, then the String "Invalid Key" will be returned.
	 * @return The String containing the property.
	 */
	@Override
	public String getProperty(String key) {
		if(properties.containsKey(key)){
			return properties.get(key);
		}
		
		return "Invalid Key";
	}
	
	/**
	 * Method used by LoCCAM to know if a CAC is already running or not.
	 * @return The running state of the CAC.
	 */
	@Override
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Set the property in the format: <Key>: <Value>. These properties will be written in the Manifest.
	 * @param key The key of the property.
	 * @param value The value of the property.
	 */
	@Override
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	
	/**
	 * Implementation just to maintain older compatibility
	 */
	@Override
	public Tuple set(Tuple command){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Implementation just to maintain older compatibility
	 */
	@Override
	public Tuple get() {
		throw new UnsupportedOperationException();
	}
	
	public void addDependency(String contextKey){
		
	}
	
	/**
	 * Used to generate the CAC jar File. This method will create the Manifest file and a classes.dex, them put it into a jar file.
	 * @param args main method arguments.
	 */
	public void build() {
		checkAndPutProperty("Manifest-Version", "1.0");
		checkAndPutProperty("Created-By", "LoCCAM Adapter");
		checkAndPutProperty("Bundle-SymbolicName", getName());
		checkAndPutProperty("ISensor-Class", getClassName());
		checkAndPutProperty("Context-Provided", getContextKey());
		checkAndPutProperty("Minimal-Version", getMinimalVersion());
		checkAndPutProperty("Class-Path", new ClasspathReader().getLibsPath());
		
		String dependencies = formatDependencies(getDependencies());
		if(!dependencies.isEmpty()){
			checkAndPutProperty("Interest-Zone", dependencies);
		}
		
		new ManifestBuilder().build(properties);
		new JarBuilder().build(properties.get("Bundle-SymbolicName"), properties.get("Class-Path"));
	}
	
	private String formatDependencies(List<String> dependencies) {
		String formatedDependencies = "";
		if(dependencies == null){
			return formatedDependencies;
		}
		else{
			for(String key : dependencies){
				formatedDependencies += key;
				formatedDependencies += ",";
			}
			formatedDependencies = formatedDependencies.substring(0, formatedDependencies.length() - 1);
			return formatedDependencies;
		}
	}

	private void checkAndPutProperty(String key, String value){
		if(properties.containsKey(key)){
			return;
		}
		properties.put(key, value);
	}
}
