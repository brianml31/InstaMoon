package com.brianml31.mainactivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.brianml31.instamoon.handlers.LongClickMenuHandler;
import com.brianml31.insta_moon.R;
import com.brianml31.instamoon.utils.BackupManager;
import com.brianml31.instamoon.utils.FontUtils;
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

        //Button open menu
        Button btnOpenMenu = findViewById(R.id.btnOpenMenu);
        LongClickMenuHandler.Companion.setLongClickMenuHandler(MainActivity.this, btnOpenMenu);

        //Button force crash
        Button btnForceCrash = findViewById(R.id.btnForceCrash);
        btnForceCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("This is a test crash for ACRA");
            }
        });

        //EditText custom font
        EditText editTextCustomFont = findViewById(R.id.editTextCustomFont);
        Typeface font = FontUtils.Companion.getCustomFont(Typeface.DEFAULT);
        editTextCustomFont.setTypeface(font);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BackupManager.Companion.after_onActivityResult(this, requestCode, resultCode, data);

    }
}