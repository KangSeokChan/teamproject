package com.example.madforumapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainFeed extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private ImageView profileIcon;

//    initializing the fragments
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private AskFragment askFragment;
    private SettingsFragment settingsFragment;
    private FeedbackFragment feedbackFragment;

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_edit);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 각 메뉴 클릭시 이뤄지는 이벤트
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.Question:
                        changeFragment(homeFragment);
                        return true;

                    case R.id.answer:
                        changeFragment(askFragment);
                        return true;

                    case R.id.support:
                        Intent contactUsIntent = new Intent(MainFeed.this, ContactUs.class);
                        startActivity(contactUsIntent);
                        return true;

                    case R.id.logout:
                        Intent i=new Intent(MainFeed.this,personalprofile.class);
                        startActivity(i);
                        return true;

                }

                return true;
            }
        });

        homeFragment = new HomeFragment();
        askFragment = new AskFragment();
        settingsFragment = new SettingsFragment();
        feedbackFragment = new FeedbackFragment();
        profileFragment = new ProfileFragment();
//        profileIcon = findViewById(R.id.profileIcon);
        bottomNavigationView = findViewById(R.id.mainNavBar);
        frameLayout = findViewById(R.id.navFrame);


        changeFragment(homeFragment);

//        profileIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i=new Intent(MainFeed.this,personalprofile.class);
//                startActivity(i);
//            }
//        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navBtnHome:
                        changeFragment(homeFragment);
                        return true;
                    case R.id.navBtnAsk:
                        changeFragment(askFragment);
                        return true;
                    case R.id.navBtnContact:
                        Intent contactUsIntent = new Intent(MainFeed.this, ContactUs.class);
                        startActivity(contactUsIntent);
                        return true;
                    case R.id.navBtnProfile:
                        Intent i=new Intent(MainFeed.this,personalprofile.class);
                        startActivity(i);
                        return true;

                        default:
                            return false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        changeFragment(homeFragment);
    }

    public void ChangeToHome(){
        changeFragment(homeFragment);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
    }

    private void changeFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.navFrame, fragment);
        fragmentTransaction.commit();

    }
    private long backKeyPressedTime = 0;
    private Toast toast;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}
