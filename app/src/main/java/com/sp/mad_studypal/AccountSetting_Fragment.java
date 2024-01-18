package com.sp.mad_studypal;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class AccountSetting_Fragment extends Fragment {
    private ImageView back_button;
    private Button edit_profile;
    private Button about;
    private Button logout;
    public AccountSetting_Fragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accountsetting, container, false);

        back_button = view.findViewById(R.id.back_to_profile);
        back_button.setOnClickListener(switch_to_profile);

        edit_profile = view.findViewById(R.id.id_switch_edit_profile);
        edit_profile.setOnClickListener(switch_to_editprofile);

        about = view.findViewById(R.id.id_about);
        about.setOnClickListener(switch_to_about);

        logout = view.findViewById(R.id.id_logout);
        logout.setOnClickListener(switch_to_start);

        return view;
    }

    private View.OnClickListener switch_to_profile = new View.OnClickListener() {   //Back Button, go back to profile page
        @Override
        public void onClick(View v) {
            Fragment profile_fragment = new Profile_Fragment();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_View,profile_fragment).commit();
        }
    };

    private View.OnClickListener switch_to_editprofile = new View.OnClickListener() {   //Edit Profile, go back to edit profile
        @Override
        public void onClick(View v) {
            Fragment editprofile_fragment = new EditProfile_Fragment();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_View,editprofile_fragment).commit();
        }
    };

    private View.OnClickListener switch_to_about = new View.OnClickListener() {   //About Button, go to about page
        @Override
        public void onClick(View v) {
            Fragment about_fragment = new About_Fragment();
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.fragment_View,about_fragment).commit();
        }
    };

    private View.OnClickListener switch_to_start = new View.OnClickListener() {   //About Button, go to about page
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), StartPage_Activity.class));
        }
    };



}