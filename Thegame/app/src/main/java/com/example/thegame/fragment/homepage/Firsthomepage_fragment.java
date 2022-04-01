package com.example.thegame.fragment.homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thegame.R;

public class Firsthomepage_fragment extends Fragment {
    private static final String TAG = "Firsthomepage_fragment";
    private View view;
    private ViewFlipper viewFlipper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage_first, container, false);

        init();
        Log.d(TAG, "onCreateView: ");

        return view;
    }

    private void init() {
        viewFlipper = view.findViewById(R.id.MF_viewflipper_first);
        viewFlipper.startFlipping();
    }
}
