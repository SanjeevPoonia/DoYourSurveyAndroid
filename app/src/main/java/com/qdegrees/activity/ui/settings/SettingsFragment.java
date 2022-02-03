package com.qdegrees.activity.ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.ChangePassword_Request;
import com.qdegrees.network.request.ReferAFriend_Request;
import com.qdegrees.network.response.ReedemPointResponse;
import com.qdegrees.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {
    Activity mActivity;
    TextView NameText,EmailText,MobileText;
    CardView ChangePasswordCard,ReferAFriend;
    String NameStr="",EmailStr="",MobileStr="";
    protected ApiService apiService;
    String passwordError="Password must be at least 8 characters and must contain at least one lower case letter, one upper case letter and one digit";
    String ReferCode="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_accountsetting, container, false);
        mActivity=getActivity();
        apiService = RetrofitAPIClient.getRetrofitClient();
        EmailStr= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
        NameStr=SharedPreferencesRepository.getDataManagerInstance().getSessionname()+" "+ SharedPreferencesRepository.getDataManagerInstance().getSessionLastname();
        MobileStr= SharedPreferencesRepository.getDataManagerInstance().getSessionMobile();
        ReferCode= SharedPreferencesRepository.getDataManagerInstance().getSessionReferCode();

        NameText=root.findViewById(R.id.Fragment_AccountSetting_NameTextView);
        EmailText=root.findViewById(R.id.Fragment_AccountSetting_EmailTextView);
        MobileText=root.findViewById(R.id.Fragment_AccountSetting_PhoneTextView);
        ChangePasswordCard=root.findViewById(R.id.cv_Fragment_AccountSetting_ChangePassword);
        ReferAFriend=root.findViewById(R.id.cv_Fragment_AccountSetting_ReferFriend);


        NameText.setText(NameStr);
        EmailText.setText(EmailStr);
        MobileText.setText(MobileStr);

        ChangePasswordCard.setOnClickListener(v->{
            changePasswordDialog();
        });
        ReferAFriend.setOnClickListener(v->{
            ReferAFriendDialog();
        });


        return root;
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
            apiService.DoChangePassword(new ChangePassword_Request(EmailStr,currentPassword,newPassword,confirmPassword)).enqueue(new Callback<ReedemPointResponse>() {
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



    /**************************************Refer Friend Functionality***********************************************/

    private void ReferAFriendDialog(){
        Dialog referDialog= new Dialog(mActivity);
        referDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        referDialog.setContentView(R.layout.dialog_referfriend);

        String ReferalString=mActivity.getResources().getString(R.string.Referal_Text)+ReferCode+""+mActivity.getResources().getString(R.string.Referal_text_2);
        String Applink="https://doyoursurvey.com/#/register";
        Log.e("ReferalString",ReferalString);


        CardView whatsAppCard=referDialog.findViewById(R.id.cv_refer_whatsapp);
        EditText NameEditText=referDialog.findViewById(R.id.et_refer_name);
        EditText EmaildEditText=referDialog.findViewById(R.id.et_refer_Email);
        CardView submitCard=referDialog.findViewById(R.id.cv_refer_submit);
        ImageView facebookImage=referDialog.findViewById(R.id.iv_refer_facebook);
        ImageView twitterImage=referDialog.findViewById(R.id.iv_refer_twitter);
        ImageView linkdinImage=referDialog.findViewById(R.id.iv_refer_linkedin);
        ImageView mailImage=referDialog.findViewById(R.id.iv_refer_mail);


        whatsAppCard.setOnClickListener(v->{
            Utility.shareWhatsapp(mActivity,ReferalString,"");
            referDialog.dismiss();
        });
        facebookImage.setOnClickListener(v->{
            /*Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, ReferalString);
            PackageManager pm = v.getContext().getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains("facebook")) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |             Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    v.getContext().startActivity(shareIntent);
                    referDialog.dismiss();
                    break;
                }
            }*/
            Utility.shareFacebook(mActivity,ReferalString,Applink);
            referDialog.dismiss();
        });
        twitterImage.setOnClickListener(v->{
            Utility.shareTwitter(mActivity,ReferalString,"","","");
            referDialog.dismiss();
        });
        linkdinImage.setOnClickListener(v->{
            Utility.shareLinkedin(mActivity,ReferalString);
            referDialog.dismiss();
        });
        mailImage.setOnClickListener(v->{
            Utility.shareOnMail(mActivity,"Do Survey and Earn Points",ReferalString);
            referDialog.dismiss();
        });
        submitCard.setOnClickListener(v->{
            String nameStr=NameEditText.getText().toString();
            String emailStr=EmaildEditText.getText().toString();
            if(!TextUtils.isEmpty(emailStr)&& Utility.isValidEmail(emailStr)){
                doReferFriend(nameStr,emailStr);
                referDialog.dismiss();
            }else{
                EmaildEditText.setError("Please Enter a valid Email ID");
                Toast.makeText(mActivity, "Please Enter a valid Email ID", Toast.LENGTH_SHORT).show();
            }
        });


        referDialog.show();
        ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);
        referDialog.getWindow().setBackgroundDrawable(drawable);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(referDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        referDialog.getWindow().setAttributes(lp);
    }

    private void doReferFriend(String name,String email){
        if(Utility.isNetworkAvailable(mActivity)){
            Utility.showDialog(mActivity);
            apiService.DoReferFriend(new ReferAFriend_Request(NameStr,name,ReferCode,EmailStr,email)).enqueue(new Callback<ReedemPointResponse>() {
                @Override
                public void onResponse(Call<ReedemPointResponse> call, Response<ReedemPointResponse> response) {
                    ReedemPointResponse reedemPointResponse= response.body();
                    int code=reedemPointResponse.status;
                    String msg=reedemPointResponse.message;
                    Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);

                }

                @Override
                public void onFailure(Call<ReedemPointResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });

        }else {
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
}