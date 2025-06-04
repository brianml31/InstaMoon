package com.brianml31.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.brianml31.instamoon.LongClickMenuHandler;
import com.brianml31.insta_moon.R;
import com.instagram.mainactivity.InstagramMainActivity;

public class MainActivity extends InstagramMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Open menu
        Button btnOpenMenu = findViewById(R.id.btnOpenMenu);
        LongClickMenuHandler.Companion.setLongClickMenuHandler(MainActivity.this, btnOpenMenu);

        //Crash
        Button btnForceCrash = findViewById(R.id.btnForceCrash);
        btnForceCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("This is a test crash for ACRA");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        com.brianml31.instamoon.InstagramMainActivity.Companion.after_onActivityResult(this, requestCode, resultCode, data);

    }
}