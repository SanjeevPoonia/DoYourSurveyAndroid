package com.qdegrees.activity.ui.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.qdegrees.activity.Authenticator_Activity;
import com.qdegrees.activity.HomeAct;
import com.qdegrees.activity.MainHomeActivity;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.RegisterUserRequest;
import com.qdegrees.network.response.LoginResponse;
import com.qdegrees.utils.Utility;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_General_Details extends AppCompatActivity {
    Activity mActivity;

    protected ApiService apiService;
    String EmailStr="",FB_Id="",Google_id="";
    TextView tvEmail,tvLogin;
    TextInputEditText etPassword,etFirstName,etLastName,etMobile,etCity,etReedemCoupon,tvDOB,etBio;
    RadioGroup rgGender;
    String strPassword="",strFirstName="",strLastName="",strMobile="",strCity="",strCoupon="",strGender="",strDob="",strBio="";
    CardView cvSubmit;
    String passwordError="Password must be at least 8 characters and must contain at least one lower case letter, one upper case letter and one digit";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_general_details);
        mActivity = this;
        EmailStr=getIntent().getStringExtra("email");
        FB_Id=getIntent().getStringExtra("fb_id");
        Google_id=getIntent().getStringExtra("google_id");
        apiService = RetrofitAPIClient.getRetrofitClient();
        findIds();

    }
    private void findIds(){


        tvEmail=findViewById(R.id.tvGeneralDetailEmail);
        tvDOB=findViewById(R.id.tv_registergeneral_dob);
        etPassword=findViewById(R.id.et_registergeneral_password);
        etFirstName=findViewById(R.id.et_registergeneral_firstname);
        etLastName=findViewById(R.id.et_registergeneral_lastName);
        etMobile=findViewById(R.id.et_registergeneral_Phone);
        etCity=findViewById(R.id.et_registergeneral_City);
        etBio = findViewById(R.id.et_registergeneral_Bio);
        etReedemCoupon=findViewById(R.id.et_registergeneral_RedeemCoupon);
        rgGender=findViewById(R.id.rgRegisterGeneralGender);
        cvSubmit=findViewById(R.id.cv_RegisterGeneral_Submit);
        tvLogin=findViewById(R.id.tv_registergeneral_Login);
        tvLogin.setText(Html.fromHtml(mActivity.getResources().getString(R.string.login)));

        tvEmail.setText(EmailStr);


        tvLogin.setOnClickListener(v->{
            Intent intent= new Intent(mActivity, Authenticator_Activity.class);
            startActivity(intent);
            onBackPressed();
        });
        rgGender.clearCheck();
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rbGenderMale){
                    strGender=mActivity.getResources().getString(R.string.Gender_Male);
                }else if(checkedId==R.id.rbGenderFeMale){
                    strGender=mActivity.getResources().getString(R.string.Gender_FeMale);
                } else if(checkedId==R.id.rbGenderTransgender){
                    strGender=mActivity.getResources().getString(R.string.Gender_Transgender);
                }
            }
        });
        tvDOB.setOnClickListener(v->{
            DOBDatePicker();
        });

        cvSubmit.setOnClickListener(v->{
            if(validations()){
                if(Utility.isNetworkAvailable(mActivity)){
                    doSignup();
                }else{
                    Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
                }
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void DOBDatePicker() {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String DayStr = "0";
                        String MStr = "0";
                        if (dayOfMonth < 10) {
                            DayStr = "0" + String.valueOf(dayOfMonth);
                        } else {
                            DayStr = String.valueOf(dayOfMonth);
                        }
                        if (monthOfYear + 1 < 10) {
                            MStr = "0" + String.valueOf(monthOfYear + 1);
                        } else {
                            MStr = String.valueOf(monthOfYear + 1);
                        }

                        strDob = year + "-" + MStr + "-" +DayStr ;
                        //*************Call Time Picker Here ********************
                        tvDOB.setText(strDob);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
        datePickerDialog.show();
    }
    private boolean validations(){
        strPassword=etPassword.getText().toString();
        strFirstName=etFirstName.getText().toString();
        strLastName=etLastName.getText().toString();
        strMobile=etMobile.getText().toString();
        strCity=etCity.getText().toString();
        strCoupon=etReedemCoupon.getText().toString();
        strBio = etBio.getText().toString();



        if(!TextUtils.isEmpty(strPassword)&&strPassword.length()>8&& Utility.isValidPassword(strPassword)){
            if(!TextUtils.isEmpty(strMobile)&&strMobile.length()==10){
                if(!TextUtils.isEmpty(strFirstName.trim())){
                    if(!TextUtils.isEmpty(strGender)){
                        if(!TextUtils.isEmpty(strDob)){
                            if(!TextUtils.isEmpty(strCity.trim())){
                                return true;
                            }else {
                                Toast.makeText(mActivity, "Please fill Your city", Toast.LENGTH_SHORT).show();
                                Utility.showEditTextError(etCity,"Please fill Your city");
                                return false;
                            }

                        }else{
                            Toast.makeText(mActivity, "Please select your Date Of Birth", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    }else{
                        Toast.makeText(mActivity, "Please select your gender", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                }else{
                    Toast.makeText(mActivity, "Please fill First Name", Toast.LENGTH_SHORT).show();
                    Utility.showEditTextError(etFirstName,"Please fill First Name");
                    return false;
                }

            }else{
                Toast.makeText(mActivity, "Please Enter valid mobile number", Toast.LENGTH_SHORT).show();
                Utility.showEditTextError(etMobile,"Please Enter valid mobile number");
                return false;
            }


        }else{
            Toast.makeText(mActivity, passwordError, Toast.LENGTH_SHORT).show();
            Utility.showEditTextError(etPassword,passwordError);
            return false;
        }
    }
    private void doSignup(){
        if(Utility.isNetworkAvailable(mActivity)) {
            Utility.showDialog(mActivity);
            apiService.doSignup(new RegisterUserRequest(EmailStr, strMobile, strFirstName, strLastName, strFirstName + " " + strLastName, strGender, strDob, strCity, "", strCoupon, "", Google_id, FB_Id, strPassword,"panelist",strBio)).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    try{
                    LoginResponse loginResponse = response.body();
                    Log.e("Response", loginResponse.toString());
                    int code = loginResponse.status;
                    String message = loginResponse.message;
                    if (code == 200) {
                        String token = loginResponse.token;
                        LoginResponse.Data data = loginResponse.data;
                        String id = data.id;
                        String email = data.email;
                        String mobile = data.mobile;
                        String firstName = data.firstName;
                        String lastName = data.lastName;
                        String fullName = data.fullName;
                        String gender = data.gender;
                        String dob = data.dob;
                        String city = data.city;
                        String refferalBy = data.refferalBy;
                        String referCode = data.referCode;
                        String profileImage = data.profileImage;
                        String googleId = data.googleId;
                        String facebookUserID = data.facebookUserID;
                        String date = data.date;
                        String v = data.v;
                        String bio = data.bio;
                        SharedPreferencesRepository.getDataManagerInstance().setIsLoggedIn(true);
                        SharedPreferencesRepository.saveUserDetails(id, email, mobile, firstName, lastName, fullName, gender, dob, city, refferalBy, referCode, profileImage, googleId, facebookUserID, date, v, token,bio);
                        Utility.hideDialog(mActivity);
                        Toast.makeText(mActivity, "Login Successfully!!!", Toast.LENGTH_SHORT).show();
                      /*  Intent intent = new Intent(mActivity, MainHomeActivity.class);
                        startActivity(intent);
                        finish(); */
                        Intent intent = new Intent(mActivity, HomeAct.class);
                        startActivity(intent);
                        finish();


                    } else {
                        Utility.showAlertDialog(mActivity, "Error!!", message);
                        Utility.hideDialog(mActivity);
                    }
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                        Utility.hideDialog(mActivity);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);
                }
            });
        }else{
            Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
        }
    }
}
