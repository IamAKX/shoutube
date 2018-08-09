package com.akashapplications.shoutube;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.akashapplications.shoutube.fragments.Login;
import com.akashapplications.shoutube.fragments.Register;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class Authenticate extends AppCompatActivity {
    private SmartTabLayout viewPagerTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Login", Login.class)
                .add("Register", Register.class)
                .create());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);


        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewPageTab);
        viewPagerTab.setViewPager(viewPager);
    }
}
