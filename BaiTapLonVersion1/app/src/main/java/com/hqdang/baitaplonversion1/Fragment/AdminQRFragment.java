package com.hqdang.baitaplonversion1.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class AdminQRFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    List<Staff> list;
    StaffAdapter staffAdapter;

    EditText edtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_qr, container, false);

        getLinkViews();

        getDatabase();

        getControls();

        return view;
    }

    private void getControls() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                staffAdapter.getFilter().filter(s.toString());
            }
        });
    }

    private void getLinkViews() {
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        staffAdapter = new StaffAdapter(getContext());
        recyclerView.setAdapter(staffAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        edtSearch = view.findViewById(R.id.edt_search);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivityAdmin","load data failed : " + error.toString());
            }
        });
    }
}