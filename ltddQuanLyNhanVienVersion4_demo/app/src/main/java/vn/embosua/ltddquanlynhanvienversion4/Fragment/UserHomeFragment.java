package vn.embosua.ltddquanlynhanvienversion4.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import vn.embosua.ltddquanlynhanvienversion4.DanhSach.ListStaffActivity;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.FbManeger;
import vn.embosua.ltddquanlynhanvienversion4.QRCode.ScanCodeActivity;
import vn.embosua.ltddquanlynhanvienversion4.R;
import vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan.PersonInforActivity;
import vn.embosua.ltddquanlynhanvienversion4.TinhCong.PayRollActivity;

public class UserHomeFragment extends Fragment {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    String TAG = "UserHomeFragment";

    View view;
    Button btPayRoll, btGenQR;
    String uID = AGConnectAuth.getInstance().getCurrentUser().getUid();
    Bitmap bmImgQr = null;

    FbManeger fbManeger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_home, container, false);

        getLinkViews();

        getControls();

        return view;
    }

    private void getControls() {
        // chuyển sang acivity tính tương
        btPayRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PayRollActivity.class));
            }
        });

        // tạo mã qr
        btGenQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                openDialogCreate();
            }
        });
    }

    // liên kết với các đối tượng trên view
    private void getLinkViews() {
        btPayRoll = view.findViewById(R.id.bt_payroll);
        btGenQR = view.findViewById(R.id.bt_gen_qr);
        fbManeger = new FbManeger();
    }

    // kiểm tra xin cấp quyền
    private void checkPermissions(){
        requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_EXTERNAL_STORAGE);
    }

    @Override // kết quả xin cấp quyền trả về
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length < 2 || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[0] != PackageManager.PERMISSION_GRANTED || requestCode != REQUEST_EXTERNAL_STORAGE) {
            Toast.makeText(getContext(), "Need permission memory access", Toast.LENGTH_SHORT).show();
        }
    }

    // open dialog tạo mã vạch
    private void openDialogCreate(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_qr_code);

        Window window = dialog.getWindow();
        if (window == null) return;

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        Button btSave =  dialog.findViewById(R.id.bt_save);
        ImageView imgQR =  dialog.findViewById(R.id.img_qr_code);

        CreateQRCode();

        if(uID == "" || uID.isEmpty() || bmImgQr == null){
            imgQR.setImageResource(R.drawable.person_500px);
        }else {
            imgQR.setImageBitmap(bmImgQr);
            CreateTimesheets();
        }

        // lưu ảnh qr code
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCodeBtnClick(dialog);
            }
        });

        dialog.show();
    }

    // tạo bảng chấm công
    private void CreateTimesheets() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth ==1){
            fbManeger.CreateTimesheets(uID);
        }

    }

    // tạo qr code
    private void CreateQRCode(){
        try {
            HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator().setBitmapMargin(1).setBitmapColor(Color.BLACK).setBitmapBackgroundColor(Color.WHITE).create();
            bmImgQr = ScanUtil.buildBitmap(uID, HmsScan.QRCODE_SCAN_TYPE, 400, 400, options);
        } catch (WriterException e) {
            Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    // lưu qr code
    private void saveCodeBtnClick(Dialog dialog) {
        if (bmImgQr == null) {
            Toast.makeText(getContext(),"No QR code yet", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            String fileName = System.currentTimeMillis() + ".jpeg";
//            String storePath;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
//                storePath= getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
//            }
//            else
//            {
//                storePath= Environment.getExternalStorageDirectory().getAbsolutePath();
//            }
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File file = new File(appDir, fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            boolean isSuccess = bmImgQr.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uri = Uri.fromFile(file);
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                dialog.dismiss();
                Toast.makeText(getContext(),"Success", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(getContext(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}