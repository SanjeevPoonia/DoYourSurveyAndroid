package com.qdegrees.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.qdegrees.activity.ui.register.Register_Activity;
import com.qdegrees.activity.ui.register.Register_General_Details;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.LoginPostData;
import com.qdegrees.network.request.SocialLogin_Request;
import com.qdegrees.network.response.LoginResponse;
import com.qdegrees.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Authenticator_Activity extends AppCompatActivity {
    Activity mActivity;
    TextView tvLoginForgotPassword,tvLoginSignup;
    String EmailStr="",PasswordStr="";
    TextInputEditText edMail,edPassword;
    CardView cv_signin,cv_googlesignin,cv_facebookSignIn;
    protected ApiService apiService;

    /***************facebook**************************/
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    /*************Google Sign In*********************/
    GoogleSignInClient mGoogleSignInClient;
    public int RC_SIGN_IN=14;

    /***************App Update**********************/

    private AppUpdateManager appUpdateManager;
    private final int IMMEDIATE_APP_UPDATE_REQ_CODE=12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        mActivity = this;

        /*******App Update***************/
        appUpdateManager= AppUpdateManagerFactory.create(mActivity);
        checkUpdate();

        /*******************************/


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

        tvLoginForgotPassword=findViewById(R.id.tv_login_forgotpassword);
        tvLoginSignup=findViewById(R.id.tv_login_signup);
        edMail=findViewById(R.id.et_login_email);
        edPassword=findViewById(R.id.et_login_password);
        cv_signin=findViewById(R.id.cv_login_signin);
        cv_googlesignin=findViewById(R.id.cv_login_google);
        cv_facebookSignIn=findViewById(R.id.cv_login_facebook);

      /*  tvLoginPoweredby.setText(Html.fromHtml(mActivity.getResources().getString(R.string.PoweredBy)));
        tvLoginPoweredby.setMovementMethod(LinkMovementMethod.getInstance());
        tvLoginForgotPassword.setText(Html.fromHtml(mActivity.getResources().getString(R.string.login_createAccount)));
*/
        tvLoginSignup.setText(Html.fromHtml(mActivity.getResources().getString(R.string.login_signup)));
        cv_signin.setOnClickListener(v->{
            if(Utility.isNetworkAvailable(mActivity)){
                if(loginValidation()){
                    doLogin();
                }
            }else{
                Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
            }
        });
        tvLoginSignup.setOnClickListener(v->{
            Intent intent= new Intent(mActivity, Register_Activity.class);
            startActivity(intent);
            onBackPressed();

        });

        cv_googlesignin.setOnClickListener(v->{
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        cv_facebookSignIn.setOnClickListener(v->{
            loginManager.logInWithReadPermissions(
                    mActivity,
                    Arrays.asList(
                            "email",
                            "public_profile",
                            "user_birthday"));
        });




    }

    private boolean loginValidation(){
        EmailStr=edMail.getText().toString();
        PasswordStr=edPassword.getText().toString();
        if(!TextUtils.isEmpty(EmailStr.trim())&& Utility.isValidEmail(EmailStr)){
            if(!TextUtils.isEmpty(PasswordStr.trim())){
                return true;
            }else{
                Toast.makeText(mActivity, "Please Enter your password", Toast.LENGTH_SHORT).show();
                Utility.showEditTextError(edPassword,"Please Enter your password");
                return false;
            }
        }else{
            Toast.makeText(mActivity, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            Utility.showEditTextError(edMail,"Please Enter Valid Email");
            return false;
        }
    }
    private void doLogin(){
        if(Utility.isNetworkAvailable(mActivity)) {
            Utility.showDialog(mActivity);
            apiService.doLogin(new LoginPostData(EmailStr, PasswordStr,"user")).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    try {
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
                            Intent intent = new Intent(mActivity, HomeAct.class);
                            startActivity(intent);
                            /*Intent intent = new Intent(mActivity, MainHomeActivity.class);
                            startActivity(intent);*/
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    /**************************************************************/

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
                                                        SocialLogin(fbUserID);

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
        }if(requestCode==IMMEDIATE_APP_UPDATE_REQ_CODE){
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! ", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Update success!" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed!", Toast.LENGTH_LONG).show();
                checkUpdate();
            }
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
            SocialLogin(googleId);
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
    public void printHashKey()
    {

        // Add code to print out the key hash
        try {

            PackageInfo info
                    = getPackageManager().getPackageInfo("com.qdegrees.doyoursurvey",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md
                        = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:",
                        Base64.encodeToString(
                                md.digest(),
                                Base64.DEFAULT));
            }
        }

        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("Error!!!!!",e.toString());
        }

        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("Error1!!!!!",e.toString());
        }
    }

    /*********************SignInWIthSocial**************************/

    private void SocialLogin(String socialID){
        if(Utility.isNetworkAvailable(mActivity)) {
            Utility.showDialog(mActivity);
            apiService.doSocialLogin(new SocialLogin_Request(socialID)).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    try {
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
                            SharedPreferencesRepository.getDataManagerInstance().setIsLoggedIn(true);
                            SharedPreferencesRepository.saveUserDetails(id, email, mobile, firstName, lastName, fullName, gender, dob, city, refferalBy, referCode, profileImage, googleId, facebookUserID, date, v, token,"");
                            Utility.hideDialog(mActivity);
                            Toast.makeText(mActivity, "Login Successfully!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mActivity, HomeAct.class);
                            startActivity(intent);
                            /*Intent intent = new Intent(mActivity, MainHomeActivity.class);
                            startActivity(intent);*/
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




    /*
    Check for update
     */
    private void checkUpdate() {

        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

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
