package com.brianml31.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.brianml31.insta_moon.R;
import com.winsontan520.OnReceiveListener;
import com.winsontan520.WVersionManager;

public class CheckVersionActivity extends AppCompatActivity {
    protected static final String TAG = "CheckVersionActivity";

    private EditText versionContentUrl;
    private EditText updateNowLabel;
    private EditText remindMeLaterLabel;
    private EditText ignoreThisVersionLabel;
    private EditText reminderTimer;
    private CheckBox checkBoxCallback;
    private CheckBox checkBoxUseDefaultDialog;
    private Button checkVersionButton;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.check_version);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        versionContentUrl = (EditText) findViewById(R.id.versionContentUrl);
        updateNowLabel = (EditText) findViewById(R.id.updateNowLabel);
        remindMeLaterLabel = (EditText) findViewById(R.id.remindMeLaterLabel);
        ignoreThisVersionLabel = (EditText) findViewById(R.id.ignoreThisVersionLabel);
        reminderTimer = (EditText) findViewById(R.id.reminderTimer);
        checkBoxCallback = (CheckBox) findViewById(R.id.checkBoxWithCallback);
        checkBoxUseDefaultDialog = (CheckBox) findViewById(R.id.checkBoxUseDefaultDialog);
        checkVersionButton = (Button) findViewById(R.id.check_version_button);
        checkVersionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVersion();
            }
        });
    }

    private void checkVersion() {
        WVersionManager versionManager = new WVersionManager(this);
        versionManager.setVersionContentUrl(versionContentUrl.getText().toString());
        versionManager.setUpdateNowLabel(updateNowLabel.getText().toString());
        versionManager.setRemindMeLaterLabel(remindMeLaterLabel.getText().toString());
        versionManager.setIgnoreThisVersionLabel(ignoreThisVersionLabel.getText().toString());
        versionManager.setReminderTimer(Integer.valueOf(reminderTimer.getText().toString()));
        // implement own listener callback handler if is checked
        if (checkBoxCallback.isChecked()) {
            // set own listener
            versionManager.setOnReceiveListener(new OnReceiveListener() {
                @Override
                public boolean onReceive(int status, String result) {
                    Log.d(TAG, "status = " + status);
                    Log.d(TAG, "result = " + result);
                    if(mProgressDialog != null){
                        mProgressDialog.cancel();
                    }
                    if(!checkBoxUseDefaultDialog.isChecked()){
                        showMyOwnDialog(status, result);
                    }
                    // return true to use default dialog
                    return checkBoxUseDefaultDialog.isChecked();
                }
            });
            mProgressDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true, false);
        }
        versionManager.checkVersion();
    }

    protected void showMyOwnDialog(int status, String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Status " + status);
        builder.setMessage("Result: \n" + result);
        if(status != 200){
            builder.setTitle("Ops...");
            builder.setMessage("Something wrong...");
        }
        builder.create().show();
    }
}
