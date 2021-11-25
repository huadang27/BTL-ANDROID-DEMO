package com.hqdang.baitaplonversion1.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import com.hqdang.baitaplonversion1.R;


public class DayAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private int resource;
    private List<String> objects = new ArrayList<>();

    public DayAdapter(Activity activity, int resource) {
        super(activity, resource);
        this.activity = activity;
        this.resource = resource;
    }

    public void addListDate(List<String> listDate){
        objects.clear();
        objects.addAll(listDate);
        notifyDataSetChanged();
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View view =inflater.inflate(resource,null);

        TextView dayofmonth = view.findViewById(R.id.txt_day_off_month);
        ImageView yesorno = view.findViewById(R.id.img_yes_or_no);

        String yon = this.objects.get(position);

        dayofmonth.setText(""+(position+1));


        if(yon.equals("1")){
            yesorno.setImageResource(R.drawable.ic_yes);

        }else {
            yesorno.setImageResource(R.drawable.ic_no);
        }

        return view;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return objects.get(position);
    }

    @Override
    public int getPosition(@Nullable String item) {
        return objects.indexOf(item);
    }

    public int getDayOff(){
        int dem = 0;
        for (int i=0;i<getCount();i++){
            if (!objects.get(i).equals("1")){
                dem++;
            }
        }
        return dem;
    }

    public int getDayWorking(){
        return getCount()-getDayOff();
    }

    public float getPayRoll(float Coefficient, int BaseSalary){
        return (float) BaseSalary*Coefficient* ((float)(getDayWorking())/(float)objects.size());
    }
}
