package br.ufc.great.iot.coapclienttest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.ws4d.coap.Constants;
import org.ws4d.coap.connection.BasicCoapChannelManager;
import org.ws4d.coap.interfaces.CoapChannelManager;
import org.ws4d.coap.interfaces.CoapClient;
import org.ws4d.coap.interfaces.CoapClientChannel;
import org.ws4d.coap.interfaces.CoapRequest;
import org.ws4d.coap.interfaces.CoapResponse;
import org.ws4d.coap.messages.CoapRequestCode;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Felipe on 25/09/2016.
 */
public class SimpleCoapClient implements CoapClient{

    private static final String SERVER_ADDRESS = "192.168.25.69";
    private static final int PORT = Constants.COAP_DEFAULT_PORT;

    private static SimpleCoapClient instance;

    private Activity context;

    CoapChannelManager channelManager = null;
    CoapClientChannel clientChannel = null;

    private SimpleCoapClient(){
        // Just making it private.
    }

    public static SimpleCoapClient getInstance(){
        if(instance == null){
            instance = new SimpleCoapClient();
        }
        return instance;
    }

    @Override
    public void onResponse(CoapClientChannel channel, final CoapResponse response) {
        Log.d("CoAP Response", response + " // Payload = " + new String(response.getPayload()));
//        context.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(context, "Resposta: " + response, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Conexão não estabelicida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void connect(Activity context){
        this.context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                channelManager = BasicCoapChannelManager.getInstance();
                try {
                    clientChannel = channelManager.connect(SimpleCoapClient.this, InetAddress.getByName(SERVER_ADDRESS), PORT);
                    doDefaultPost();
                } catch(UnknownHostException ex){
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void doDefaultPost(){
        CoapRequest coapRequest = clientChannel.createRequest(true, CoapRequestCode.POST);
        coapRequest.setUriPath("/smartphone/lumi");
        clientChannel.sendMessage(coapRequest);
    }

    public void updateLuminosity(float lum){
        if(clientChannel == null){
            return;
        }

        CoapRequest coapRequest = clientChannel.createRequest(true, CoapRequestCode.PUT);
        coapRequest.setUriPath("/smartphone/lumi");
        coapRequest.setPayload(lum + "");
        clientChannel.sendMessage(coapRequest);
    }
}
