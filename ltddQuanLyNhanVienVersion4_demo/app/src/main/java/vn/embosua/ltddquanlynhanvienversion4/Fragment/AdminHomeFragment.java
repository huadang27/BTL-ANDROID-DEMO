package vn.embosua.ltddquanlynhanvienversion4.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.embosua.ltddquanlynhanvienversion4.QRCode.ScanCodeActivity;
import vn.embosua.ltddquanlynhanvienversion4.R;
import vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan.PersonInforActivity;

public class AdminHomeFragment extends Fragment {

    View view;
    Button btCreat, btqr, btDepartment, btCreatDepartment;
    EditText edtDepartment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        getLinkViews();

        getControls();

        return view;
    }

    private void getControls() {
        // tạo tài khoản mới
        btCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PersonInforActivity.class));
            }
        });

        // tạo mã qr
        btqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ScanCodeActivity.class));
            }
        });

        btDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCreateNewDepartment();
            }
        });
    }

    // liên kết với các đối tượng trên view
    private void getLinkViews() {
        btDepartment = view.findViewById(R.id.bt_create_department);
        btCreat = view.findViewById(R.id.bt_create);
        btqr = view.findViewById(R.id.bt_qr);
    }

    public void DialogCreateNewDepartment(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_department);
        
        Window window = dialog.getWindow();
        if (window == null) return;

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        
        edtDepartment = dialog.findViewById(R.id.edt_name_department);
        btCreatDepartment = dialog.findViewById(R.id.bt_create);
        
        btCreatDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtDepartment.getText().toString().trim();
                if (name.isEmpty() || name == null){
                    Toast.makeText(getContext(), "Not be empty" + name, Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("dbRoom").push().setValue(name);
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        dialog.show();
    }
}