package vn.embosua.ltddquanlynhanvienversion4.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.util.ArrayList;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.Adapter.StaffAdapter;
import vn.embosua.ltddquanlynhanvienversion4.DanhSach.ListStaffActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;
import vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan.PersonInforActivity;

public class AdminQRFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    List<Staff> list;
    StaffAdapter staffAdapter;

    EditText edtSearch;

    Button btWorking, btDeleted;

    Boolean working = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_qr, container, false);

        getLinkViews();

        getDatabase(working);

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

        btWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!working){
                    working = true;
                    getDatabase(working);
                }
            }
        });

        btDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(working){
                    working = false;
                    getDatabase(working);
                }
            }
        });
    }

    // liên kết với các đối tượng trên view
    private void getLinkViews() {
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        staffAdapter = new StaffAdapter(getContext());
        recyclerView.setAdapter(staffAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        edtSearch = view.findViewById(R.id.edt_search);
        btWorking = view.findViewById(R.id.bt_working);
        btDeleted = view.findViewById(R.id.bt_deleted);
    }

    // lấy danh sách nhân viên trên firebase
    private void getDatabase(Boolean working) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference("dbUser");
        Query query;
        if (working)
            query = databaseReference.orderByChild("staff").equalTo(true);
        else
            query = databaseReference.orderByChild("staff").equalTo(false);

        //query.addListenerForSingleValueEvent(valueEventListener);
        query.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                Staff nv = dataSnapshot.getValue(Staff.class);
                list.add(nv);
            }
            staffAdapter.addListStaff(list,working);
            list.clear();
            Log.e("MainActivityAdmin","load data success");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("MainActivityAdmin","load data failed : " + error.toString());
        }
    };

    @Override // khi thoát tap này sẽ đóng bàn phím
    public void onPause() {
        super.onPause();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}