package com.hqdang.baitaplonversion1.QRCode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.util.Calendar;
import java.util.List;

import com.hqdang.baitaplonversion1.DuLieu.FbManeger;
import com.hqdang.baitaplonversion1.Model.ImageConvert;
import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

public class ScanCodeActivity extends AppCompatActivity implements FbManeger.FBCallBack {
    String TAG = "ScanCodeActivity";

    Button btScan;

    private static final int DEFAULT_VIEW = 111;
    private static final int REQUEST_CODE_SCAN_ONE = 0X01;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Calendar calendar;
    int day, month;

    private String uID;

    TextView uFullName;
    ImageView uImgStaff, imgBack;

    FbManeger fbManeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        getViews();

        getTimes();

        getControls();


    }

    private void getTimes(){
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void getControls() {
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getViews() {
        imgBack = findViewById(R.id.img_back);
        uFullName = findViewById(R.id.txt_name_staff);
        uImgStaff = findViewById(R.id.img_staff);
        btScan = findViewById(R.id.bt_sacn_code);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        calendar = Calendar.getInstance();
        fbManeger = new FbManeger();
        fbManeger.addCallBacks(ScanCodeActivity.this);
    }

    // check permissions -- kiem tra cac quyen
    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    DEFAULT_VIEW);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || grantResults == null || grantResults.length < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(ScanCodeActivity.this,"Camera permission required",Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == DEFAULT_VIEW) {
            //start ScankitActivity for scanning barcode
            ScanUtil.startScan(ScanCodeActivity.this, REQUEST_CODE_SCAN_ONE, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN_ONE) {
            Object obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    uID = ((HmsScan) obj).getOriginalValue();
                    fbManeger.QueryStaffInFo("dbUser",uID);
                }else {
                    Toast.makeText(this,"Staff not found", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void QueryStaffInFoSuccess(Staff staff) {
        uFullName.setText(staff.getFullName());
        uImgStaff.setImageBitmap(new ImageConvert().toBitmap(staff.getPhoto()));
        fbManeger.Timekeeping(staff.getId(),String.valueOf(day),month,"1");
    }

    @Override
    public void QueryStaffInFoFail(String e) {
        Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void QueryCalenderSuccess(int off, int work, List<String> list) {

    }
}