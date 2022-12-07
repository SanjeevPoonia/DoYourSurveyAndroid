package com.qdegrees.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class Splash_Screen_Activity extends AppCompatActivity {
    Activity mActivity;
    Handler handler;
    CardView cvChoose;
    LinearLayout linChoose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mActivity=this;
        cvChoose=findViewById(R.id.cv_welcome_ChooseCategory);
        linChoose=findViewById(R.id.lin_choose_layout);

        linChoose.setVisibility(View.GONE);

     /*    if(SharedPreferencesRepository.getDataManagerInstance().isLoggedIn()){
             linChoose.setVisibility(View.GONE);
             HandlerMethod();
            }else{
             linChoose.setVisibility(View.VISIBLE);
            }

         cvChoose.setOnClickListener(v -> {
             Intent intent= new Intent(mActivity,ChooseYourCategory_Act.class);
             startActivity(intent);
             finish();
         });*/
        HandlerMethod();


    }
    private void HandlerMethod(){
        handler = new Handler();
        handler.postDelayed(() -> {

            if(SharedPreferencesRepository.getDataManagerInstance().isLoggedIn()){
                Intent intent = new Intent(mActivity, HomeAct.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(mActivity,Authenticator_Activity.class);
                startActivity(intent);
                finish();
            }



         /*   Intent intent = new Intent(mActivity, HomeAct.class);
            startActivity(intent);
            finish();*/

          /*  Intent intent = new Intent(mActivity, MainHomeActivity.class);
            startActivity(intent);
            finish();*/
            /*if(SharedPreferencesRepository.getDataManagerInstance().isLoggedIn()){
                Intent intent = new Intent(mActivity, MainHomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(mActivity,Authenticator_Activity.class);
                startActivity(intent);
                finish();
            }*/
        }, 3000);
    }

}
