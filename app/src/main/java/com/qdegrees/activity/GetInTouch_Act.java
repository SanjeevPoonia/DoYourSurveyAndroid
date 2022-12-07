package com.qdegrees.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qdegrees.doyoursurvey.R;

public class GetInTouch_Act extends AppCompatActivity {
    ImageView ivBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_getintouch);
        ivBack=findViewById(R.id.iv_getTouchBack);
        ivBack.setOnClickListener(v->{
            onBackPressed();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
