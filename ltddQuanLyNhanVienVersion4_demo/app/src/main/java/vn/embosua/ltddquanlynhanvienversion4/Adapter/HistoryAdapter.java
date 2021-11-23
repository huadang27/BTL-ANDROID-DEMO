package vn.embosua.ltddquanlynhanvienversion4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.ChiTiet.DetailHistoryActivity;
import vn.embosua.ltddquanlynhanvienversion4.ChiTiet.DetailStaffDeleted;
import vn.embosua.ltddquanlynhanvienversion4.ChiTiet.DetailStaffWorking;
import vn.embosua.ltddquanlynhanvienversion4.Model.EditHistory;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHoler>{

    List<EditHistory> historyList = new ArrayList<>();
    List<EditHistory> historyListOld = new ArrayList<>();
    List<Staff> staffList = new ArrayList<>();
    Context context;

    public void addListHistory(List<EditHistory> historyList) {
        this.historyList.clear();
        this.historyList.addAll(historyList);
        this.historyListOld.addAll(historyList);
    }

    public void addListStaff(List<Staff> staffList){
        this.staffList.clear();
        this.staffList.addAll(staffList);
        notifyDataSetChanged();
    }

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override //1
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_history,parent,false);

        return new MyViewHoler(view);
    }

    //2
    public class MyViewHoler extends RecyclerView.ViewHolder {
        TextView txt_time, txt_name_editor, txt_id_editor;
        RelativeLayout relativeLayout;
        ImageView img_next;
        public MyViewHoler(@NonNull View itemView) {
            super(itemView);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_name_editor = itemView.findViewById(R.id.txt_name_editor);
            txt_id_editor = itemView.findViewById(R.id.txt_id_editor);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
            img_next = itemView.findViewById(R.id.img_next);
        }
    }

    @Override//3
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {
        EditHistory editHistory = historyList.get(position);
        // lấy thông tin nhân viên trước khi chỉnh sửa (khi position >=0)
        Staff staff1 = new Staff();
        if(position>0){
            staff1.setDataStaff(historyList.get((position-1)).getInfoCorrected());
        }
        // thông tin nhân viên đã chỉnh sửa (khi position >0)
        Staff staff2 = new Staff();
        staff2.setDataStaff(editHistory.getInfoCorrected());
        // set view item
        holder.txt_time.setText(editHistory.getDateTime());
        holder.txt_id_editor.setText(editHistory.getIDEditor());
        String name = "error";
        // lay thong tin editor - nguoi chinh sua thong tin
        Staff editor = new Staff();
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getId().equals(editHistory.getIDEditor())){
                name = staffList.get(i).getFullName();
                editor.setDataStaff(staffList.get(i));
            }

        }
        holder.txt_name_editor.setText(name);

        //xét đều màu item
        // 0: khởi tạo tài khoản - trắng
        // 1: lịch sử sửa đổi - trắng
        // 2: lịch sử xóa tài khoản - đỏ
        // 3: lịch sử khôi phục tài khoản - xanh lá
        int cases;

//        if (!staff2.getStaff()){ // nếu isStaff = false thì là đã xóa
//            cases = 2;
//            holder.relativeLayout.setBackgroundResource(R.drawable.custom_bg_item_history_red);
//        }else {
//            if (historyList.get(position).getReason().equals("undelete01101000")){
//                cases = 3;
//                holder.relativeLayout.setBackgroundResource(R.drawable.custom_bg_item_history_green);
//            }else {
//                if (historyList.get(position).getReason().equals("edit01101000")){
//                    cases = 1;
//                    holder.relativeLayout.setBackgroundResource(R.drawable.custom_bg_item_history);
//                }
//            }
//        }

        switch (editHistory.getReason()){
            case "create01101000": cases = 0;
                break;
            case "edit01101000": cases = 1;
                holder.relativeLayout.setBackgroundResource(R.drawable.custom_bg_item_history);
                break;
            case "undelete01101000": cases = 3;
                holder.relativeLayout.setBackgroundResource(R.drawable.custom_bg_item_history_green);
                break;
            default: cases = 2;
                holder.relativeLayout.setBackgroundResource(R.drawable.custom_bg_item_history_red);
                break;
        }

        int finalCases = cases;
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailHistoryActivity.class);
                    intent.putExtra("cases",finalCases);

                    if (finalCases == 1){
                        intent.putExtra("staff1",staff1);
                        intent.putExtra("staff2", staff2);
                    }
                    intent.putExtra("time",editHistory.getDateTime());
                    intent.putExtra("editor",editor);
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

}
