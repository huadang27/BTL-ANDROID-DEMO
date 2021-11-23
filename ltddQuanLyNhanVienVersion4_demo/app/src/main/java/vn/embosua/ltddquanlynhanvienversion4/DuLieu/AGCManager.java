package vn.embosua.ltddquanlynhanvienversion4.DuLieu;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_REGISTER_LOGIN;

public class AGCManager {

    FbManeger fbManeger;

    private CreateAccountCallBack createAccountCallBack = CreateAccountCallBack.DEFAULT;

    public void addCallBacks(CreateAccountCallBack createAccountCallBack) {
        this.createAccountCallBack = createAccountCallBack;
    }

    private SigInCallBack sigInCallBack = SigInCallBack.DEFAULT;

    public void addSigInCallBack(SigInCallBack sigInCallBack){
        this.sigInCallBack = sigInCallBack;
    }

    public AGCManager() {
    }

    public void sendConfirmCode(String Email, int ACTION) {
        VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                .action(ACTION)   //ACTION_REGISTER_LOGIN/ACTION_RESET_PASSWORD
                .sendInterval(30)
                .build();

        Task<VerifyCodeResult> task = AGConnectAuth.getInstance().requestVerifyCode(Email, settings);
        task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
            @Override
            public void onSuccess(VerifyCodeResult verifyCodeResult) {
                createAccountCallBack.AfterSendSuccess();
            }
        }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e.getMessage().equals(" code: 203818032 message: incorrect user name or password.")){
                    createAccountCallBack.AfterSendFail("Account does not exist");
                }else {
                    createAccountCallBack.AfterSendFail(e.getMessage());
                }
            }
        });
    }

    public void creatWithEmail(String Email, String VerCode, String Pass, String DB, Staff staff){
        fbManeger = new FbManeger();

        EmailUser emailUser = new EmailUser.Builder().setEmail(Email) // dat ca tham so cho emailuser
                .setVerifyCode(VerCode)
                .setPassword(Pass).build();

        AGConnectAuth.getInstance()
                .createUser(emailUser)
                .addOnCompleteListener(new OnCompleteListener<SignInResult>() {
                    @Override
                    public void onComplete(Task<SignInResult> task) {
                        if (task.isSuccessful()) {
                            AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
                            String id = user.getUid();

                            staff.setId(id);

                            fbManeger.writeNewStaffOnFB(DB,id,staff);

                            fbManeger.CreateTimesheets(id);

                            createAccountCallBack.AfterCreateAccountSuccess();
                        } else {
                            createAccountCallBack.AfterCreateAccountFail(task.getException().getMessage());
                        }
                    }
                });
    }

    public void signInWitEmailAndPassword(String emai, String password) {
        AGConnectAuthCredential credential = EmailAuthProvider.credentialWithPassword(emai, password);
        AGConnectAuth.getInstance()
                .signIn(credential)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        // Lấy id nhân viên
                        String ID = AGConnectAuth.getInstance().getCurrentUser().getUid();
                        sigInCallBack.SinginSuccess(ID);
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("dbUser").child(ID).child("password").setValue(password);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Tạo nhận biết nỗi xảy ra khi đăng nhập

                        if (e.getMessage().equals(" code: 203818032 message: incorrect user name or password.")){
                            sigInCallBack.SinginFail("incorrect user name or password");
                        }else {
                            sigInCallBack.SinginFail(e.getMessage());
                        }
                    }
                });

    }

    public void resetPasswordWithEemail(String email, String pass, String vercode){
        Task<Void> task = AGConnectAuth.getInstance().resetPassword(email,pass,vercode);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                createAccountCallBack.ResetPassSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                createAccountCallBack.ResetPassFail(e.getMessage());
            }
        });
    }

    public  interface CreateAccountCallBack{
        CreateAccountCallBack DEFAULT = new CreateAccountCallBack(){

            @Override
            public void AfterSendSuccess() {
                Log.e("","Send verCode Success");
            }

            @Override
            public void AfterSendFail(String e) {
                Log.e("","Send verCode Fail: "+e);
            }

            @Override
            public void AfterCreateAccountSuccess() {
                Log.e("","Create Account Success");
            }

            @Override
            public void AfterCreateAccountFail(String e) {
                Log.e("","Create Account Fail: "+e);
            }

            @Override
            public void ResetPassSuccess() {

            }

            @Override
            public void ResetPassFail(String e) {

            }
        };

        void AfterSendSuccess();
        void AfterSendFail(String e);
        void AfterCreateAccountSuccess();
        void AfterCreateAccountFail(String e);
        void ResetPassSuccess();
        void ResetPassFail(String e);
    }

    public interface SigInCallBack{
        SigInCallBack DEFAULT = new SigInCallBack() {
            @Override
            public void SinginSuccess(String id) {

            }

            @Override
            public void SinginFail(String e) {

            }
        };
        void SinginSuccess(String id);
        void SinginFail(String e);
    }
}
