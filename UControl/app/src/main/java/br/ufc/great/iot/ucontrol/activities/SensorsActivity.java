package br.ufc.great.iot.ucontrol.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.ufc.great.iot.ucontrol.R;
import br.ufc.great.iot.ucontrol.adapters.ViewPagerAdapter;
import br.ufc.great.iot.ucontrol.context.ContextManager;

public class SensorsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;

    private ViewPagerAdapter viewPageAdapter;

    private final static CharSequence tabsTitles[] = {"Hue Lamps", "Sensor Tag", "Watch"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.hideOverflowMenu();
        toolbar.setTitle(R.string.title_sensors_actuators);

        setSupportActionBar(toolbar);

        tabs = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPageAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabsTitles, tabs);
        viewPager.setAdapter(viewPageAdapter);

        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContextManager.getInstance().disconnect();
    }
}
