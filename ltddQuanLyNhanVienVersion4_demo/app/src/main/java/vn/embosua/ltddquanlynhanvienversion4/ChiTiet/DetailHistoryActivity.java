package vn.embosua.ltddquanlynhanvienversion4.ChiTiet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class DetailHistoryActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView txt_time, txt_name_editor, txt_id_editor, txt_function, txt_room, txt_position;
    TextView txt_name_1, txt_birthday_1, txt_gender_1, txt_cmt_1, txt_address_1, txt_phone_1, txt_positon_1, txt_coeffic_1, txt_room_1, txt_access_right_1;
    TextView txt_name_2, txt_birthday_2, txt_gender_2, txt_cmt_2, txt_address_2, txt_phone_2, txt_positon_2, txt_coeffic_2, txt_room_2, txt_access_right_2;
    LinearLayout layout_editor, layout_edit_infor;

    Staff staff1, staff2, editor;
    int finalCases;
    String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        getLinkViews();

        getDataStaff();

        setDataViews();

        getControls();
    }

    private void getControls() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layout_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailHistoryActivity.this,DetailStaffWorking.class);
                intent.putExtra("editor",true);
                intent.putExtra("staff",editor);
                startActivity(intent);
            }
        });
    }

    private void setDataViews() {
        txt_name_editor.setText(editor.getFullName());
        txt_id_editor.setText(editor.getId());
        txt_room.setText(editor.getRoom());
        txt_position.setText(editor.getPosition());
        txt_time.setText(time);
        if (finalCases == 1){
            txt_name_1.setText(staff1.getFullName());
            txt_birthday_1.setText(staff1.getBirthDay());
            if (staff1.getGender())
                txt_gender_1.setText("Man");
            else
                txt_gender_1.setText("Woman");
            txt_cmt_1.setText(staff1.getCMT());
            txt_address_1.setText(staff1.getAddress());
            txt_phone_1.setText(staff1.getPhoneNumber());
            txt_positon_1.setText(staff1.getPosition());
            txt_coeffic_1.setText(String.valueOf(staff1.getCoeffic()));
            txt_room_1.setText(staff1.getRoom());
            if (staff1.getAccessRight())
                txt_access_right_1.setText("Admin");
            else
                txt_access_right_1.setText("User");

            //staff 2
            txt_name_2.setText(staff2.getFullName());
            txt_birthday_2.setText(staff2.getBirthDay());
            if (staff2.getGender())
                txt_gender_2.setText("Man");
            else
                txt_gender_2.setText("Woman");
            txt_cmt_2.setText(staff2.getCMT());
            txt_address_2.setText(staff2.getAddress());
            txt_phone_2.setText(staff2.getPhoneNumber());
            txt_positon_2.setText(staff2.getPosition());
            txt_coeffic_2.setText(String.valueOf(staff2.getCoeffic()));
            txt_room_2.setText(staff2.getRoom());
            if (staff2.getAccessRight())
                txt_access_right_2.setText("Admin");
            else
                txt_access_right_2.setText("User");
        }else{
            layout_edit_infor.setVisibility(View.GONE);
        }

        switch (finalCases){ // Color.RED ,Color.parseColor("#ffffff"),R.color.black
            case 0: txt_function.setText("Create"); break;
            case 1: txt_function.setText("Edit Infor"); break;
            case 2: txt_function.setText("Delete");
                txt_function.setTextColor(Color.parseColor("#FFC6C6")); break;
            case 3: txt_function.setText("Undelete");
                txt_function.setTextColor(Color.parseColor("#D2FFC2")); break;
        }
    }

    private void getDataStaff() {
        if (getIntent().hasExtra("editor")){
            time = getIntent().getStringExtra("time");
            editor = (Staff) getIntent().getSerializableExtra("editor");
            finalCases = getIntent().getIntExtra("cases",0);
            if (finalCases == 1){
                staff1 = (Staff) getIntent().getSerializableExtra("staff1");
                staff2 = (Staff) getIntent().getSerializableExtra("staff2");
            }
        }else {
            Toast.makeText(DetailHistoryActivity.this,"No Data.",Toast.LENGTH_SHORT).show();
        }
    }

    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        txt_time = findViewById(R.id.txt_time);
        txt_name_editor = findViewById(R.id.txt_name_editor);
        txt_id_editor = findViewById(R.id.txt_id_editor);
        txt_room = findViewById(R.id.txt_room);
        txt_position = findViewById(R.id.txt_positon);
        txt_function = findViewById(R.id.txt_function);

        txt_name_1 = findViewById(R.id.txt_name_1);
        txt_birthday_1 = findViewById(R.id.txt_birthday_1);
        txt_gender_1 = findViewById(R.id.txt_gender_1);
        txt_cmt_1 = findViewById(R.id.txt_cmt_1);
        txt_address_1 = findViewById(R.id.txt_address_1);
        txt_phone_1 = findViewById(R.id.txt_phone_1);
        txt_positon_1 = findViewById(R.id.txt_positon_1);
        txt_coeffic_1 = findViewById(R.id.txt_coeffic_1);
        txt_room_1 = findViewById(R.id.txt_room_1);
        txt_access_right_1 = findViewById(R.id.txt_access_right_1);

        txt_name_2 = findViewById(R.id.txt_name_2);
        txt_birthday_2 = findViewById(R.id.txt_birthday_2);
        txt_gender_2 = findViewById(R.id.txt_gender_2);
        txt_cmt_2 = findViewById(R.id.txt_cmt_2);
        txt_address_2 = findViewById(R.id.txt_address_2);
        txt_phone_2 = findViewById(R.id.txt_phone_2);
        txt_positon_2 = findViewById(R.id.txt_positon_2);
        txt_coeffic_2 = findViewById(R.id.txt_coeffic_2);
        txt_room_2 = findViewById(R.id.txt_room_2);
        txt_access_right_2 = findViewById(R.id.txt_access_right_2);

        layout_editor = findViewById(R.id.layout_editor);
        layout_edit_infor = findViewById(R.id.layout_edit_infor);

        staff1 = new Staff();
        staff2 = new Staff();
        editor = new Staff();
    }
}