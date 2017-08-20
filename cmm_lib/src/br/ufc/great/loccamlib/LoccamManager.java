package br.ufc.great.loccamlib;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IClientReaction;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.ISysSUService;

public class LoccamManager{
	private ISysSUService service;
	private MyServiceConnection connection;
	private ArrayList<String> interests = new ArrayList<String>();
	private ArrayList<String> reactionIds = new ArrayList<String>();
	private String appId;
	private Context context;
	private LoccamListener listener;
	private String id;
	
	private static final String SERVICE_NAME = "br.ufc.great.loccam.service.SysSUService";
	
	public LoccamManager(Context context, String id) {		
		this.context = context;
		this.appId = "Unknown";
		this.id = id;
	}
	
	public LoccamManager(Context context, String appId, String id) {
		this.context = context;
		this.appId = appId;
		this.id = id;
	}
	
	public void start(LoccamListener listener){		
		this.listener = listener;
		initService();
	}
	
	public void downloadCAC(List<String> list) {
		if(!checkServiceState()) 
			System.out.println("putCAC - LoCCAM n�o iniciado");
		else
			try {
				service.putCAC(list);
			} catch (RemoteException e) {
				listener.onLoccamException(e);
			}
	}
	
	public void putInterest(String key) {
		if(!checkServiceState())
			System.out.println("putInterest - LoCCAM n�o iniciado");
		else { 
			try {
				Tuple tupla = (Tuple) new Tuple()
				.addField("AppId", appId)
				.addField("InterestElement", key);
				service.put(tupla);
				interests.add(key);
			} catch (RemoteException e) {
				listener.onLoccamException(e);
			}	
		}
	}
	
	public void takeInterest(String key){
		if(!checkServiceState())
			System.out.println("takeInterest - LoCCAM n�o iniciado");
		else {
			try {
				Pattern pattern1 = (Pattern)new Pattern().addField("AppId", appId)
						.addField("InterestElement", key);
				service.take(pattern1, null);
				interests.remove(key);
			} catch (RemoteException e) {
				listener.onLoccamException(e);
			}
		}
	}
	
	public void takeAllInterests() {
		if(!checkServiceState())
			System.out.println("takeInterest - LoCCAM n�o iniciado");
		else {
			try {
				for(int i = 0; i < interests.size(); i++) {
					Pattern pattern1 = (Pattern) new Pattern().addField("AppId", appId)
							.addField("InterestElement", interests.get(i));
					service.take(pattern1, null);
				}
				interests.clear();
			} catch (RemoteException e) {
				listener.onLoccamException(e);
			}
		}
	}
	
	public ArrayList<String> getInterests() {
		return interests;
	}
	
	public Tuple read(String key) {
		if(!checkServiceState()) {
			System.out.println("read - LoCCAM n�o iniciado");
			return null;
		}
		else {
			try {
				Pattern pattern1 = (Pattern) new Pattern().addField("ContextKey", key);
				List<Tuple> lista = null;
				lista = service.read(pattern1, null);
				if(!lista.isEmpty())
					return lista.get(0);
				else
					return new Tuple();
			} catch (RemoteException e) {
				listener.onLoccamException(e);
				return new Tuple();
			}
		} 
	}
	
	public ArrayList<Tuple> readAll() {
		if(!checkServiceState()) {
			System.out.println("readAll - LoCCAM n�o iniciado");
			return null;
		}
		else {	
			try {
				ArrayList<Tuple> tuplas = new ArrayList<Tuple>();
			
				for(int i = 0; i < interests.size(); i++) {
					Pattern pattern1 = (Pattern) new Pattern().addField("ContextKey", interests.get(i));
					List<Tuple> lista = null;	
					lista = service.read(pattern1, null);
					if(!lista.isEmpty())
						tuplas.add(lista.get(0));
					else
						tuplas.add(new Tuple());
				}
				return tuplas;
			}catch (RemoteException e) {
				listener.onLoccamException(e);
				return new ArrayList<Tuple>();
			}
		}
	}
	
	public String subscribe(IClientReaction reaction, String event, String key, IFilter filter) {
		if(!checkServiceState()) {
			System.out.println("subscribe - LoCCAM n�o iniciado");
			return null;
		}
		else {
			try {
				String reactionId = "";
				
				Pattern pattern = new Pattern();
				pattern.addField("ContextKey", key);
				reactionId = service.subscribe(reaction, event, pattern, filter);
				
				if(!reactionId.equals("")) {
					reactionIds.add(reactionId);
				}
				return reactionId;
				
			} catch (RemoteException e) {
				listener.onLoccamException(e);
				return "";
			}
		}
	}
	
	public String subscribe(IClientReaction reaction, String key) {
		return subscribe(reaction, "put", key, null);
	}
	
	public void unSubscribe(String reactionId) {
		if(!checkServiceState())
			System.out.println("unSubscribe - LoCCAM n�o iniciado");
		else {
			try {
				service.unSubscribe(reactionId);
				reactionIds.remove(reactionId);
			} catch (RemoteException e) {
				listener.onLoccamException(e);
			}
			
		}
	}
	
	public void unSubscribeAll() {
		if(!checkServiceState())
			System.out.println("unSubscribeAll - LoCCAM n�o iniciado");
		else {
			try {
				ArrayList<String> reactionIdsCache = new ArrayList<String>(reactionIds);
				for(String reactionId : reactionIdsCache) {
					service.unSubscribe(reactionId);
					reactionIds.remove(reactionId);	
				}
			} catch (RemoteException e) {
				listener.onLoccamException(e);
			}
		}
	}
	
	public void putSysSUMessage(String key, String message){
		if(!checkServiceState())
			System.out.println("sendMessage - LoCCAM n�o iniciado");
		else{
			try {
				Tuple tuple = (Tuple) new Tuple().addField("ContextKey", key)
						 						 .addField("AppMessage", message);
				service.put(tuple);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		if(service != null) {
			takeAllInterests();
			unSubscribeAll();
			releaseService();
		}
	}
		
	private boolean checkServiceState() {
	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (SERVICE_NAME.equals(service.service.getClassName())) {
	            return true;
	        }
	    }
		return false;
	}
	
	private void initService() {
		connection = new MyServiceConnection();
		Intent i = new Intent();
		i.setClassName("br.ufc.great.loccam", "br.ufc.great.loccam.service.SysSUService");
		//context.startService(i);
		boolean ret = context.bindService(i, connection, Context.BIND_AUTO_CREATE);
		Log.d(appId, "initService() bound with " + ret);
	}
	
	private void releaseService() {
		try {
			service.removeApp(id);
		} catch (RemoteException e) {
			listener.onLoccamException(e);
		}

		context.unbindService(connection);
		listener.onServiceDisconnected();
		connection = null;
		Log.d(appId, "releaseService() unbound.");
		service = null; 
	}	
 	
	class MyServiceConnection implements ServiceConnection {

		public void onServiceConnected(ComponentName name, IBinder boundService) {
			service = ISysSUService.Stub.asInterface(boundService);
			try {
				service.putApp(id);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	listener.onServiceConnected(service);
		}
		public void onServiceDisconnected(ComponentName name) {
			service = null; 
			listener.onServiceDisconnected();
		}
	}
}