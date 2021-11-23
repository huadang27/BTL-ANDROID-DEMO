package vn.embosua.ltddquanlynhanvienversion4.DangNhap;

import androidx.appcompat.app.AppCompatActivity;

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

import vn.embosua.ltddquanlynhanvienversion4.DuLieu.FbManeger;
import vn.embosua.ltddquanlynhanvienversion4.ManHinhChinh.AdminActivity;
import vn.embosua.ltddquanlynhanvienversion4.ManHinhChinh.UserActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class SilentLoginActivity extends PortraitActivity implements FbManeger.FBCallBack {
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

    private void getMoveAnimation() {
        logo.setAnimation(topAnim);
        nameApp.setAnimation(bottomAnim);
        source.setAnimation(bottomAnim);

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
        }, 1500);
    }

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
            Toast.makeText(this, "No staff members", Toast.LENGTH_SHORT).show();
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