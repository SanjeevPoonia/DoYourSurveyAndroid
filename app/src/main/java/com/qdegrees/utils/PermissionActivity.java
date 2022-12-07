package com.qdegrees.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends Activity {
   List<String> perms= new ArrayList<>();
   String[] pams;

   String EXTRA_DATA = "extra_data";
   String EXTRA_RESULT = "extra_result";
   int RC_PERM = 111;
   int RC_PERM_ACT = 112;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setResult(Activity.RESULT_CANCELED);
      perms=getIntent().getStringArrayListExtra(EXTRA_DATA);
      if(hasPermissions(perms) ||Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
         returnResult();
      }else{
         for(int i=0;i<perms.size();i++){
            pams[i]=perms.get(i);
         }
         requestPermissions(pams,RC_PERM);
      }

   }
   private void returnResult() {
      Intent intent = new Intent();
      intent.putExtra(EXTRA_RESULT, hasPermissions(perms));
      setResult(Activity.RESULT_OK, intent);
      finish();
   }
   public boolean hasPermissions(List<String> perm){
      boolean result=true;
      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
         for(int i=0;i<perm.size();i++){
            if(checkSelfPermission(perm.get(i))== PackageManager.PERMISSION_GRANTED){
               result=true;
            }else{
               result=false;
               break;
            }
         }
      }
      return result;
   }

   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      returnResult();
   }
}
