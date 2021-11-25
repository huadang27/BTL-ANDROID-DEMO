package com.hqdang.baitaplonversion1.QuenMatKhau;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.hqdang.baitaplonversion1.R;

public class EnterEmailActivity extends AppCompatActivity {

    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_NEW_PASS = 2002;

    Button btNext;
    EditText edtEmail;
    String uEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        getLinkViews();

        getControls();
    }

    private void getControls() {
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uEmail = edtEmail.getText().toString().trim();
                if (!checkData(uEmail)){
                    Intent intent = new Intent(EnterEmailActivity.this,ResetPassActivity.class);
                    intent.putExtra("email",uEmail);
                    resultLauncher.launch(intent);
                }

            }
        });
    }

    private boolean checkData(String abc){
        if (abc.isEmpty() || abc == null){
            return true;
        }else{
            return false;
        }
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == FINISH_ACTIVITY){
                        //staff = (Staff) result.getData().getExtras().get("staff");
                        String email = (String) result.getData().getExtras().get("email");
                        String pass = (String) result.getData().getExtras().get("pass");
                        Intent intent = new Intent();
                        intent.putExtra("email",email);
                        intent.putExtra("pass",pass);
                        setResult(FINISH_ACTIVITY,intent);
                        finish();
                    }
                }
            });

    private void getLinkViews() {
        btNext = findViewById(R.id.bt_next);
        edtEmail = findViewById(R.id.edt_email);
    }
}