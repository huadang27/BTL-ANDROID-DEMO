package vn.embosua.ltddquanlynhanvienversion4.QuenMatKhau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class ResetPassSuccessActivity extends PortraitActivity {
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