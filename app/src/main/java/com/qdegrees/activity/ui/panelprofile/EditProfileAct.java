package com.qdegrees.activity.ui.panelprofile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.qdegrees.doyoursurvey.R;
import com.qdegrees.local_storage.SharedPreferencesRepository;
import com.qdegrees.network.ApiService;
import com.qdegrees.network.RetrofitAPIClient;
import com.qdegrees.network.request.UpdateProfilePostData;
import com.qdegrees.network.response.ProfileUploadResponse;
import com.qdegrees.network.response.UpdateProfileResponse;
import com.qdegrees.utils.ProgressRequestBody;
import com.qdegrees.utils.URIPath;

import com.qdegrees.utils.Utility;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditProfileAct extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {
   Activity mActivity;
   TextInputEditText fNameEdit,lNameEdit,cityEdit,mobileEdit,dobEdit,bioEdit;
   RadioGroup rgGender;
   RadioButton rbMale,rbFemale,rbTrans;

   String fNameStr="",lNameStr="",cityStr="",mobileStr="",dobStr="",bioStr="",UserIdStr="";
   String genderStr="",emailStr="";
   String referalStr="",googleIdStr="",faceBookId="";

   CardView updateCv;
   int RC_FILE_PERM = 253;
   ImageView ivProfileImageUpdate;
   CircleImageView ProfileImage;


   private ArrayList permissionsToRequest;
   private ArrayList permissionsRejected = new ArrayList();
   private ArrayList permissions = new ArrayList();
   private final static int ALL_PERMISSIONS_RESULT = 101;

   final int MY_PERMISSIONS_REQUEST_IMAGEVIEW = 1118;
   final int MY_PERMISSIONS_REQUEST_CAMERA_IMAGEVIEW = 1120;
   final int IMAGE_BROWSE_CODE = 1111;
   final int IMAGE_BROWSE_CAMERA_CODE = 1113;

   String CameraImagePath="";


   ProgressBar fileUploadProgress;
   AppCompatTextView fileUploadTitle;
   AppCompatTextView fileUploadper;
   BottomSheetDialog fileUploadDialog;

   protected ApiService apiService;


   String uploadedImagePath="";




   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_edit_profile);
      mActivity= this;
      apiService = RetrofitAPIClient.getRetrofitClient();

      emailStr= SharedPreferencesRepository.getDataManagerInstance().getSessionEmail();
      UserIdStr= SharedPreferencesRepository.getDataManagerInstance().getSessionUserId();
      fNameStr= SharedPreferencesRepository.getDataManagerInstance().getSessionname();
      lNameStr= SharedPreferencesRepository.getDataManagerInstance().getSessionLastname();
      cityStr= SharedPreferencesRepository.getDataManagerInstance().getSessionCity();
      mobileStr= SharedPreferencesRepository.getDataManagerInstance().getSessionMobile();
      dobStr= SharedPreferencesRepository.getDataManagerInstance().getSessionDOB();
      bioStr = SharedPreferencesRepository.getDataManagerInstance().getSessionBio();
      genderStr = SharedPreferencesRepository.getDataManagerInstance().getSessionGender();
      uploadedImagePath = SharedPreferencesRepository.getDataManagerInstance().getSessionProfileImage();
      referalStr =SharedPreferencesRepository.getDataManagerInstance().getSessionReferCode();
      googleIdStr =SharedPreferencesRepository.getDataManagerInstance().getSessionGoogleId();
      faceBookId =SharedPreferencesRepository.getDataManagerInstance().getSessionFaceBookId();

      Log.e("user_id",UserIdStr);


      permissions.add(READ_EXTERNAL_STORAGE);
      permissions.add(WRITE_EXTERNAL_STORAGE);
      permissions.add(CAMERA);
      permissionsToRequest = findUnAskedPermissions(permissions);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         if (permissionsToRequest.size() > 0){
            String[] permArray= new String[permissionsToRequest.size()];
            for(int i=0;i<permissionsToRequest.size();i++){
               permArray[i]=permissionsToRequest.get(i).toString();

            }
            requestPermissions(permArray,ALL_PERMISSIONS_RESULT);
         }

      }

      fNameEdit= findViewById(R.id.et_EditProfile_FirstName);
      lNameEdit= findViewById(R.id.et_EditProfile_LastName);
      cityEdit= findViewById(R.id.et_EditProfile_City);
      mobileEdit= findViewById(R.id.et_EditProfile_mobile);
      dobEdit= findViewById(R.id.et_EditProfile_dob);
      bioEdit= findViewById(R.id.et_EditProfile_bio);
      rgGender= findViewById(R.id.rg_editProfile_gender);
      rbMale = findViewById(R.id.rb_EditProfile_GenderMale);
      rbFemale = findViewById(R.id.rb_EditProfile_GenderFeMale);
      rbTrans = findViewById(R.id.rb_EditProfile_GenderTransgender);
      updateCv=findViewById(R.id.cv_Frag_Panel_EditProfile);

      ivProfileImageUpdate=findViewById(R.id.ivEditprofile_EditUser);
      ProfileImage=findViewById(R.id.ivEditProfile_User);


      showUploadProgress();


      if(!TextUtils.isEmpty(dobStr)){
         String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
         String outputPattern = "dd-MMM-yyyy";
         SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
         SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
         Date date = null;
         String str = null;
         try {
            date = inputFormat.parse(dobStr);
            str = outputFormat.format(date);
            dobEdit.setText(str);
         } catch (ParseException e) {
            e.printStackTrace();
         }
      }else{
         dobEdit.setText(dobStr);
      }



      if(!TextUtils.isEmpty(genderStr)){
         if(genderStr.matches(getResources().getString(R.string.Gender_Male))){
            rbMale.setChecked(true);
         }else if(genderStr.matches(getResources().getString(R.string.Gender_FeMale))){
            rbFemale.setChecked(true);
         }else if(genderStr.matches(getResources().getString(R.string.Gender_Transgender))){
            rbTrans.setChecked(true);
         }
      }


      fNameEdit.setText(fNameStr);
      lNameEdit.setText(lNameStr);
      cityEdit.setText(cityStr);
      mobileEdit.setText(mobileStr);
      bioEdit.setText(bioStr);

      dobEdit.setOnClickListener(v->{
         DOBDatePicker();
      });
      rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId==R.id.rb_EditProfile_GenderMale){
               genderStr=mActivity.getResources().getString(R.string.Gender_Male);
            }else if(checkedId==R.id.rb_EditProfile_GenderFeMale){
               genderStr=mActivity.getResources().getString(R.string.Gender_FeMale);
            } else if(checkedId==R.id.rb_EditProfile_GenderTransgender){
               genderStr=mActivity.getResources().getString(R.string.Gender_Transgender);
            }
         }
      });

      updateCv.setOnClickListener(v->{
         if(validations()){
            updateProfileImage();
         }
      });

      ivProfileImageUpdate.setOnClickListener(v->{
         BrowseButtonDialog();

      });


   }
   private boolean validations(){
      fNameStr=fNameEdit.getText().toString();
      lNameStr= lNameEdit.getText().toString();
      cityStr= cityEdit.getText().toString();
      mobileStr= mobileEdit.getText().toString();
      bioStr= bioEdit.getText().toString();
      if(!TextUtils.isEmpty(fNameStr.trim())){
         if(!TextUtils.isEmpty(genderStr)){
            if(!TextUtils.isEmpty(cityStr.trim())){
               if(!TextUtils.isEmpty(mobileStr)&&mobileStr.length()==10){
                  if(!TextUtils.isEmpty(bioStr.trim())){
                     if(!TextUtils.isEmpty(uploadedImagePath)){
                        return true;
                     }else{
                        Toast.makeText(mActivity, "Please Upload Your Profile Image!!", Toast.LENGTH_SHORT).show();
                        return false;
                     }

                  }else{
                     Toast.makeText(mActivity, "Bio cannot be blank!!", Toast.LENGTH_SHORT).show();
                     Utility.showEditTextError(bioEdit,"Bio cannot be blank!!");
                     return false;
                  }

               }else{
                  Toast.makeText(mActivity, "Please Enter valid mobile number", Toast.LENGTH_SHORT).show();
                  Utility.showEditTextError(mobileEdit,"Please Enter valid mobile number");
                  return false;
               }

            }else{
               Toast.makeText(mActivity, "Please fill Your city", Toast.LENGTH_SHORT).show();
               Utility.showEditTextError(cityEdit,"Please fill Your city");
               return false;
            }

         }else{
            Toast.makeText(mActivity, "Please select your gender", Toast.LENGTH_SHORT).show();
            return false;
         }

      }else{
         Toast.makeText(mActivity, "Please fill First Name", Toast.LENGTH_SHORT).show();
         Utility.showEditTextError(fNameEdit,"Please fill First Name");
         return false;
      }
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

                    dobStr = DayStr + "-" + MStr + "-" + year;
                    //*************Call Time Picker Here ********************
                    dobEdit.setText(dobStr);
                 }
              }, mYear, mMonth, mDay);
      datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
      datePickerDialog.show();
   }
   public void BrowseButtonDialog(){
      final Dialog dialog = new Dialog(mActivity);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.setContentView(R.layout.dialog_browse_selector);
      CardView CameraLayout=dialog.findViewById(R.id.cv_BrowseSelector_Camera);
      CardView GalleryLayout=dialog.findViewById(R.id.cv_BrowseSelector_Gallery);

      CameraLayout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {


            onCameraImageClick();

            dialog.dismiss();
         }
      });
      GalleryLayout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            onBrowseImageClick();
            dialog.dismiss();
         }
      });

      dialog.show();
      try {
         Window window = dialog.getWindow();
         window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      } catch (NullPointerException e) {
         e.printStackTrace();
      }
   }
   private void onCameraImageClick() {
      if (ContextCompat.checkSelfPermission(mActivity,
              Manifest.permission.READ_EXTERNAL_STORAGE)
              != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity,
              Manifest.permission.WRITE_EXTERNAL_STORAGE)
              != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity,
              Manifest.permission.CAMERA)
              != PackageManager.PERMISSION_GRANTED) {
         ActivityCompat.requestPermissions(mActivity,
                 new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                 MY_PERMISSIONS_REQUEST_CAMERA_IMAGEVIEW);
      } else {
         //Permission Already Granted
         CallCameraToCapturePic();
      }
   }
   private void CallCameraToCapturePic(){
      Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      File pictureFile = null;

      pictureFile = createPhotoFromCamera();

      if (pictureFile != null) {
         Uri photoURI = FileProvider.getUriForFile(this,
                 "com.qdegrees.doyoursurvey.provider",
                 pictureFile);
         cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
         startActivityForResult(cameraIntent,IMAGE_BROWSE_CAMERA_CODE);
      }else{
         Toast.makeText(this,
                 "Photo file can't be created, please try again",
                 Toast.LENGTH_SHORT).show();
      }
   }
   private File  createPhotoFromCamera(){
      try {
         String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
         String pictureFile = "DYSCamera" + "_" + timeStamp;
         File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
         File image = File.createTempFile(pictureFile, ".jpg", storageDir);
         CameraImagePath = image.getAbsolutePath();

         Log.e("CameraImagePath",CameraImagePath);
         return image;
      }catch (IOException e){
         e.printStackTrace();
         return null;
      }
   }
   private void onBrowseImageClick() {
      if (ContextCompat.checkSelfPermission(mActivity,
              Manifest.permission.READ_EXTERNAL_STORAGE)
              != PackageManager.PERMISSION_GRANTED) {
         ActivityCompat.requestPermissions(mActivity,
                 new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                 MY_PERMISSIONS_REQUEST_IMAGEVIEW);
      } else {
         //Permission Already Granted
         browseQuestionImageView();
      }
   }
   private void browseQuestionImageView() {
      if (Environment.getExternalStorageState().equals("mounted")) {
         Intent intent = new Intent();
         intent.setType("image/*");
         intent.setAction(Intent.ACTION_PICK);
         startActivityForResult(
                 Intent.createChooser(
                         intent,
                         "Select Photo"),
                 IMAGE_BROWSE_CODE);
      }
   }
   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      switch(requestCode){
            case IMAGE_BROWSE_CODE:
               if(resultCode==RESULT_OK){
                  Uri SelectedImageUrl = data.getData();

                  if(SelectedImageUrl!=null){
                     String selectedImagePath = URIPath.getPath(mActivity, SelectedImageUrl);
                     Log.e("Imagepath",selectedImagePath);
                     File imgFile = new  File(selectedImagePath);
                     if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ProfileImage.setImageBitmap(myBitmap);
                        UploadProfileImage(selectedImagePath);
                     }




                  }else{
                     Log.e("URINULL",SelectedImageUrl.toString());
                  }

               }
               break;
         case IMAGE_BROWSE_CAMERA_CODE:
            if(resultCode == RESULT_OK){
               File imageFile=new File(CameraImagePath);
               if(imageFile.exists()){
                     Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                     ProfileImage.setImageBitmap(myBitmap);
                     UploadProfileImage(CameraImagePath);
               }
            }
            break;
      }
   }
   /****************Permissions************************/
   private ArrayList findUnAskedPermissions(ArrayList wanted) {
      ArrayList result = new ArrayList();

      for (int i=0;i<wanted.size();i++){
         String perm=wanted.get(i).toString();
         if(!hasPermission(perm)){
            result.add(perm);
         }

      }
      return result;
   }
   private boolean hasPermission(String permission) {
      if (canMakeSmores()) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
         }
      }
      return true;
   }
   private boolean canMakeSmores() {
      return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
   }
   @TargetApi(Build.VERSION_CODES.M)
   @Override
   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      switch (requestCode) {

         case ALL_PERMISSIONS_RESULT:
            for (int i = 0; i < permissionsToRequest.size(); i++) {
               if (!hasPermission(permissionsToRequest.get(i).toString())) {
                  permissionsRejected.add(permissionsToRequest.get(i).toString());
               }
            }
               /* for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }*/

            if (permissionsRejected.size() > 0) {


               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                  if (shouldShowRequestPermissionRationale(permissionsRejected.get(0).toString())) {
                     showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                             new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                   String[] permArray = new String[permissionsRejected.size()];
                                   for (int i = 0; i < permissionsRejected.size(); i++) {
                                      permArray[i] = permissionsRejected.get(i).toString();

                                   }
                                   requestPermissions(permArray, ALL_PERMISSIONS_RESULT);

                                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }*/
                                }
                             });
                     return;
                  }
               }

            } else {

            }

            break;
      }

   }
   private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
      new AlertDialog.Builder(mActivity)
              .setMessage(message)
              .setPositiveButton("OK", okListener)
              .setNegativeButton("Cancel", null)
              .create()
              .show();
   }

   private void showUploadProgress(){
      fileUploadDialog =new BottomSheetDialog(mActivity,R.style.SheetDialog);
      fileUploadDialog.setContentView(R.layout.dialog_upload_profile_image);
      fileUploadDialog.setCancelable(false);
      fileUploadProgress = (ProgressBar) fileUploadDialog.findViewById(R.id.pbFileUpload);
      fileUploadTitle = (AppCompatTextView) fileUploadDialog.findViewById(R.id.tvFileUploadHeading);
      fileUploadper =(AppCompatTextView) fileUploadDialog.findViewById(R.id.tvFileUploadPercentage);
   }

   private  void updateProfileImage(){
      if(Utility.isNetworkAvailable(mActivity)){
         Utility.showDialog(mActivity);
         UpdateProfilePostData pData = new UpdateProfilePostData(emailStr,mobileStr,fNameStr,lNameStr,genderStr,dobStr,cityStr,bioStr,referalStr,uploadedImagePath,faceBookId,googleIdStr);
         apiService.updateProfile(pData).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
               try{
                  UpdateProfileResponse updateProfileResponse=response.body();
                  int code = updateProfileResponse.status;
                  String message = updateProfileResponse.message;
                  if(code==200){
                     Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                     Utility.hideDialog(mActivity);
                     onBackPressed();

                  }else{
                     Utility.showAlertDialog(mActivity, "Error!!", message);
                     Utility.hideDialog(mActivity);
                  }



               }catch (Exception e){
                  e.printStackTrace();
                  Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                  Utility.hideDialog(mActivity);
               }

            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
               Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
               Utility.hideDialog(mActivity);
            }
         });
      }else{
         Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
      }
   }
   private void UploadProfileImage(String filePath){
      ProgressRequestBody fileBody= new ProgressRequestBody(new File(filePath),"image",this);
      if(Utility.isNetworkAvailable(mActivity)) {
         fileUploadTitle.setText(getResources().getString(R.string.Uploading_profile));
         fileUploadDialog.show();
         fileUploadProgress.setProgress(0);
         apiService.uploadProfileImage(UserIdStr,RetrofitAPIClient.getImageFilePartProgress(fileBody)).enqueue(new Callback<ProfileUploadResponse>() {
            @Override
            public void onResponse(Call<ProfileUploadResponse> call, Response<ProfileUploadResponse> response) {
               try{
                  ProfileUploadResponse uploadResponse = response.body();
                  int code = uploadResponse.status;
                  String message = uploadResponse.message;
                  if(code==200){
                     Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                     uploadedImagePath = uploadResponse.data;
                     fileUploadDialog.dismiss();
                  }else{
                     Utility.showAlertDialog(mActivity, "Error!!", message);
                     fileUploadDialog.dismiss();
                  }

               }catch (Exception e){
                  e.printStackTrace();
                  Toast.makeText(mActivity, "Something went wrong!! Please Try Again.", Toast.LENGTH_SHORT).show();
                  fileUploadDialog.dismiss();
               }
            }

            @Override
            public void onFailure(Call<ProfileUploadResponse> call, Throwable t) {

            }
         });




      }else{
         Utility.showAlertDialog(mActivity, mActivity.getString(R.string.alert), mActivity.getString(R.string.no_internet_connection));
      }

   }
   @Override
   public void onProgressUpdate(int percentage) {
      fileUploadProgress.setProgress(percentage);
      fileUploadper.setText(percentage+" %");
   }
   @Override
   public void onError() {
      Toast.makeText(mActivity, "Uploading Failed!!", Toast.LENGTH_SHORT).show();

   }
   @Override
   public void onFinish() {
      fileUploadProgress.setProgress(100);
      fileUploadper.setText("Uploaded 100%");
   }

   @Override
   public void onBackPressed() {
      super.onBackPressed();
      finish();
   }
}
