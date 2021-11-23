package vn.embosua.ltddquanlynhanvienversion4.TinhCong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.Adapter.DayAdapter;
import vn.embosua.ltddquanlynhanvienversion4.ChiTiet.DetailStaffWorking;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.FbManeger;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class PayRollActivity extends PortraitActivity implements FbManeger.FBCallBack{

    String TAG = "PayRollActivity";

    FbManeger fbManeger;
    Calendar calendar;

    ImageView imgBack;
    List<String> arrayList;
    DayAdapter dayAdapter;
    ListView listView;

    LinearLayout layout;
    Button btDetail, btChooseMonth;
    TextView txtDayWork, txtDayOff, txtBaseSalery, txtCoeffic, txtSum, txtMonth, txtNotification;
    float fBaseSalery = 4500000f, fCoeffic = 0, fSum = 0;
    int iOff = 0, iWork = 0;
    int month;
    String ID;

    Boolean mind = true;

    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_roll);

        getLinkViews();

        getUID();

        getControls();

        month = calendar.get(Calendar.MONTH) + 1;

        //getDatabaseFirebase();
        txtMonth.setText("Month "+month);
        //txtMonth.setText("Payroll");
        fbManeger.QueryStaffInFo("dbUser",ID);
    }

    // lấy id nhân viên
    private void getUID(){
        if (getIntent().hasExtra("id")){
            ID = getIntent().getStringExtra("id");
        }else {
            ID = AGConnectAuth.getInstance().getCurrentUser().getUid();
        }
    }

    private void getControls() {
        // xem chi tiết ngày đi làm và ngày nghỉ
        btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mind){
                    layout.setVisibility(View.GONE);
                    btChooseMonth.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    btDetail.setText("Come back");
                    dayAdapter.addListDate(arrayList);
                    mind = false;
                }else {
                    layout.setVisibility(View.VISIBLE);
                    btChooseMonth.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    btDetail.setText("Detail");
                    mind = true;
                }
            }
        });

        // chọn tháng xem bảng lương
        btChooseMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogChooseMonth();
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

    // open dialog chọn tháng
    public void openDialogChooseMonth(){
        dialog = new Dialog(PayRollActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_month);

        Window window = dialog.getWindow();
        if (window == null) return;

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        Button m1  = dialog.findViewById(R.id.bt_month_1);
        Button m2  = dialog.findViewById(R.id.bt_month_2);
        Button m3  = dialog.findViewById(R.id.bt_month_3);
        Button m4  = dialog.findViewById(R.id.bt_month_4);
        Button m5  = dialog.findViewById(R.id.bt_month_5);
        Button m6  = dialog.findViewById(R.id.bt_month_6);
        Button m7  = dialog.findViewById(R.id.bt_month_7);
        Button m8  = dialog.findViewById(R.id.bt_month_8);
        Button m9  = dialog.findViewById(R.id.bt_month_9);
        Button m10  = dialog.findViewById(R.id.bt_month_10);
        Button m11  = dialog.findViewById(R.id.bt_month_11);
        Button m12  = dialog.findViewById(R.id.bt_month_12);

        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 1;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 2;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 3;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 4;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 5;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 6;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 7;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 8;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 9;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 10;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 11;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        m12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = 12;
                txtMonth.setText("Month "+month);
                fbManeger.QueryStaffInFo("dbUser",ID);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    // // liên các đối tượng hiển thị trên view
    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        layout = findViewById(R.id.payroll);
        fbManeger = new FbManeger();
        fbManeger.addCallBacks(PayRollActivity.this);
        calendar = Calendar.getInstance();
        btDetail = findViewById(R.id.bt_detail);
        btChooseMonth = findViewById(R.id.bt_choose_month);
        txtDayWork = findViewById(R.id.txt_day_work);
        txtDayOff = findViewById(R.id.txt_day_off);
        txtBaseSalery = findViewById(R.id.txt_base_salary);
        txtCoeffic = findViewById(R.id.txt_coefficient);
        txtSum = findViewById(R.id.txt_sum);
        txtMonth = findViewById(R.id.month);
        txtNotification = findViewById(R.id.txt_notification);

        listView = findViewById(R.id.lv_day);
        arrayList = new ArrayList<>();
        dayAdapter = new DayAdapter(PayRollActivity.this,R.layout.item_listview_dayadapter);
        listView.setAdapter(dayAdapter);
    }

    @Override // lấy thông tin nhân viên thành công
    public void QueryStaffInFoSuccess(Staff staff) {
        fCoeffic = staff.getCoeffic();
        txtCoeffic.setText(String.valueOf(fCoeffic));
        txtBaseSalery.setText(String.valueOf(fBaseSalery));
        fbManeger.QueryCalender(ID,month);
    }

    @Override
    public void QueryStaffInFoFail(String e) {

    }

    @Override // lấy thông tin bảng chấm công thành công
    public void QueryCalenderSuccess(int off, int work, List<String> list) {
        txtDayOff.setText(""+off);
        txtDayWork.setText(""+work);
        iOff = off;
        iWork = work;
        fSum = (float) fBaseSalery*fCoeffic* ((float)(work)/(float)(iWork+iOff));
        txtSum.setText(String.valueOf(fSum));

        arrayList.clear();
        arrayList.addAll(list);
        int thisMonth = calendar.get(Calendar.MONTH) + 1;
        if (list.size()==0 && month<thisMonth){
            txtNotification.setVisibility(View.VISIBLE);
        }
        if (list.size()==0 && month>thisMonth){
            txtNotification.setVisibility(View.VISIBLE);
            txtNotification.setText("The month hasn't come yet");
        }
        if (list.size()>0)
            txtNotification.setVisibility(View.GONE);
    }


//    private void getDatabaseFirebase() {
//        Calendar calendar = Calendar.getInstance();
//        int month =  calendar.get(Calendar.MONTH) + 1;
//        String uID = AGConnectAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference.child("BDMONTH_" + month).child(uID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String value = dataSnapshot.getValue(String.class);
//                    if (value.equals("0")) {
//                        arrayList.add(value);
//                    } else {
//                        arrayList.add(value);
//                    }
//                }
//                dayAdapter.addListDate(arrayList);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "load data failed : " + error.toString());
//            }
//        });
//    }
}