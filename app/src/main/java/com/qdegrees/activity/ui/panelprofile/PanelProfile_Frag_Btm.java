package com.qdegrees.activity.ui.panelprofile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdegrees.activity.ui.dashboard.DashboardFragment;
import com.qdegrees.activity.ui.survey.ProfileSurvey_AttemptActivity;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.ChangePassword_Request;
import com.qdegrees.network.request.DashboardPostData;
import com.qdegrees.network.response.DashboardProfileResponse;
import com.qdegrees.network.response.ReedemPointResponse;
import com.qdegrees.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanelProfile_Frag_Btm extends Fragment {
    Activity mActivity;
    protected ApiService apiService;
    String sUserEmail="",sUserName="",sUserPhone="",sUserCity="",sUserBio="";
    int ProfilePercentage=0;
    ProgressBar progressBar;
    TextView tvPercentage;
    List<ProfileSurveyListItem> ProfileSurveyList= new ArrayList<>();
    Dialog profileDialog;
    CardView cvCompleteProfile,cvEditProfile;
    AppCompatTextView tvName,tvLocation,tvBio,tvEmail,tvPhone;
    ImageView ivEditPassword;
    public static final int REQUEST_CODE=17;
    public static final int RESULT_CODE=18;
    String passwordError="Password must be at least 8 characters and must contain at least one lower case letter, one upper case letter and one digit";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_panelprofile_btm, container, false);
        mActivity=getActivity();
        sUserEmail= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        sUserName=SharedPreferencesRepository.getDataManagerInstance().getSessionname()+" "+SharedPreferencesRepository.getDataManagerInstance().getSessionLastname();
        sUserPhone=SharedPreferencesRepository.getSessionMobile();
        sUserCity=SharedPreferencesRepository.getDataManagerInstance().getSessionCity();
        sUserBio = SharedPreferencesRepository.getDataManagerInstance().getSessionBio();

        apiService = RetrofitAPIClient.getRetrofitClient();
        tvName=root.findViewById(R.id.tvFrag_PanelName);
        tvLocation=root.findViewById(R.id.tv_FragPanel_Location);
        cvCompleteProfile=root.findViewById(R.id.cv_Frag_Panel_CompleteProfile);
        progressBar=root.findViewById(R.id.pb_FragPanel_Profile);
        tvPercentage=root.findViewById(R.id.tvFragPanel_Percentage);
        tvBio=root.findViewById(R.id.tv_FragPanel_Bio);
        tvEmail=root.findViewById(R.id.tv_Frag_Panel_Email);
        tvPhone=root.findViewById(R.id.tv_Frag_Panel_Phone);
        cvEditProfile=root.findViewById(R.id.cv_Frag_Panel_EditProfile);

        ivEditPassword=root.findViewById(R.id.iv_Frag_Panel_EditPass);
        tvLocation.setText(sUserCity);

        tvName.setText(sUserName);
        tvEmail.setText(sUserEmail);
        tvPhone.setText("+91-"+sUserPhone);

        tvBio.setText(sUserBio);

        cvCompleteProfile.setOnClickListener(v->{
            Log.e("profileSurvey",ProfileSurveyList.size()+"");
            if(ProfileSurveyList.size()>0){
                createProfileDialog();
            }
        });
        ivEditPassword.setOnClickListener(v->{
            changePasswordDialog();
        });

        cvEditProfile.setOnClickListener(v->{
            Intent intent = new Intent (mActivity, EditProfileAct.class);
            startActivity(intent);
        });

        getProfileDashboard();





        return root;
    }
    private void createProfileDialog(){
        profileDialog= new Dialog(mActivity);
        profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profileDialog.setContentView(R.layout.dialog_profilesurvey);
        ImageView closeImg=profileDialog.findViewById(R.id.ivCloseProfileDialog);
        RecyclerView profileRecyclerView=profileDialog.findViewById(R.id.rvDialogProfile);
        LinearLayoutManager llm= new LinearLayoutManager(mActivity);
        profileRecyclerView.setLayoutManager(llm);

        ProfileListAdapter profileListAdapter= new ProfileListAdapter(mActivity,ProfileSurveyList);
        profileRecyclerView.setAdapter(profileListAdapter);
        profileListAdapter.notifyDataSetChanged();

        closeImg.setOnClickListener(v->{
            profileDialog.dismiss();
        });
        profileDialog.show();
        ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);
        profileDialog.getWindow().setBackgroundDrawable(drawable);
        profileDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);




    }
    class ProfileListHolder extends RecyclerView.ViewHolder{

        CardView mainView;
        TextView SurveyNameText,StartSurvey;
        public ProfileListHolder(View view){
            super(view);
            mainView=view.findViewById(R.id.cv_fragment_profile_listitem);
            SurveyNameText=view.findViewById(R.id.tv_fragmentProfileListItem_SurveyName);
            StartSurvey=view.findViewById(R.id.tv_fragment_profile_StartSurvey);

        }

    }
    class ProfileListAdapter extends RecyclerView.Adapter<ProfileListHolder>{

        List<ProfileSurveyListItem>itemList;
        Context mContext;
        public ProfileListAdapter(Context c, List<ProfileSurveyListItem> list){
            this.itemList=list;
            this.mContext=c;
        }
        @NonNull
        @Override
        public ProfileListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_survey_listitem,parent,false);
            ProfileListHolder auditListHolder= new ProfileListHolder(itemView);
            return auditListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileListHolder holder, int position) {

            String surveyId=itemList.get(position).getSurveyId();
            String surveyName=itemList.get(position).getTaskName();
            holder.SurveyNameText.setText(surveyName.toUpperCase());

            holder.StartSurvey.setText("Start Survey to Complete Profile "+surveyName.toUpperCase()+" Details");

            holder.mainView.setOnClickListener(v->{
                if(profileDialog!=null){
                    profileDialog.dismiss();
                }
                Log.e("Percentage",""+ProfilePercentage);
                Intent intent= new Intent(mActivity, ProfileSurvey_AttemptActivity.class);
                intent.putExtra("survey_type",surveyName);
                intent.putExtra("percentage",ProfilePercentage);
                startActivityForResult(intent,REQUEST_CODE);
                //startActivity(intent);
            });



        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode == RESULT_CODE){
            getProfileDashboard();
        }
    }
    /*********Point Data*****************/
    private void getProfileDashboard(){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.getDashboardProfile(new DashboardPostData(sUserEmail)).enqueue(new Callback<DashboardProfileResponse>() {
                @Override
                public void onResponse(Call<DashboardProfileResponse> call, Response<DashboardProfileResponse> response) {
                    try {
                        DashboardProfileResponse dashboardProfileResponse = response.body();
                        int code = dashboardProfileResponse.status;
                        if (code == 200) {
                            ProfileSurveyList.clear();
                            List<DashboardProfileResponse.profileTaskStatus> proflei = dashboardProfileResponse.data.ProfileTask;
                            List<DashboardProfileResponse.dataUser> userData = dashboardProfileResponse.data.dataUsers;
                            for (int i = 0; i < userData.size(); i++) {
                                ProfilePercentage = userData.get(i).profilePercent;
                            }
                            for (int i = 0; i < proflei.size(); i++) {
                                boolean pr = proflei.get(i).status;
                                String id = proflei.get(i).id;
                                String taskName = proflei.get(i).taskName;
                                if (!pr) {
                                    ProfileSurveyList.add(new ProfileSurveyListItem(id, taskName, pr));
                                }
                            }



                            progressBar.setProgress(ProfilePercentage);
                            tvPercentage.setText(ProfilePercentage + "%");




                        } else {
                            Utility.showAlertDialog(mActivity, "Error!!", "Something went wrong!!");
                        }
                        Utility.hideDialog(mActivity);

                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);
                    }
                }

                @Override
                public void onFailure(Call<DashboardProfileResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });

        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }

    private void changePasswordDialog(){
        Dialog dialog= new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_changepassword);

        CardView changeCv=dialog.findViewById(R.id.cv_change_ChangePassword);
        EditText currentPass=dialog.findViewById(R.id.et_change_currentpassword);
        EditText newPass=dialog.findViewById(R.id.et_change_newpassword);
        EditText confirmPass=dialog.findViewById(R.id.et_change_confirmpassword);
        changeCv.setOnClickListener(v->{
            String curs=currentPass.getText().toString();
            String newPas=newPass.getText().toString();
            String confi=confirmPass.getText().toString();
            if(!TextUtils.isEmpty(curs.trim())){

                if(!TextUtils.isEmpty(newPas)&&newPas.length()>8&& Utility.isValidPassword(newPas)){

                    if(!TextUtils.isEmpty(confi.trim())&&newPas.matches(confi)){
                        if(!curs.matches(newPas)){
                            doChangePassword(curs,newPas,confi);
                            dialog.dismiss();
                        }else{
                            newPass.setText("");
                            confirmPass.setText("");
                            newPass.requestFocus();
                            Toast.makeText(mActivity, "New Password and Current Password Cannot be same, Please use other password", Toast.LENGTH_SHORT).show();
                            Utility.showEditTextError(newPass,"New Password and Current Password Cannot be same, Please use other password");
                        }

                    }else{
                        Toast.makeText(mActivity, "New Password and Confirm Password didn't match", Toast.LENGTH_SHORT).show();
                        Utility.showEditTextError(newPass,"New Password and Confirm Password didn't match");
                    }



                }else{
                    Toast.makeText(mActivity, passwordError, Toast.LENGTH_SHORT).show();
                    Utility.showEditTextError(newPass,passwordError);

                }
            }else{
                Toast.makeText(mActivity, "Please Enter Your Current Password", Toast.LENGTH_SHORT).show();
                Utility.showEditTextError(currentPass,"Please Enter Your Current Password");
            }
        });

        dialog.show();
        ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);
        dialog.getWindow().setBackgroundDrawable(drawable);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    }
    private void doChangePassword(String currentPassword,String newPassword,String confirmPassword){

        if(Utility.isNetworkAvailable(mActivity)) {
            Utility.showDialog(mActivity);
            apiService.DoChangePassword(new ChangePassword_Request(sUserEmail,currentPassword,newPassword,confirmPassword)).enqueue(new Callback<ReedemPointResponse>() {
                @Override
                public void onResponse(Call<ReedemPointResponse> call, Response<ReedemPointResponse> response) {
                    try{
                        ReedemPointResponse reedemPointResponse=response.body();
                        int code= reedemPointResponse.status;
                        String msg=reedemPointResponse.message;
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);

                    }catch (Exception e){
                        e.printStackTrace();
                        Utility.showAlertDialog(mActivity,"Error!!","Something went wrong!! Please Try Again");
                        Utility.hideDialog(mActivity);
                    }
                }

                @Override
                public void onFailure(Call<ReedemPointResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });
        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }

    }
}
