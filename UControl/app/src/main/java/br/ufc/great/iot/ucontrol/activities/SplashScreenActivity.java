package br.ufc.great.iot.ucontrol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import br.ufc.great.iot.ucontrol.R;
import br.ufc.great.iot.ucontrol.context.ContextListener;
import br.ufc.great.iot.ucontrol.context.ContextManager;


public class SplashScreenActivity extends AppCompatActivity implements ContextListener{

    private ImageView img_splash;

    private boolean isToContinue = true;

    private long initialTimestamp;

    private static final int MINIMAL_SPLASH_TIME = 2000;
    private static final int MAX_SPLASH_TIME = 7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img_splash = (ImageView) findViewById(R.id.img_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                skipToErrorActivity();
            }
        }, MAX_SPLASH_TIME);

        ContextManager.getInstance().registerListener(this);
        ContextManager.getInstance().connect(getApplicationContext(), getResources().getString(R.string.app_name));
        initialTimestamp = System.currentTimeMillis();
    }

    private void skipSplashScreen() {
        if(isToContinue) {
            long finalTimestamp = System.currentTimeMillis();
            long timeElapsed = finalTimestamp - initialTimestamp;
            if(timeElapsed < MINIMAL_SPLASH_TIME){
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(isToContinue){
                            skipNow();
                        }
                    }
                }, MINIMAL_SPLASH_TIME - timeElapsed);
            }
            else{
                skipNow();
            }

        }
    }

    private void skipToErrorActivity() {
        if(isToContinue) {
            isToContinue = false;
            Intent it = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(it);
        }
    }

    private void skipNow(){
        isToContinue = false;
        Intent it = new Intent(getApplicationContext(), SensorsActivity.class);
        startActivity(it);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isToContinue = false;
        ContextManager.getInstance().disconnect();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            img_splash.setPivotX(img_splash.getWidth() / 2);
            img_splash.setPivotY(img_splash.getHeight() / 2);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(isToContinue){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img_splash.setRotation(((int) img_splash.getRotation() + 15)%360);
                            }
                        });

                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void onContextReady() {
        ContextManager.getInstance().unregisterListener(this);
        skipSplashScreen();
    }
}
