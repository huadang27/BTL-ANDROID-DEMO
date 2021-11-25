package com.hqdang.baitaplonversion1.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hqdang.baitaplonversion1.Fragment.AdminHomeFragment;
import com.hqdang.baitaplonversion1.Fragment.AdminQRFragment;
import com.hqdang.baitaplonversion1.Fragment.AdminSettingFragment;

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
