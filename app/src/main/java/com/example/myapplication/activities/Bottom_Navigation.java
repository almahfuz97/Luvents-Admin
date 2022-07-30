package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.Adapters.SlidePagerAdapterFragment;
import com.example.myapplication.Fragments.AllEventsFragment;
import com.example.myapplication.Fragments.CreateFragment;
import com.example.myapplication.Fragments.ProfileFragment;
import com.example.myapplication.R;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import java.util.ArrayList;

public class Bottom_Navigation extends AppCompatActivity {

    int profileTab=0;
    ViewPager viewPager;
    BubbleNavigationLinearView bubbleNavigationLinearView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__navigation);

        ArrayList<Fragment> fragmentArrayList=new ArrayList<>();

        fragmentArrayList.add(new CreateFragment());
        fragmentArrayList.add(new AllEventsFragment());
        fragmentArrayList.add(new ProfileFragment());

        SlidePagerAdapterFragment pagerAdapterFragment=new SlidePagerAdapterFragment(fragmentArrayList, getSupportFragmentManager());

         final Fragment fragmentFinal=pagerAdapterFragment.getItem(2);

          bubbleNavigationLinearView=findViewById(R.id.bubble_nav_linear_id);

        bubbleNavigationLinearView.setTypeface(null);

        bubbleNavigationLinearView.setBadgeValue(0,null);
        bubbleNavigationLinearView.setBadgeValue(1,null);
        bubbleNavigationLinearView.setBadgeValue(2,null);

        Intent intent=getIntent();
        profileTab=intent.getIntExtra("tab",0);

          viewPager=findViewById(R.id.view_pager);

        viewPager.setAdapter(pagerAdapterFragment);

        switchTab();



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                bubbleNavigationLinearView.setCurrentActiveItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setCurrentItem(position,true);
            }
        });
    }

    private void switchTab() {

        if (profileTab==3)
        {
            viewPager.setCurrentItem(2);
            bubbleNavigationLinearView.setCurrentActiveItem(2);
        }
        else if (profileTab==2){
            viewPager.setCurrentItem(1);
            bubbleNavigationLinearView.setCurrentActiveItem(1);
        }
        else if (profileTab==1){
            viewPager.setCurrentItem(0);
            bubbleNavigationLinearView.setCurrentActiveItem(0);
        }

    }
}
