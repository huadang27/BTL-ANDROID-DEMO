package vn.embosua.ltddquanlynhanvienversion4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.ChiTiet.DetailStaffDeleted;
import vn.embosua.ltddquanlynhanvienversion4.ChiTiet.DetailStaffWorking;
import vn.embosua.ltddquanlynhanvienversion4.Model.ImageConvert;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.MyViewHoler> implements Filterable {

    List<Staff> nhanVien = new ArrayList<>();
    List<Staff> nhanVienOld = new ArrayList<>();
    Context context;
    Boolean working;

    public void addListStaff(List<Staff> ListStaff, Boolean tf) {
        nhanVien.clear();
        nhanVien.addAll(ListStaff);
        nhanVienOld.addAll(ListStaff);
        notifyDataSetChanged();
        working = tf;
    }

    public StaffAdapter(Context context) {
        this.context = context;
    }

    @Override //1
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_staff,parent,false);

        return new MyViewHoler(view);
    }

    @Override //3
    public void onBindViewHolder(MyViewHoler holder, int position) {
        Staff nv = nhanVien.get(position);
        holder.imageNV.setImageBitmap(new ImageConvert().toBitmap(nv.getPhoto()));
        holder.nameNV.setText(nv.getFullName());
        holder.emailNV.setText(nv.getEmail());
        holder.idNV.setText(nv.getId());
        holder.positionNV.setText(nv.getPosition());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (working){
                    intent = new Intent(context, DetailStaffWorking.class);
                }else {
                    intent = new Intent(context, DetailStaffDeleted.class);
                }
                intent.putExtra("staff",nv);
                intent.putExtra("working",working);
                context.startActivity(intent);
            }
        });
    }

    @Override //4
    public int getItemCount() {
        return nhanVien.size();
    }


    // 2
    public class MyViewHoler extends RecyclerView.ViewHolder {

        ImageView imageNV;
        TextView nameNV,emailNV,idNV,positionNV;
        LinearLayout item;

        public MyViewHoler(View itemView) {
            super(itemView);
            imageNV = itemView.findViewById(R.id.image_NV);
            nameNV = itemView.findViewById(R.id.name_NV);
            emailNV = itemView.findViewById(R.id.email_NV);
            idNV = itemView.findViewById(R.id.id_NV);
            positionNV = itemView.findViewById(R.id.position_NV);
            item = itemView.findViewById(R.id.item_LinearLayout);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    nhanVien = nhanVienOld;
                }else {
                    List<Staff> list = new ArrayList<>();
                    for (Staff staff: nhanVienOld){
                        if (staff.getRoom().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(staff);
                        }
                        if (staff.getFullName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(staff);
                        }
                        if (staff.getId().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(staff);
                        }
                        if (staff.getAddress().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(staff);
                        }
                        if (staff.getPosition().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(staff);
                        }
                        if (staff.getPhoneNumber().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(staff);
                        }
                    }
                    nhanVien = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = nhanVien;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                nhanVien = (List<Staff>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
