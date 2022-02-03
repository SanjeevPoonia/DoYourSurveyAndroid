package com.qdegrees.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.qdegrees.activity.ui.blogs.BlogFragment;
import com.qdegrees.activity.ui.dashboard.DashboardFragment;
import com.qdegrees.activity.ui.helpcenter.HelpCenterfragment;
import com.qdegrees.activity.ui.panelprofile.PanelProfileFragment;
import com.qdegrees.activity.ui.rewards.RewardsFragment;
import com.qdegrees.activity.ui.settings.SettingsFragment;
import com.qdegrees.activity.ui.signout.SignoutFragment;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class MainHomeActivity extends AppCompatActivity {


    String sUserId,sUserEmail,sRememberToken,sUserFirstName,sUserLastName;

    private AppBarConfiguration mAppBarConfiguration;
    Activity mActivity;
    int CurrentPosition=0;
    public static NavigationView navigationView;

    /***************App Update**********************/

    private AppUpdateManager appUpdateManager;
    private final int IMMEDIATE_APP_UPDATE_REQ_CODE=12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActivity=this;

        /*******App Update***************/
        appUpdateManager= AppUpdateManagerFactory.create(mActivity);
        checkUpdate();

        /*******************************/

        sUserId= SharedPreferencesRepository.getDataManagerInstance().getSessionUserId();
        sUserEmail= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        sRememberToken= SharedPreferencesRepository.getDataManagerInstance().getSessionRememberToken();
        sUserFirstName= SharedPreferencesRepository.getDataManagerInstance().getSessionname();
        sUserLastName= SharedPreferencesRepository.getDataManagerInstance().getSessionLastname();




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View headerLayout = navigationView.getHeaderView(0);

        TextView uNameTextView=headerLayout.findViewById(R.id.TitleNameTextView);
        uNameTextView.setText(sUserFirstName+ " "+sUserLastName);
        TextView uEmailTextView=headerLayout.findViewById(R.id.EmailtextView);
        uEmailTextView.setText(sUserEmail);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_slideshow,R.id.nav_blogs,R.id.nav_Help,R.id.nav_settings,R.id.nav_signout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMMEDIATE_APP_UPDATE_REQ_CODE){
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! ", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Update success!" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed!", Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }


    /*
    Check for update
     */
    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                startUpdateFlow(appUpdateInfo);
            }
        });
    }
    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, mActivity, IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
}