package com.elijahcorp.notes.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.ChangerTheme;
import com.elijahcorp.notes.domain.Note;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NotesListFragment.Controller, NotesListFragment.TopAppBarListener, NoteEditFragment.Controller, NoteEditFragment.TopAppBarListener,
        AboutFragment.Controller, AboutFragment.TopAppBarListener, SettingsFragment.Controller, SettingsFragment.TopAppBarListener {
    private MaterialToolbar topAppBar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private final String NAVIGATION_ITEM_KEY = "NAVIGATION_ITEM_KEY";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(NAVIGATION_ITEM_KEY, Objects.requireNonNull(navigationView.getCheckedItem()).getItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        setContentView(R.layout.activity_main);
        initialiseTopAppBar();
        initialiseMenuItem(savedInstanceState);
        changerFragmentLaunch();
        ChangerTheme.initialiseTheme(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(NoteEditFragment.NOTE_EDIT_FRAGMENT) != null) {
            returnOnBackOrSavePressed();
        } else {
            super.onBackPressed();
        }
    }

    private void initialiseTopAppBar() {
        topAppBar = findViewById(R.id.top_app_bar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
    }

    private void initialiseMenuItem(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.open_main_screen_item);
        } else {
            navigationView.setCheckedItem(savedInstanceState.getInt(NAVIGATION_ITEM_KEY));
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void changerFragmentLaunch() {
        switch (Objects.requireNonNull(navigationView.getCheckedItem()).getItemId()) {
            case R.id.open_main_screen_item:
                launchNotesListFragment();
                break;
            case R.id.open_settings_screen_item:
                launchSettingsFragment();
                break;
            case R.id.open_about_app_screen_item:
                launchAboutFragment();
                break;
        }
    }

    @Override
    public void changeTopAppBar(String nameFragment) {
        switch (nameFragment) {
            case NotesListFragment.NOTES_LIST_FRAGMENT:
            case SettingsFragment.SETTING_FRAGMENT:
            case AboutFragment.ABOUT_FRAGMENT:
                topAppBar.setTitle(R.string.app_name);
                setSupportActionBar(topAppBar);
                initDrawer();
                break;
            case NoteEditFragment.NOTE_EDIT_FRAGMENT:
                topAppBar.setTitle(" ");
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    topAppBar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
                } else {
                    topAppBar.setNavigationIcon(R.drawable.ic_baseline_check_24);
                }
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                setSupportActionBar(topAppBar);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void initDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, topAppBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(item -> {
            closeAnotherFragments();
            switch (item.getItemId()) {
                case R.id.open_main_screen_item:
                    launchNotesListFragment();
                    drawer.closeDrawers();
                    return true;
                case R.id.open_settings_screen_item:
                    launchSettingsFragment();
                    drawer.closeDrawers();
                    return true;
                case R.id.open_about_app_screen_item:
                    launchAboutFragment();
                    drawer.closeDrawers();
                    return true;
            }
            return false;
        });
    }

    private void closeAnotherFragments() {
        NotesListFragment notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentByTag(NotesListFragment.NOTES_LIST_FRAGMENT);
        AboutFragment aboutFragment = (AboutFragment) getSupportFragmentManager().findFragmentByTag(AboutFragment.ABOUT_FRAGMENT);
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.SETTING_FRAGMENT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (notesListFragment != null) {
            fragmentTransaction.remove(notesListFragment);
        }

        if (aboutFragment != null) {
            fragmentTransaction.remove(aboutFragment);
        }

        if (settingsFragment != null) {
            fragmentTransaction.remove(settingsFragment);
        }

        fragmentTransaction.commit();
    }

    private void launchNotesListFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            launchPortraitNotesList();
        } else {
            launchLandscapeNotesList();
        }
    }

    private void launchPortraitNotesList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NotesListFragment notesListFragment = (NotesListFragment) fragmentManager.findFragmentByTag(NotesListFragment.NOTES_LIST_FRAGMENT);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (notesListFragment != null) {
            fragmentTransaction.replace(R.id.fragment_container_frame_layout, notesListFragment, NotesListFragment.NOTES_LIST_FRAGMENT);
        } else {
            fragmentTransaction.replace(R.id.fragment_container_frame_layout, new NotesListFragment(), NotesListFragment.NOTES_LIST_FRAGMENT);
        }
        fragmentTransaction.commit();
    }

    private void launchLandscapeNotesList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(NotesListFragment.NOTES_LIST_FRAGMENT) != null) {
            fragmentManager.popBackStack();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_frame_layout, new NotesListFragment(), NotesListFragment.NOTES_LIST_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void launchAboutFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            launchPortraitAboutFragment();
        } else {
            launchLandscapeAboutFragment();
        }
    }

    private void launchPortraitAboutFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_frame_layout, new AboutFragment(), AboutFragment.ABOUT_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void launchLandscapeAboutFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_3_frame_layout, new AboutFragment(), AboutFragment.ABOUT_FRAGMENT);
        fragmentTransaction.commit();
    }

    public void launchSettingsFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            launchPortraitSettingFragment();
        } else {
            launchLandscapeSettingsFragment();
        }
    }

    private void launchLandscapeSettingsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_3_frame_layout, new SettingsFragment(), SettingsFragment.SETTING_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void launchPortraitSettingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_frame_layout, new SettingsFragment(), SettingsFragment.SETTING_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void displayNoteEdit(Note note) {
        launchEditNoteFragment(note);
    }

    private void launchEditNoteFragment(Note note) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            launchEditNoteFragmentPortrait(note);
        } else {
            launchEditNoteFragmentLandscape(note);
        }
    }

    private void launchEditNoteFragmentPortrait(Note note) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_frame_layout, NoteEditFragment.newInstance(note), NoteEditFragment.NOTE_EDIT_FRAGMENT)
                .addToBackStack("")
                .commit();
    }

    private void launchEditNoteFragmentLandscape(Note note) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_2_frame_layout, NoteEditFragment.newInstance(note), NoteEditFragment.NOTE_EDIT_FRAGMENT)
                .commit();
    }

    @Override
    public void initialiseNavigationIconBack() {
        topAppBar.setNavigationOnClickListener(l -> returnOnBackOrSavePressed());
    }

    private void returnOnBackOrSavePressed() {
        NotesListFragment notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentByTag(NotesListFragment.NOTES_LIST_FRAGMENT);
        NoteEditFragment noteEditFragment = (NoteEditFragment) getSupportFragmentManager().findFragmentByTag(NoteEditFragment.NOTE_EDIT_FRAGMENT);
        if (notesListFragment != null && noteEditFragment != null) {
            noteEditFragment.saveChangeNote();
            if (noteEditFragment.getArguments() != null) {
                Note note = noteEditFragment.getArguments().getParcelable(NoteEditFragment.CHANGE_NOTE_KEY);
                notesListFragment.setNoteChange(note);
            }

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                getSupportFragmentManager().popBackStack();
            } else {
                deleteOldEditFragment(noteEditFragment);
                notesListFragment.initialiseTopAppBar();
            }
        }
    }

    @Override
    public void deleteSettingFragment(SettingsFragment settingsFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(settingsFragment)
                .commit();
    }

    @Override
    public void deleteAboutFragment(AboutFragment aboutFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(aboutFragment)
                .commit();
    }

    @Override
    public void deleteOldEditFragment(NoteEditFragment noteEditFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(noteEditFragment)
                .commit();
    }
}
