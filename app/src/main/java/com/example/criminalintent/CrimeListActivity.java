package com.example.criminalintent;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CrimeListActivity extends AppCompatActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    private static final String TAG_WELCOME_FRAGMENT = "welcome_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new CrimeListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        showTabletWelcomeIfNeeded();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCreateNewCrime() {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimeActivity.newCrimeIntent(this);
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(java.util.UUID.randomUUID(), true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated() {
        refreshCrimeList();
    }

    @Override
    public void onCrimeDeleted() {
        refreshCrimeList();
        showTabletWelcomeIfNeeded();
    }

    private void refreshCrimeList() {
        Fragment listFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (listFragment instanceof CrimeListFragment) {
            ((CrimeListFragment) listFragment).updateUI();
        }
    }

    private void showTabletWelcomeIfNeeded() {
        if (findViewById(R.id.detail_fragment_container) == null) {
            return;
        }

        Fragment currentDetail = getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
        if (currentDetail == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, new WelcomeFragment(), TAG_WELCOME_FRAGMENT)
                    .commit();
        }
    }
}
