package br.ufc.great.iot.ucontrol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import br.ufc.great.iot.ucontrol.R;
import br.ufc.great.iot.ucontrol.context.ContextManager;

public class NoConnectionActivity extends AppCompatActivity {

    private Button btn_retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        btn_retry = (Button) findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), SplashScreenActivity.class);
                startActivity(it);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContextManager.getInstance().disconnect();
    }
}
