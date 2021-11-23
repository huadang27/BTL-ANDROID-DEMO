package vn.embosua.ltddquanlynhanvienversion4.DuLieu;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;

public class FbManeger {
    private DatabaseReference databaseReference;

    private FBCallBack callBack = new FBCallBack() {
        @Override
        public void QueryStaffInFoSuccess(Staff staff) {
        }

        @Override
        public void QueryStaffInFoFail(String e) {
        }

        @Override
        public void QueryCalenderSuccess(int off, int work, List<String> list) {

        }
    };

    public void addCallBacks(FBCallBack fbCallBack){
        callBack = fbCallBack;
    }

    // phuong thuc khoi tao
    public FbManeger() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

/////////////////////////////

    // ghi thong tin nhan vien len fribase
    public void writeNewStaffOnFB(String DB, String id, Staff staff){
        databaseReference.child(DB).child(id).setValue(staff);
    }

    // tao bang cham cong theo id nhan vien
    public void CreateTimesheets(String id) {
        Calendar calendar = Calendar.getInstance();
        int sumDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;

        for (int i=1; i<=sumDay; i++){
            Timekeeping(id,String.valueOf(i),month,"0");
        }
    }

    // ghi du lieu cham cong len fribase (0 - nghi; 1 - di lam)
    public void Timekeeping(String id ,String days,int month, String bit){
        databaseReference
                .child("BDMONTH_"+month)
                .child(id)
                .child(days)
                .setValue(bit);
    }

    // test
    // truy van nhan vien theo id
    public void QueryStaffInFo(String DB, String id){
        databaseReference.child(DB).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Staff staff = snapshot.getValue(Staff.class);
                            callBack.QueryStaffInFoSuccess(staff);
                        }else {
                            callBack.QueryStaffInFoFail("Khong tim thay id: "+id);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        callBack.QueryStaffInFoFail(error.getMessage());
                    }
                });
    }

    public void QueryCalender(String ID, int month) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("BDMONTH_" + month).child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> list = new ArrayList<>();
                int off=0,work=0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String value = dataSnapshot.getValue(String.class);
                    list.add(value);
                    if (value.equals("0"))
                        off++;
                    else
                        work++;
                }
                callBack.QueryCalenderSuccess(off,work,list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface FBCallBack{
        void QueryStaffInFoSuccess(Staff staff);
        void QueryStaffInFoFail(String e);
        void QueryCalenderSuccess(int off, int work, List<String> list);
    }
}
