package com.brianml31.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.brianml31.insta_moon.utils.DialogUtils;
import com.brianml31.instamoon.R;
import com.instagram.mainactivity.InstagramMainActivity;

public class MainActivity extends InstagramMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("InstaMoon");


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Open menu
        Button btnOpenMenu = findViewById(R.id.btnOpenMenu);
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = v.getContext();
                DialogUtils.Companion.showInstaMoonOptionsDialog(ctx, MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        com.brianml31.insta_moon.InstagramMainActivity.Companion.after_onActivityResult(this, requestCode, resultCode, data);

    }
}