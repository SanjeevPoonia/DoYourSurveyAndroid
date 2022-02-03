package com.qdegrees.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.qdegrees.doyoursurvey.R;
import com.qdegrees.doyoursurvey.databinding.LayoutProgressDialogBinding;


public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutProgressDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_progress_dialog, null, false);
       /* binding.webView.loadUrl("file:///android_asset/progress_logo.svg");
        binding.card.setShapeAppearanceModel(ShapeAppearanceModel.builder().build());*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        setCancelable(false);
        ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);
        getWindow().setBackgroundDrawable(drawable);
    }
}
