package com.sp.mad_studypal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile_Fragment extends Fragment {
    private ImageView setting_button;
    private Button switch_reservation;
    private Button switch_saved;

    public Profile_Fragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);

        setting_button = view.findViewById(R.id.id_setting_button);
        setting_button.setOnClickListener(settings_button);

        switch_reservation = view.findViewById(R.id.id_switch_reservation);
        switch_reservation.setOnClickListener(switch_to_reservation);

        switch_saved = view.findViewById(R.id.id_switch_saved);
        switch_saved.setOnClickListener(switch_to_saved);
        return view;
    }

    private View.OnClickListener settings_button = new View.OnClickListener() {    //Setting buttons, to go account settings
        @Override
        public void onClick(View v) {
            Fragment accountsetting_fragment = new AccountSetting_Fragment();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_View,accountsetting_fragment).commit();
        }
    };

    private View.OnClickListener switch_to_reservation = new View.OnClickListener() {    //Reservation button, switch to reservation
        @Override
        public void onClick(View v) {
            Fragment reservation_fragment = new Reservation_Fragment();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_View,reservation_fragment).commit();
        }
    };

    private View.OnClickListener switch_to_saved = new View.OnClickListener() {    //Saved button, switch to saved
        @Override
        public void onClick(View v) {
            Fragment saved_fragment = new Saved_Fragment();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_View,saved_fragment).commit();
        }
    };



}