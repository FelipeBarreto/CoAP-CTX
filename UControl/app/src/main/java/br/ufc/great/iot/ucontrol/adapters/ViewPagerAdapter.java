package br.ufc.great.iot.ucontrol.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.ufc.great.iot.ucontrol.fragments.HueLampFragment;
import br.ufc.great.iot.ucontrol.fragments.SensorTagFragment;
import br.ufc.great.iot.ucontrol.fragments.SmartWatchFragment;

/**
 * Created by Felipe on 21/06/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence mTitles[];
    private TabLayout tabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], TabLayout tabs){
        super(fm);
        this.mTitles = mTitles;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return HueLampFragment.newInstance();
        }
        else if(position == 1){
            return SensorTagFragment.newInstance();
        }
        else{
            return SmartWatchFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
