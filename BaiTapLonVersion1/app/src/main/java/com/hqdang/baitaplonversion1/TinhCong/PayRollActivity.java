package com.hqdang.baitaplonversion1.TinhCong;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.hqdang.baitaplonversion1.Adapter.DayAdapter;
import com.hqdang.baitaplonversion1.DuLieu.FbManeger;
import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

public class PayRollActivity extends AppCompatActivity implements FbManeger.FBCallBack {

    String TAG = "PayRollActivity";

    FbManeger fbManeger;
    Calendar calendar;

    ImageView imgBack;
    List<String> arrayList;
    DayAdapter dayAdapter;
    ListView listView;

    LinearLayout layout;
    Button btDetail;
    TextView txtDayWork, txtDayOff, txtBaseSalery, txtCoeffic, txtSum, txtMonth;
    float fBaseSalery = 4500000f, fCoeffic = 0, fSum = 0;
    int iOff = 0, iWork = 0;
    int month;
    String ID = AGConnectAuth.getInstance().getCurrentUser().getUid();

    Boolean mind = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_roll);

        getLinkViews();

        getControls();

        month = calendar.get(Calendar.MONTH) + 1;

        //getDatabaseFirebase();
        txtMonth.setText("Month "+month);
        fbManeger.QueryStaffInFo("dbUser",ID);
    }

    private void getControls() {
        btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mind){
                    layout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    btDetail.setText("Come back");
                    dayAdapter.addListDate(arrayList);
                    mind = false;
                }else {
                    layout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    btDetail.setText("Detail");
                    mind = true;
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        layout = findViewById(R.id.payroll);
        fbManeger = new FbManeger();
        fbManeger.addCallBacks(PayRollActivity.this);
        calendar = Calendar.getInstance();
        btDetail = findViewById(R.id.bt_detail);
        txtDayWork = findViewById(R.id.txt_day_work);
        txtDayOff = findViewById(R.id.txt_day_off);
        txtBaseSalery = findViewById(R.id.txt_base_salary);
        txtCoeffic = findViewById(R.id.txt_coefficient);
        txtSum = findViewById(R.id.txt_sum);
        txtMonth = findViewById(R.id.month);

        listView = findViewById(R.id.lv_day);
        arrayList = new ArrayList<>();
        dayAdapter = new DayAdapter(PayRollActivity.this,R.layout.item_listview_dayadapter);
        listView.setAdapter(dayAdapter);
    }

    @Override
    public void QueryStaffInFoSuccess(Staff staff) {
        fCoeffic = staff.getCoeffic();
        txtCoeffic.setText(String.valueOf(fCoeffic));
        txtBaseSalery.setText(String.valueOf(fBaseSalery));
        fbManeger.QueryCalender(ID,month);
    }

    @Override
    public void QueryStaffInFoFail(String e) {

    }

    @Override
    public void QueryCalenderSuccess(int off, int work, List<String> list) {
        txtDayOff.setText(""+off);
        txtDayWork.setText(""+work);
        iOff = off;
        iWork = work;
        fSum = (float) fBaseSalery*fCoeffic* ((float)(work)/(float)(iWork+iOff));
        txtSum.setText(String.valueOf(fSum));

        arrayList.addAll(list);
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