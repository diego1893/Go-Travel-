package com.monsordi.gotravel.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.monsordi.gotravel.MyPreference;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.api.OrderApi;
import com.monsordi.gotravel.dto.Orden;
import com.monsordi.gotravel.fragments.FinishFragment;
import com.monsordi.gotravel.fragments.PhoneFragment;
import com.monsordi.gotravel.fragments.ServiceFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewOrderActivity extends AppCompatActivity implements PhoneFragment.PhoneFragmentCallBack, ServiceFragment.ServicesFragmentCallBack,
        FinishFragment.FinishCallback,OrderApi.OrderListeners{

    @BindView(R.id.new_order_toolbar)
    Toolbar toolbar;

    private FragmentTransaction fragmentTransaction;
    private PhoneFragment phoneFragment;
    private ServiceFragment serviceFragment;
    private FinishFragment finishFragment;

    private MyPreference preference;
    private OrderApi orderApi;
    private int step=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preference = new MyPreference(this);

        phoneFragment = new PhoneFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.new_order_fragment, phoneFragment).addToBackStack(PhoneFragment.class.getSimpleName()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void handlePhoneFragment() {
        serviceFragment = new ServiceFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.add(R.id.new_order_fragment, serviceFragment).addToBackStack(ServiceFragment.class.getSimpleName()).commit();
        fragmentTransaction.hide(phoneFragment);
    }

    @Override
    public void handleServiceFragment() {
        finishFragment = new FinishFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.add(R.id.new_order_fragment, finishFragment).addToBackStack(FinishFragment.class.getSimpleName()).commit();
        fragmentTransaction.hide(serviceFragment);
    }

    @Override
    public void handleFinishCallback() {
        orderApi = new OrderApi(this);
        orderApi.setWarehouse(0L,preference.getOrderId());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        switch (step){
            case 0:
                orderApi.setWarehouse(0L,preference.getOrderId());
                break;
            case 1:
                orderApi.setAttendant(0l,preference.getOrderId());
                break;
            case 2:
                orderApi.setCounselor(0l,preference.getOrderId());
                break;
        }
    }

    @Override
    public void onResponse(Orden order) {
        switch (step){
            case 0:
                step++;
                orderApi.setAttendant(0l,preference.getOrderId());
                break;
            case 1:
                step++;
                orderApi.setCounselor(0l,preference.getOrderId());
                break;
        }
    }
}
