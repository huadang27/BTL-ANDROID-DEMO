package com.hqdang.baitaplonversion1.DangNhap;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.hmf.tasks.Continuation;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

import java.util.List;

import com.hqdang.baitaplonversion1.DuLieu.FbManeger;
import com.hqdang.baitaplonversion1.ManHinhChinh.AdminActivity;
import com.hqdang.baitaplonversion1.ManHinhChinh.UserActivity;
import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

public class SilentLoginActivity extends AppCompatActivity implements FbManeger.FBCallBack {
    String TAG = "SilentLoginActivity";

    Animation topAnim, bottomAnim;
    ImageView logo;
    TextView nameApp, source;

    public String email = null, uID = null;
    AGConnectUser user;

    FbManeger fbManeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silent_login);

        user = AGConnectAuth.getInstance().getCurrentUser();

        if(user==null){
            silentLogIns();
        } else{
            email = user.getEmail();
            uID = user.getUid();
        }

        getLinkViews();

        getLinkAnimations();

        getMoveAnimation();
    }
// đăng nhập im lặng
    private void silentLogIns() {
        AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams();
        AccountAuthService service = AccountAuthManager.getService(this, authParams);
        Task<AGConnectAuth> task = service.silentSignIn().continueWithTask(new Continuation<AuthAccount, Task<AGConnectAuth>>() {
            @Override
            public Task<AGConnectAuth> then(Task<AuthAccount> task) throws Exception {
                return null;
            }
        });
        task.addOnSuccessListener(new OnSuccessListener<AGConnectAuth>() {

            @Override
            public void onSuccess(AGConnectAuth agConnectAuth) {
                email = agConnectAuth.getCurrentUser().getEmail();
                Log.e(TAG, "user email [" + email+"]");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG,e.getMessage());
            }
        });
    }
// chuyển động lên xuống của logo
    private void getMoveAnimation() {
        logo.setAnimation(topAnim);
        nameApp.setAnimation(bottomAnim);
        source.setAnimation(bottomAnim);
// đếm thời cho trang Slient chạy trong 3s
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (email!=null) {
                    Log.e(TAG,"email: "+email);
                    //checkAdmin();
                    fbManeger.addCallBacks(SilentLoginActivity.this);
                    fbManeger.QueryStaffInFo("dbUser",uID);
                    // QueryStaffInFoSuccess(); dieu kien dang nhap
                }else{
                    StartLogInActivity();
                }
            }
        }, 3000);
    }

    // nếu tk chưa đăng nhập thì logo silent chuyển sang login
    public void StartLogInActivity(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SilentLoginActivity.this,LogInActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(logo,"logo_image");
                pairs[1] = new Pair<View,String>(nameApp,"logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SilentLoginActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        }, 3000L );
    }
// chuyển động lên xuống
    private void getLinkAnimations() {
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
    }

    private void getLinkViews() {
        logo = findViewById(R.id.logo);
        nameApp = findViewById(R.id.name_app);
        source = findViewById(R.id.source);
        fbManeger = new FbManeger();
    }
//kiểm tra là admin hay user
    @Override
    public void QueryStaffInFoSuccess(Staff staff) {
        //Toast.makeText(this, "Query Success: "+staff.getId(), Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Query Success: "+staff.getId());
        if (staff.getStaff()){
            if (staff.getAccessRight()){
                startActivity(new Intent(SilentLoginActivity.this, AdminActivity.class));
                finish();
            }else {
                startActivity(new Intent(SilentLoginActivity.this, UserActivity.class));
                finish();
            }
        }else{
            Toast.makeText(this, "Khong phai nhan vien", Toast.LENGTH_SHORT).show();
            AGConnectAuth.getInstance().signOut();
            StartLogInActivity();
        }
    }

    @Override
    public void QueryStaffInFoFail(String e) {
        Toast.makeText(this, "Query Fail: "+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Query Fail: "+e);
    }

    @Override
    public void QueryCalenderSuccess(int off, int work, List<String> list) {

    }
}