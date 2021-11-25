package com.hqdang.baitaplonversion1.DanhSach;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.hqdang.baitaplonversion1.Adapter.StaffAdapter;
import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

public class ListStaffActivity extends AppCompatActivity {

    ImageView imgBack;
    RecyclerView recyclerView;
    List<Staff> list;
    StaffAdapter staffAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_staff);

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

    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        staffAdapter = new StaffAdapter(this);
        recyclerView.setAdapter(staffAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("dbUser");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Staff nv = dataSnapshot.getValue(Staff.class);
                    list.add(nv);
                }
                staffAdapter.addListStaff(list);
                list.clear();
                Log.e("MainActivityAdmin","load data success");
                Toast.makeText(ListStaffActivity.this,"load data success",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivityAdmin","load data failed : " + error.toString());
                Toast.makeText(ListStaffActivity.this,"load data failed : " + error.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

}