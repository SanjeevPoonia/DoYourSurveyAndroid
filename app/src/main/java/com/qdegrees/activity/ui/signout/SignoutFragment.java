package com.qdegrees.activity.ui.signout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.qdegrees.activity.Authenticator_Activity;
import com.qdegrees.activity.MainHomeActivity;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;

public class SignoutFragment extends Fragment {
    Activity mActivity;
    TextView YeSText,NoText,textCo;
    String NameStr="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signout, container, false);
        mActivity=getActivity();
        NameStr=SharedPreferencesRepository.getDataManagerInstance().getSessionname()+" "+ SharedPreferencesRepository.getDataManagerInstance().getSessionLastname();

        YeSText=root.findViewById(R.id.tvFragmentSignOut_Yes);
        textCo=root.findViewById(R.id.Fragment_Signout_Text);

        textCo.setText("Hi "+NameStr+", You are signing out of your DoYourSurvey app on this device");

        YeSText.setText("SIGN OUT");


        YeSText.setOnClickListener(v->{
            boolean isDemo=SharedPreferencesRepository.getDataManagerInstance().IsDashboardDemoRequired();
            boolean isRewardDemo=SharedPreferencesRepository.getDataManagerInstance().IsRewardDemoRequired();
            SharedPreferencesRepository.clearLoginPref(isDemo,isRewardDemo);
            Intent intent= new Intent(mActivity, Authenticator_Activity.class);
            startActivity(intent);
            mActivity.finish();
        });
        NoText=root.findViewById(R.id.tvFragmentSignOut_No);
        NoText.setText("CANCEL");
        NoText.setOnClickListener(v->{
          Intent intent= new Intent(mActivity,MainHomeActivity.class);
          startActivity(intent);
          mActivity.finish();
        });


        return root;
    }
}
