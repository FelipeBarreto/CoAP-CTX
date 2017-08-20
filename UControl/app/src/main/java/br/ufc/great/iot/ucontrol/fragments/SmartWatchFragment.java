package br.ufc.great.iot.ucontrol.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.ufc.great.iot.ucontrol.R;

/**
 * Created by Felipe on 21/06/2015.
 */
public class SmartWatchFragment extends Fragment{

    public static SmartWatchFragment newInstance() {
        SmartWatchFragment fragment = new SmartWatchFragment();
        return fragment;
    }

    public SmartWatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_smart_watch, container, false);
    }
}
