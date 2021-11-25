package com.hqdang.baitaplonversion1.ManHinhChinh;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.hqdang.baitaplonversion1.Adapter.ViewPagetAdapter;
import com.hqdang.baitaplonversion1.R;

public class AdminActivity extends AppCompatActivity {

    //Button btSignOut;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getLinkViews();

        setUpViewPager();

        getControls();
    }
// chứa Fragment
    private void setUpViewPager() {
        ViewPagetAdapter viewPagetAdapter = new ViewPagetAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagetAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // bắt sự kiện khi ấn nút home
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
                        bottomNavigationView.getMenu().findItem(R.id.action_qrcode).setChecked(true);
                        break;
                    case 2:
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
//        btSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AGConnectAuth.getInstance().signOut();
//                startActivity(new Intent(AdminActivity.this, LogInActivity.class));
//                finish();
//            }
//        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        viewPager.setCurrentItem(0);
                        Toast.makeText(AdminActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_qrcode:
                        viewPager.setCurrentItem(1);
                        Toast.makeText(AdminActivity.this, "List", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_setting:
                        viewPager.setCurrentItem(2);
                        Toast.makeText(AdminActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }

    private void getLinkViews() {
        //btSignOut = findViewById(R.id.bt_sign_out);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.view_pager);
    }
}