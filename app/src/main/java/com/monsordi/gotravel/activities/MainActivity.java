package com.monsordi.gotravel.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.monsordi.gotravel.MyPreference;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.adapters.OrderAdapter;
import com.monsordi.gotravel.api.OrderApi;
import com.monsordi.gotravel.api.SignApi;
import com.monsordi.gotravel.api.UserApi;
import com.monsordi.gotravel.dialog.DialogGoTravel;
import com.monsordi.gotravel.dto.Orden;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DialogGoTravel.DialogGoTravelTasks, View.OnClickListener
        , UserApi.UserOrdersListeners, AdapterView.OnItemClickListener {

    private static final int REQUEST_NEW_ORDER = 100;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_listview)
    ListView listView;
    @BindView(R.id.main_textview_listview)
    TextView textView;
    @BindView(R.id.main_new_order_button)
    FloatingActionButton newOrderButton;
    @BindView(R.id.main_progressBar)
    ProgressBar progressBar;


    private List<Orden> orderList;
    private OrderApi orderApi;
    private OrderAdapter orderAdapter;
    private MyPreference preference;
    private String token;
    private Long id;
    private int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.home));

        preference = new MyPreference(this);

        /*Checks if there is a current active user. If yes, a welcome greeting appears on the
         screen. On the contrary, the user is taken to the SignInActivity*/
        if (preference.isFirsTime()) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setEmptyView(textView);
        listView.setOnItemClickListener(this);
        newOrderButton.setOnClickListener(this);
        token = preference.getToken();
        id = preference.getId();
        getOrderList(SignApi.USER);
    }

    //****************************************************************************************************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_ORDER && resultCode != RESULT_OK) {
            OrderApi deletion = new OrderApi(new OrderApi.DeletionListeners() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {
                }
            });
            deletion.deleteOrderById(preference.getOrderId());
        }
    }

    //****************************************************************************************************************

    private void getOrderList(String type) {
        switch (type) {
            case SignApi.ATTENDANT:
                break;
            case SignApi.COUNSELOR:
                break;

            case SignApi.USER:
                UserApi userApi = new UserApi(this);
                userApi.getOrdersById(id, token);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_new_order_button:
                step = 0;
                orderApi = new OrderApi(orderListeners);
                orderApi.createOrder();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Orden currentOrder = orderList.get(i);
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(getString(R.string.order_key),currentOrder);
        startActivity(intent);
    }

    //****************************************************************************************************************

    //Methods related to the menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*When clicking the sign out button, the user is warned through a custom dialog
            if he really wants to complete the action.*/
            case R.id.menu_sign_out:
                DialogGoTravel dialogGoTravel = new DialogGoTravel(this, this);
                dialogGoTravel.showGoTravelDialog(getString(R.string.sign_out));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //****************************************************************************************************************

    //The next two following methods are the ones related to the selection in the appearing dialog.

    //Closes dialog
    @Override
    public void doCancelTask(Dialog dialog) {
        dialog.dismiss();
    }


    //Signs out and navigates to the SignInActivity
    @Override
    public void doOkTask(Dialog dialog) {
        preference.setOld(false);
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    //****************************************************************************************************************

    //These methods are related to the orders retrieving
    @Override
    public void onErrorResponse(VolleyError error) {
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(List<Orden> orderList) {
        this.orderList = orderList;

        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        orderAdapter = new OrderAdapter(this, orderList);
        listView.setAdapter(orderAdapter);
    }

    //****************************************************************************************************************

    OrderApi.OrderListeners orderListeners = new OrderApi.OrderListeners() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(Orden order) {
            if (step == 0) {
                step++;
                orderApi.setUser(id, order.getId());
            } else {
                Intent intent = new Intent(MainActivity.this, NewOrderActivity.class);
                preference.setOrderId(order.getId());
                startActivityForResult(intent, REQUEST_NEW_ORDER);
            }
        }
    };

    //****************************************************************************************************************


}
