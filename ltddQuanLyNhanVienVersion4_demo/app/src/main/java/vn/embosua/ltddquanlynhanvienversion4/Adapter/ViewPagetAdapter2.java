package vn.embosua.ltddquanlynhanvienversion4.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import vn.embosua.ltddquanlynhanvienversion4.Fragment.AdminHomeFragment;
import vn.embosua.ltddquanlynhanvienversion4.Fragment.AdminQRFragment;
import vn.embosua.ltddquanlynhanvienversion4.Fragment.AdminSettingFragment;
import vn.embosua.ltddquanlynhanvienversion4.Fragment.UserHomeFragment;
import vn.embosua.ltddquanlynhanvienversion4.Fragment.UserSettingFragment;

public class ViewPagetAdapter2 extends FragmentStatePagerAdapter {

    public ViewPagetAdapter2(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserHomeFragment();
            case 1: return new UserSettingFragment();
            default: return new UserHomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
