package vn.embosua.ltddquanlynhanvienversion4.DangNhap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.DieuKien.CheckFormat;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.FormatCallBack;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.AGCManager;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.FbManeger;
import vn.embosua.ltddquanlynhanvienversion4.ManHinhChinh.AdminActivity;
import vn.embosua.ltddquanlynhanvienversion4.ManHinhChinh.UserActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.QuenMatKhau.EnterEmailActivity;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class LogInActivity extends PortraitActivity implements AGCManager.SigInCallBack , FbManeger.FBCallBack, FormatCallBack {
    String TAG = "LogInActivity";
    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_NEW_PASS = 2002;

    TextInputLayout tilEmail, tilPassword;
    EditText edtEmail, edtPassword;
    Button btForgetPassword, btGo;
    String uEmail, uPass;

    CheckBox cbTermsAndServices;
    TextView txtTermsAndServices;

    ProgressDialog progressDialog;

    AGCManager agcManager;
    FbManeger fbManeger;
    CheckFormat checkFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getLinkViews();

        agcManager.addSigInCallBack(LogInActivity.this);

        getControls();
    }

    private void getControls() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                checkFormat.addCallBack(LogInActivity.this);
                    if (checkFormat.checkLogin(uEmail,EMAIL,uPass,PASSWORD))
                        if (cbTermsAndServices.isChecked()) {
                            progressDialog = ProgressDialog.show(LogInActivity.this, "Sign In",
                                    "Please wait !!!", true);
                            progressDialog.show();
                            agcManager.signInWitEmailAndPassword(uEmail, uPass);
                        } else
                            openDialogTermsAndServices();
            }
        });

        btForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultLauncher.launch(new Intent(LogInActivity.this, EnterEmailActivity.class));
            }
        });

        txtTermsAndServices.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtTermsAndServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://huadang27.github.io/dieukhoan/?fbclid=IwAR0pH4AotfreROu3YkuPW4uOWi9W208RHGRE8N16AGsF10ZsY8tI4fHRypg")));
            }
        });

//        btCreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LogInActivity.this, PersonInforActivity.class));
//                //Toast.makeText(LogInActivity.this, "Tao tai khoan", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void getDataFromView() {
        uEmail = edtEmail.getText().toString().trim();
        uPass = edtPassword.getText().toString().trim();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == FINISH_ACTIVITY){
                        uEmail = (String) result.getData().getExtras().get("email");
                        uPass = (String) result.getData().getExtras().get("pass");
                        edtEmail.setText(uEmail);
                        edtPassword.setText(uPass);
                        //finish();
                    }
                }
            });

    // khi nhan thoat ra khoi man hinh
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if((keyCode ==KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertDialogBuiler = new AlertDialog.Builder(this);
            alertDialogBuiler.setMessage("Do you want to escape?");
            alertDialogBuiler.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            alertDialogBuiler.setNegativeButton("No", new DialogInterface.OnClickListener() {@Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
            });
            AlertDialog alertDialog = alertDialogBuiler.create();
            alertDialog.show();
            return true;
        }else if ((keyCode==KeyEvent.KEYCODE_MENU)){
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void getLinkViews() {
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        //btCreate = findViewById(R.id.bt_create);
        cbTermsAndServices = findViewById(R.id.cb_the_terms_and_services);
        txtTermsAndServices = findViewById(R.id.txt_the_terms_and_services);
        btForgetPassword = findViewById(R.id.bt_forget_password);
        btGo = findViewById(R.id.bt_go);
        agcManager = new AGCManager();
        fbManeger = new FbManeger();
        checkFormat = new CheckFormat();
    }

    @Override
    public void SinginSuccess(String id) {
        Log.e(TAG,"Sig In Success");
        fbManeger.addCallBacks(LogInActivity.this);
        fbManeger.QueryStaffInFo("dbUser",id);
    }

    @Override
    public void SinginFail(String e) {
        progressDialog.dismiss();
        Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,""+e);
    }

    @Override
    public void QueryStaffInFoSuccess(Staff staff) {
        progressDialog.dismiss();
        Log.e(TAG,"Query Success: "+staff.getId());
        if (staff.getStaff()){
            if (staff.getAccessRight()){
                startActivity(new Intent(LogInActivity.this, AdminActivity.class));
                finish();
            }else {
                startActivity(new Intent(LogInActivity.this, UserActivity.class));
                finish();
            }
        }else{
            Toast.makeText(this, "No staff members", Toast.LENGTH_SHORT).show();
            AGConnectAuth.getInstance().signOut();
        }
    }

    @Override
    public void QueryStaffInFoFail(String e) {
        progressDialog.dismiss();
        Toast.makeText(this, "Query Fail: "+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Query Fail: "+e);
    }

    @Override
    public void QueryCalenderSuccess(int off, int work, List<String> list) {

    }

    // ẩn bàn phím khi nhấn ngoài mành hình
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private void openDialogTermsAndServices(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        new AlertDialog.Builder(LogInActivity.this)
                .setIcon(R.drawable.ic_error_dialog)
                .setTitle("Terms And Services")
                .setMessage("You need to agree to the terms and services to use this app.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cbTermsAndServices.setChecked(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("Details", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://huadang27.github.io/dieukhoan/?fbclid=IwAR0pH4AotfreROu3YkuPW4uOWi9W208RHGRE8N16AGsF10ZsY8tI4fHRypg")));
                    }
                })
                .setCancelable(true)
                .show();
    }

    @Override
    public void FormatTrue(String check) {
        switch (check){
            case EMAIL: tilEmail.setHelperText(null); break;
            case PASSWORD: tilPassword.setHelperText(null); break;
        }
    }

    @Override
    public void FormatFail(String messenger, String check) {
        switch (check){
            case EMAIL: tilEmail.setHelperText(messenger); break;
            case PASSWORD: tilPassword.setHelperText(messenger); break;
        }
    }
}