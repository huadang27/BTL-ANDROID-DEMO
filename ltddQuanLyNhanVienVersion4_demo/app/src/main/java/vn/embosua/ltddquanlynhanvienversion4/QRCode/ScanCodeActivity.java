package vn.embosua.ltddquanlynhanvienversion4.QRCode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.util.Calendar;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.DuLieu.FbManeger;
import vn.embosua.ltddquanlynhanvienversion4.Model.ImageConvert;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

import static com.huawei.hms.feature.DynamicModuleInitializer.getContext;

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

    // lấy ngày tháng năm
    private void getTimes(){
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void getControls() {
        // mở máy ảnh đẻ quét mã vạch
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        // thoát
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // liên kết với các đối tượng trên màn hình
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

    @Override // nhận các quyền được trả về
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

    @Override // nhận kết quả trả về từ máy ảnh
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
                    // lấy dữ liệu nhân viên trả về
                    fbManeger.QueryStaffInFo("dbUser",uID);
                }else {
                    Toast.makeText(this,"Couldn't find any info", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override // nếu lấy được thông tin nhân viên vừa quyét thành công
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