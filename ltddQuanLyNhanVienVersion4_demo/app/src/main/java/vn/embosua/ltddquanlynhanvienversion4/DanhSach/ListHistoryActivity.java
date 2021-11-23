package vn.embosua.ltddquanlynhanvienversion4.DanhSach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.Adapter.HistoryAdapter;
import vn.embosua.ltddquanlynhanvienversion4.Adapter.StaffAdapter;
import vn.embosua.ltddquanlynhanvienversion4.ChiTiet.DetailStaffWorking;
import vn.embosua.ltddquanlynhanvienversion4.Model.EditHistory;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class ListHistoryActivity extends AppCompatActivity {

    Staff staff;
    ImageView imgBack;
    RecyclerView recyclerView;
    List<EditHistory> historyList;
    List<Staff> staffList;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);

        getDataStaff();

        getLinkViews();

        getDatabase();

        getControls();
    }

    private void getControls() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("dbHistory").orderByChild("idstaff").equalTo(staff.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    EditHistory ed = dataSnapshot.getValue(EditHistory.class);
                    historyList.add(ed);
                }
                historyAdapter.addListHistory(historyList);
                historyList.clear();
                Log.e("MainActivityAdmin","load data success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivityAdmin","load data failed : " + error.toString());
                Toast.makeText(ListHistoryActivity.this,"load data failed : " + error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        databaseReference.child("dbUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Staff nv = dataSnapshot.getValue(Staff.class);
                    staffList.add(nv);
                }
                historyAdapter.addListStaff(staffList);
                staffList.clear();
                Log.e("MainActivityAdmin","load data success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivityAdmin","load data failed : " + error.toString());
                Toast.makeText(ListHistoryActivity.this,"load data failed : " + error.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        historyList = new ArrayList<>();
        staffList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        historyAdapter = new HistoryAdapter(this);
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getDataStaff() {
        if (getIntent().hasExtra("staff")){
            staff = (Staff) getIntent().getSerializableExtra("staff");
        }else {
            Toast.makeText(ListHistoryActivity.this,"No Data.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}