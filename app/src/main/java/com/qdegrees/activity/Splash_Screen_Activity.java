package com.qdegrees.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    TextView PoweredByText;
    public static int MY_REQUEST_CODE=12;
    AppUpdateManager appUpdateManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mActivity=this;
        PoweredByText=findViewById(R.id.tv_splash_poweredby);
        PoweredByText.setText(Html.fromHtml(mActivity.getResources().getString(R.string.PoweredBy)));
        PoweredByText.setMovementMethod(LinkMovementMethod.getInstance());
        /*appUpdateManager = AppUpdateManagerFactory.create(mActivity);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {

                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE, mActivity, MY_REQUEST_CODE);
                }catch (IntentSender.SendIntentException e){
                    e.printStackTrace();
                    HandlerMethod();
                }
                // Request the update.
            }else{
                HandlerMethod();
            }
        });

*/
        HandlerMethod();
    }


    private void HandlerMethod(){
        handler = new Handler();
        handler.postDelayed(() -> {
            if(SharedPreferencesRepository.getDataManagerInstance().isLoggedIn()){
                Intent intent = new Intent(mActivity, MainHomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(mActivity,Authenticator_Activity.class);
                startActivity(intent);
                finish();
            }


        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                HandlerMethod();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {

                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                try {
                                    // If an in-app update is already running, resume the update.
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            mActivity,
                                            MY_REQUEST_CODE);
                                }catch (IntentSender.SendIntentException e){
                                    e.printStackTrace();
                                }
                            }
                        });*/
    }
}
