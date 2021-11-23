package vn.embosua.ltddquanlynhanvienversion4.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.DangNhap.LogInActivity;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.FbManeger;
import vn.embosua.ltddquanlynhanvienversion4.Model.ImageConvert;
import vn.embosua.ltddquanlynhanvienversion4.Model.ImageHelper;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class UserSettingFragment extends Fragment{

    View view;
    Button btSignOut, btFeedBack;
    ImageView imgUser;
    TextView txtName, txtEmail;
    String ID;

    TextView txtTermsAndServices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_setting, container, false);

        getLinkViews();

        QueryStaffInFo("dbUser",ID);

        getControls();

        return view;
    }

    private void getControls() {
        // đăng xuất
        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSignOut();
            }
        });

        //
        btFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));

                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"nguyenmanhhung23022001@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"Andor App Feedback");
                intent.putExtra(Intent.EXTRA_TEXT,"Hello");

                if (intent.resolveActivity(getContext().getPackageManager()) != null){
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "No App is Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // link giới thiệu
        txtTermsAndServices.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtTermsAndServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://huadang27.github.io/dieukhoan/?fbclid=IwAR0pH4AotfreROu3YkuPW4uOWi9W208RHGRE8N16AGsF10ZsY8tI4fHRypg")));
            }
        });
    }

    // hỏi xác nhận đăng xuất
    private void openDialogSignOut(){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_error_dialog)
                .setTitle("Do you want to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AGConnectAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), LogInActivity.class));
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(true)
                .show();

    }

    // liên kết các đối tượng trên view
    private void getLinkViews() {
        btSignOut = view.findViewById(R.id.bt_sign_out);
        btFeedBack = view.findViewById(R.id.bt_feedback);
        imgUser = view.findViewById(R.id.img_user_photo);
        txtName = view.findViewById(R.id.txt_name_staff);
        txtEmail = view.findViewById(R.id.txt_email);
        ID = AGConnectAuth.getInstance().getCurrentUser().getUid();
        txtTermsAndServices = view.findViewById(R.id.txt_the_terms_and_services);
    }

    // lấy thông tin chủ tài khoản để hiển thị
    public void QueryStaffInFo(String DB, String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DB).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Staff staff = snapshot.getValue(Staff.class);
                            txtEmail.setText(staff.getEmail());
                            txtName.setText(staff.getFullName());
                            imgUser.setImageBitmap(new ImageConvert().toBitmap(staff.getPhoto()));
                        }else {
                            Toast.makeText(getContext(), "Load Data Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getContext(), "Load Data Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}