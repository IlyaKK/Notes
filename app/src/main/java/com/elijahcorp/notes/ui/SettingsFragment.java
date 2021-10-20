package com.elijahcorp.notes.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.ChangerTheme;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {
    private SwitchMaterial nightModeSwitchMaterial;
    public final static String SETTING_FRAGMENT = "SETTING_FRAGMENT";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nightModeSwitchMaterial = view.findViewById(R.id.night_mode_switch);
        initialiseTheme();
        initialiseChangeSwitchListener();
    }

    private void initialiseTheme() {
        nightModeSwitchMaterial.setChecked(ChangerTheme.initialiseTheme(requireContext()));
    }

    private void initialiseChangeSwitchListener() {
        nightModeSwitchMaterial.setOnCheckedChangeListener((l, c) -> {
            if (c) {
                ChangerTheme.setTheme(AppCompatDelegate.MODE_NIGHT_YES, ChangerTheme.THEME_DARK);
            } else {
                ChangerTheme.setTheme(AppCompatDelegate.MODE_NIGHT_NO, ChangerTheme.THEME_LIGHT);
            }
        });
    }
}
