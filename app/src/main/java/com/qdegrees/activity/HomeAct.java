package com.qdegrees.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qdegrees.activity.ui.dashboard.DashboardFragment;
import com.qdegrees.activity.ui.panelprofile.PanelProfile_Frag_Btm;
import com.qdegrees.activity.ui.reedem.ReedemFragment;
import com.qdegrees.activity.ui.referearn.ReferNEarn_Frag;
import com.qdegrees.activity.ui.rewards.RewardsFragment;
import com.qdegrees.activity.ui.signout.SignoutFragment;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.utils.CurvedBottomNavigationView;
import com.qdegrees.utils.Utility;

public class HomeAct extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    CurvedBottomNavigationView bottomNavigationView;
    AppCompatTextView tvTitle;
    Activity mActivity;
    LinearLayout logoutLayout, RedeemLayout;
    ImageView ivRedeem;
    TextView tvRedeem;


    String ReferCode="";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        mActivity=this;

        ReferCode= SharedPreferencesRepository.getDataManagerInstance().getSessionReferCode();
        tvTitle=findViewById(R.id.tvHomeTitle);
        bottomNavigationView=findViewById(R.id.customBottomBar);
        logoutLayout=findViewById(R.id.LogoutLL);
        RedeemLayout= findViewById(R.id.LL_Home_Redeem);
        ivRedeem = findViewById(R.id.ivAdd);
        tvRedeem =  findViewById(R.id.tvInviteReqCount);
        bottomNavigationView.inflateMenu(R.menu.bottom_menu);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Fragment fragment= new DashboardFragment();
        setCurrentFragment(fragment);
        tvTitle.setText(mActivity.getResources().getString(R.string.menu_dashboard_btm));

        logoutLayout.setOnClickListener(v->{
            tvTitle.setText(mActivity.getResources().getString(R.string.menu_SignOut));
            Fragment fragment1= new SignoutFragment();
            setCurrentFragment(fragment1);
        });

        RedeemLayout.setOnClickListener(v->{
            ivRedeem.setColorFilter(ContextCompat.getColor(mActivity, R.color.pro_color), android.graphics.PorterDuff.Mode.SRC_IN);
            tvRedeem.setTextColor(ContextCompat.getColor(mActivity,R.color.pro_color));

            Fragment fragmentew = new ReedemFragment();
            setCurrentFragment(fragmentew);
            tvTitle.setText(mActivity.getResources().getString(R.string.Redeem_reward_points));
            bottomNavigationView.setSelectedItemId(R.id.action_jugaad);
        });
    }


    private void setCurrentFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();

        if (itemId == R.id.action_home) {
            ivRedeem.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            tvRedeem.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimary));
            Fragment fragment= new DashboardFragment();
            setCurrentFragment(fragment);
            tvTitle.setText(mActivity.getResources().getString(R.string.menu_dashboard_btm));
        }else if (itemId == R.id.action_profile) {
            ivRedeem.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            tvRedeem.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimary));
            Fragment fragment= new PanelProfile_Frag_Btm();
            setCurrentFragment(fragment);
            tvTitle.setText(mActivity.getResources().getString(R.string.menu_panel_profile_btm));
        }else if (itemId == R.id.action_jugaad) {
            ivRedeem.setColorFilter(ContextCompat.getColor(mActivity, R.color.pro_color), android.graphics.PorterDuff.Mode.SRC_IN);
            tvRedeem.setTextColor(ContextCompat.getColor(mActivity,R.color.pro_color));
            Fragment fragmentew = new ReedemFragment();
            setCurrentFragment(fragmentew);
            tvTitle.setText(mActivity.getResources().getString(R.string.Redeem_reward_points));
        }else if (itemId == R.id.action_rewards) {
            ivRedeem.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            tvRedeem.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimary));
            Fragment fragment= new RewardsFragment();
            setCurrentFragment(fragment);
            tvTitle.setText(mActivity.getResources().getString(R.string.Rewards_History));
        }else if (itemId == R.id.action_refer) {
            ivRedeem.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            tvRedeem.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimary));
    /*            Fragment fragment= new ReferNEarn_Frag();
            setCurrentFragment(fragment);*/
            Utility.referNewDialog(mActivity,ReferCode);
            tvTitle.setText(mActivity.getResources().getString(R.string.menu_refer_btm));

        }
        return true;
    }
}
