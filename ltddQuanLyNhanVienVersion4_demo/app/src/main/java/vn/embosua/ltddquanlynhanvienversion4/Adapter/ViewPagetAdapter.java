package vn.embosua.ltddquanlynhanvienversion4.Adapter;

import android.net.wifi.hotspot2.pps.HomeSp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.embosua.ltddquanlynhanvienversion4.Fragment.AdminHomeFragment;
import vn.embosua.ltddquanlynhanvienversion4.Fragment.AdminQRFragment;
import vn.embosua.ltddquanlynhanvienversion4.Fragment.AdminSettingFragment;

public class ViewPagetAdapter extends FragmentStatePagerAdapter {

    public ViewPagetAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new AdminHomeFragment();
            case 1: return new AdminQRFragment();
            case 2: return new AdminSettingFragment();
            default: return new AdminHomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
