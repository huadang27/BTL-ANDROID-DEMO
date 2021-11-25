package com.hqdang.baitaplonversion1.QuenMatKhau;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hqdang.baitaplonversion1.R;

public class ResetPassSuccessActivity extends AppCompatActivity {
    static final int FINISH_ACTIVITY = 1001;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_success);

        getLinkViews();

        getControls();
    }

    private void getControls() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(FINISH_ACTIVITY);
                finish();
            }
        });
    }

    private void getLinkViews() {
        btLogin = findViewById(R.id.bt_login);
    }
}