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
public class SensorTagFragment extends Fragment{

    public static SensorTagFragment newInstance() {
        SensorTagFragment fragment = new SensorTagFragment();
        return fragment;
    }

    public SensorTagFragment() {
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
        return inflater.inflate(R.layout.fragment_sensor_tag, container, false);
    }
}
