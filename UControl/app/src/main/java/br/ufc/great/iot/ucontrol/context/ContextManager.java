package br.ufc.great.iot.ucontrol.context;

import android.content.Context;
import android.os.RemoteException;

import java.util.ArrayList;

import br.ufc.great.loccamlib.LoccamListener;
import br.ufc.great.loccamlib.LoccamManager;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IClientReaction;
import br.ufc.great.syssu.base.interfaces.ISysSUService;

/**
 * Created by Felipe on 20/06/2015.
 */
public class ContextManager implements LoccamListener{

    private static ContextManager instance;

    @Override
    public void onServiceConnected(ISysSUService iSysSUService) {
        loccamManager.subscribe(new IClientReaction.Stub() {

            @Override
            public void react(Tuple tuple) throws RemoteException {
                notifyListener(ContextEvents.CONTEXT_READY);
            }
        }, "context.device.dependency1");

        loccamManager.putInterest("context.device.dependency1");
    }

    @Override
    public void onServiceDisconnected() {

    }

    @Override
    public void onLoccamException(Exception e) {

    }

    private enum ContextEvents {CONTEXT_READY};

    private ArrayList<ContextListener> listeners;

    private LoccamManager loccamManager;

    private ContextManager(){
        listeners = new ArrayList<>();
    }

    public static ContextManager getInstance(){
        if(instance == null){
            instance = new ContextManager();
        }
        return instance;
    }

    public void connect(Context context, String appId){
        loccamManager = new LoccamManager(context, appId);
        loccamManager.start(this);
    }

    public void disconnect(){
        if(loccamManager != null){
            loccamManager.stop();
        }
    }

    public void registerListener(ContextListener listener){
        listeners.add(listener);
    }

    public void unregisterListener(ContextListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
        }
    }

    private void notifyListener(ContextEvents event){
        if(event == ContextEvents.CONTEXT_READY){
            for(ContextListener listener : new ArrayList<>(listeners)){
                listener.onContextReady();
            }
        }
    }
}
