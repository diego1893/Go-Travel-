package com.monsordi.gotravel.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.monsordi.gotravel.BottomNavigationBar;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.dto.Asesor;
import com.monsordi.gotravel.dto.Encargado;
import com.monsordi.gotravel.dto.Orden;
import com.monsordi.gotravel.dto.PrestadorServicio;
import com.monsordi.gotravel.fragments.DetailOrderFragment;
import com.monsordi.gotravel.fragments.DetailPeopleFragment;
import com.monsordi.gotravel.fragments.DetailServiceFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements BottomNavigationBar.TabListener {

    @BindView(R.id.detail_fragment_container)
    FrameLayout frameLayout;
    @BindView(R.id.detail_bottom)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.detail_toolbar)
    android.support.v7.widget.Toolbar toolbar;

    private Orden order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        order = (Orden) getIntent().getSerializableExtra(getString(R.string.order_key));

        BottomNavigationBar bottomNavigationBar = new BottomNavigationBar(bottomNavigation,this);
        bottomNavigationBar.createNavigationBar();

        loadServicesFragment();
    }

    private void loadPhoneFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.order_key),order);
        DetailOrderFragment detailOrderFragment = new DetailOrderFragment();
        detailOrderFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,detailOrderFragment).commit();
    }

    private void loadServicesFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.services_list),(Serializable) order.getPrestadores());
        DetailServiceFragment detailServiceFragment = new DetailServiceFragment();
        detailServiceFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,detailServiceFragment).commit();
    }

    private void loadPeopleFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.order_key),order);
        DetailPeopleFragment detailPeopleFragment = new DetailPeopleFragment();
        detailPeopleFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,detailPeopleFragment).commit();
    }

    @Override
    public void handlePosition(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                loadPhoneFragment();
                break;
            case 1:
                loadServicesFragment();
                break;
            case 2:
                loadPeopleFragment();
                break;
        }
    }
}
