package com.elijahcorp.notes.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elijahcorp.notes.R;

public class AboutFragment extends Fragment {
    public final static String ABOUT_FRAGMENT = "ABOUT_FRAGMENT";
    private Controller controller;
    private TopAppBarListener topAppBarListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Controller) {
            controller = (Controller) context;
            topAppBarListener = (TopAppBarListener) context;
        } else {
            throw new IllegalStateException("Activity doesn't have impl AboutFragment.Controller");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            controller.launchAboutFragment();
            controller.deleteAboutFragment(this);
        }
        topAppBarListener.changeTopAppBar(ABOUT_FRAGMENT);
    }

    interface Controller {
        void launchAboutFragment();

        void deleteAboutFragment(AboutFragment aboutFragment);
    }

    interface TopAppBarListener {

        void changeTopAppBar(String nameFragment);
    }
}
