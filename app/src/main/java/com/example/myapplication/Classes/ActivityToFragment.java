package com.example.myapplication.Classes;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.Fragments.AllEventsFragment;

public class ActivityToFragment extends FragmentActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content,new AllEventsFragment()).commit();
        }
    }
}
