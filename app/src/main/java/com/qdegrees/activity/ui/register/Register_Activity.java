package com.qdegrees.activity.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.qdegrees.activity.Authenticator_Activity;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.SignupVerifyEmailRequest;
import com.qdegrees.network.request.VerifyOtpRequest;
import com.qdegrees.network.response.SignupVerifyEmailReqResponse;
import com.qdegrees.network.response.VerifyOtpResponse;
import com.qdegrees.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_Activity extends AppCompatActivity {
    Activity mActivity;
    TextView tvSignupPoweredby,tvSignupLogin;
    String EmailStr="";
    EditText edMail,edOTP;
    CardView cv_verify,cv_signupfacebook,cv_signupgoogle;
    String OTPServerStr="",OTPStr="";
    LinearLayout otpLayout;
    protected ApiService apiService;

    /***************facebook**************************/
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    /*************Google Sign In*********************/
    GoogleSignInClient mGoogleSignInClient;
    public int RC_SIGN_IN=14;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mActivity = this;
        apiService = RetrofitAPIClient.getRetrofitClient();
        findIds();
        FacebookSdk.sdkInitialize(mActivity);
        callbackManager=CallbackManager.Factory.create();
        facebookLogin();


        /*******Google Sign in**********/
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleSignInClient= GoogleSignIn.getClient(mActivity,gso);



    }
    private void findIds(){
        tvSignupPoweredby=findViewById(R.id.tv_signup_poweredby);

        tvSignupLogin=findViewById(R.id.tv_Signup_Login);
        edMail=findViewById(R.id.et_signup_email);
        cv_verify=findViewById(R.id.cv_signup_verify);
        otpLayout=findViewById(R.id.llSignupOtp);
        edOTP=findViewById(R.id.et_signup_OTP);
        cv_signupfacebook=findViewById(R.id.cv_signup_facebook);
        cv_signupgoogle=findViewById(R.id.cv_signup_google);



        tvSignupPoweredby.setText(Html.fromHtml(mActivity.getResources().getString(R.string.PoweredBy)));
        tvSignupPoweredby.setMovementMethod(LinkMovementMethod.getInstance());
        tvSignupLogin.setText(Html.fromHtml(mActivity.getResources().getString(R.string.login)));



        tvSignupLogin.setOnClickListener(v->{
            Intent intent= new Intent(mActivity, Authenticator_Activity.class);
            startActivity(intent);
            onBackPressed();
        });
        cv_verify.setOnClickListener(v->{

            if(Utility.isNetworkAvailable(mActivity)){
                if(otpLayout.getVisibility()== View.VISIBLE){
                    if(verifyOTPValidation()){
                        doVerifyOTP();
                    }

                }else{
                    if(verifyValidation()){
                        getEmailOTP();
                    }

                }
            }else{
                Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
            }



        });
        cv_signupfacebook.setOnClickListener(v->{
            loginManager.logInWithReadPermissions(
                    mActivity,
                    Arrays.asList(
                            "email",
                            "public_profile",
                            "user_birthday"));
        });

        cv_signupgoogle.setOnClickListener(v->{
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private boolean verifyValidation(){
        EmailStr=edMail.getText().toString();
        if(!TextUtils.isEmpty(EmailStr.trim())&& Utility.isValidEmail(EmailStr)){
                return true;
        }else{
            Toast.makeText(mActivity, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            Utility.showEditTextError(edMail,"Please Enter Valid Email");
            return false;
        }
    }
    private void getEmailOTP(){
        Utility.showDialog(mActivity);
        apiService.getVerifyOtp(new SignupVerifyEmailRequest(EmailStr)).enqueue(new Callback<SignupVerifyEmailReqResponse>() {
            @Override
            public void onResponse(Call<SignupVerifyEmailReqResponse> call, Response<SignupVerifyEmailReqResponse> response) {
               try{
                SignupVerifyEmailReqResponse signupVerifyEmailReqResponse=response.body();
                Log.e("Response",signupVerifyEmailReqResponse.toString());

                int code=signupVerifyEmailReqResponse.status;
                String message=signupVerifyEmailReqResponse.message;
                if(code==200){
                    SignupVerifyEmailReqResponse.Data data=signupVerifyEmailReqResponse.data;
                    String Id=data.id;
                    String email=data.email;
                    String _V=data.__v;
                    String expireIn=data.expire_in;
                    OTPServerStr=data.otp;
                    otpLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                    Utility.hideDialog(mActivity);

                }else{
                    Utility.showAlertDialog(mActivity,"Error!!",message);
                    Utility.hideDialog(mActivity);
                    OTPServerStr="";
                    otpLayout.setVisibility(View.GONE);
                }
               }catch(Exception e){
                   e.printStackTrace();
                   Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                   Utility.hideDialog(mActivity);
               }

            }

            @Override
            public void onFailure(Call<SignupVerifyEmailReqResponse> call, Throwable t) {
                Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                Utility.hideDialog(mActivity);
                OTPServerStr="";
                otpLayout.setVisibility(View.GONE);
            }
        });
    }

    private boolean verifyOTPValidation(){
        EmailStr=edMail.getText().toString();
        OTPStr=edOTP.getText().toString();
        if(!TextUtils.isEmpty(EmailStr.trim())&& Utility.isValidEmail(EmailStr)){
            if(!TextUtils.isEmpty(OTPStr.trim())&&OTPStr.length()==6){
                    return true;
            }else{
                Toast.makeText(mActivity, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                Utility.showEditTextError(edMail,"Please Enter Valid OTP");
                return false;
            }
        }else{
            Toast.makeText(mActivity, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            Utility.showEditTextError(edMail,"Please Enter Valid Email");
            return false;
        }
    }
    private void doVerifyOTP(){
        Utility.showDialog(mActivity);
        apiService.doVerifyOtp(new VerifyOtpRequest(EmailStr,OTPStr)).enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                try {
                    VerifyOtpResponse verifyOtpResponse = response.body();
                    int code = verifyOtpResponse.status;
                    String message = verifyOtpResponse.message;
                    if (code == 200) {
                        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mActivity, Register_General_Details.class);
                        intent.putExtra("email", EmailStr);
                        intent.putExtra("fb_id", "");
                        startActivity(intent);
                        Utility.hideDialog(mActivity);
                        onBackPressed();
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
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                Utility.hideDialog(mActivity);
            }
        });
    }



    /*********************Facebook****************************/
    public void facebookLogin()
    {

        loginManager
                = LoginManager.getInstance();
        callbackManager
                = CallbackManager.Factory.create();

        loginManager
                .registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult)
                            {

                                Log.e("LoginResult",loginResult.toString());

                                GraphRequest request = GraphRequest.newMeRequest(

                                        loginResult.getAccessToken(),

                                        new GraphRequest.GraphJSONObjectCallback() {

                                            @Override
                                            public void onCompleted(JSONObject object,
                                                                    GraphResponse response)
                                            {


                                                if (object != null) {
                                                    try {
                                                        String name = object.getString("name");
                                                        String email = object.getString("email");
                                                        String fbUserID = object.getString("id");

                                                        Log.e("fbUserId",fbUserID);


                                                        disconnectFromFacebook();
                                                        Intent intent = new Intent(mActivity, Register_General_Details.class);
                                                        intent.putExtra("email", email);
                                                        intent.putExtra("fb_id", fbUserID);
                                                        intent.putExtra("google_id", fbUserID);
                                                        startActivity(intent);
                                                        onBackPressed();

                                                        // do action after Facebook login success
                                                        // or call your API
                                                    }
                                                    catch (JSONException | NullPointerException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString(
                                        "fields",
                                        "id, name, email, gender, birthday,picture.type(large)");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel()
                            {
                                Log.v("LoginScreen", "---onCancel");
                            }

                            @Override
                            public void onError(FacebookException error)
                            {
                                // here write code when get error
                                Log.v("LoginScreen", "----onError: "
                                        + error.getMessage());
                            }
                        });
    }
    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse)
                    {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);


        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            // add this line
            callbackManager.onActivityResult(
                    requestCode,
                    resultCode,
                    data);
        }

    }


    /************************Google Sign In******************************/


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String email=account.getEmail();
            String googleId=account.getId();
            String Name=account.getDisplayName();
            Log.e("Email",email);
            Log.e("googleId",googleId);
            Log.e("Name",Name);
            signOut();
            Intent intent = new Intent(mActivity, Register_General_Details.class);
            intent.putExtra("email", email);
            intent.putExtra("fb_id", "");
            intent.putExtra("google_id", googleId);
            startActivity(intent);
            onBackPressed();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("TAG", "signInResult:failed code=" + e.getStatusCode());

        }
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }


}
