package vn.embosua.ltddquanlynhanvienversion4.ManHinhChinh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.agconnect.auth.AGConnectAuth;

import vn.embosua.ltddquanlynhanvienversion4.Adapter.ViewPagetAdapter;
import vn.embosua.ltddquanlynhanvienversion4.Adapter.ViewPagetAdapter2;
import vn.embosua.ltddquanlynhanvienversion4.DangNhap.LogInActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class UserActivity extends PortraitActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getLinkViews();

        setUpViewPager();

        getControls();
    }

    private void setUpViewPager() {
        ViewPagetAdapter2 viewPagetAdapter = new ViewPagetAdapter2(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagetAdapter);
        // bắt sự kiện khi vuốt
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.action_setting).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getControls() {
        // sự kiện khi nhấn
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        viewPager.setCurrentItem(0);
                        //Toast.makeText(UserActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_setting:
                        viewPager.setCurrentItem(2);
                        //Toast.makeText(UserActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }

    // liên kết với các đối tượng trên view
    private void getLinkViews() {
        //btSignOut = findViewById(R.id.bt_sign_out);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.view_pager);
    }

    @Override // nhân 2 lần để thoát
    public void onBackPressed() {
        if (backPressedTime+2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}