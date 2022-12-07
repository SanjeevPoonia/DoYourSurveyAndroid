package com.qdegrees.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.qdegrees.doyoursurvey.R;

public class ChooseYourCategory_Act extends AppCompatActivity {
    ImageView ivCatSelected;
    LinearLayout linPartiEarn,linCustomer;
    Boolean catSelected=false;
    CardView cvLogin;
    LinearLayout linSignup;
    TextView tvSignup;
    Activity mActivity;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_choose_category);
        mActivity=this;
        ivCatSelected= findViewById(R.id.iv_choose_selected);
        linPartiEarn= findViewById(R.id.lin_choose_partiToEarn);
        linCustomer= findViewById(R.id.lin_choose_customer);
        cvLogin= findViewById(R.id.cv_choose_login);
        linSignup= findViewById(R.id.lin_choose_signup);
        tvSignup= findViewById(R.id.tv_choose_signup);
        linPartiEarn.setOnClickListener(v->{
            if(catSelected){
                ivCatSelected.setVisibility(View.GONE);
                catSelected=false;
                cvLogin.setBackgroundColor(getResources().getColor(R.color.disable_btn));
                linSignup.setBackgroundDrawable(getResources().getDrawable(R.drawable.choose_selector_back));
                tvSignup.setTextColor(getResources().getColor(R.color.disable_btn));
            }else{
                ivCatSelected.setVisibility(View.VISIBLE);
                catSelected=true;
                cvLogin.setBackgroundColor(getResources().getColor(R.color.pro_color));
                linSignup.setBackgroundDrawable(getResources().getDrawable(R.drawable.choose_selector_enable));
                tvSignup.setTextColor(getResources().getColor(R.color.pro_color));
            }
        });

        linCustomer.setOnClickListener(v->{
            Intent intent= new Intent(mActivity,GetInTouch_Act.class);
            startActivity(intent);
        });

        cvLogin.setOnClickListener(v->{
            Intent intent = new Intent(mActivity,Authenticator_Activity.class);
            startActivity(intent);
            finish();
        });

    }
}
