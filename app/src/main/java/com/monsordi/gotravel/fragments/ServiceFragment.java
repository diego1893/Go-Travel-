package com.monsordi.gotravel.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.monsordi.gotravel.MyPreference;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.adapters.PhoneAdapter;
import com.monsordi.gotravel.adapters.ServiceAdapter;
import com.monsordi.gotravel.adapters.ServiceListAdapter;
import com.monsordi.gotravel.api.CelularApi;
import com.monsordi.gotravel.api.OrderApi;
import com.monsordi.gotravel.api.ServiceApi;
import com.monsordi.gotravel.dialog.DialogGoTravel;
import com.monsordi.gotravel.dto.Orden;
import com.monsordi.gotravel.dto.PrestadorServicio;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceFragment extends Fragment implements ServiceApi.ServiceListeners, View.OnClickListener, ServiceAdapter.OnItemClickListener, DialogGoTravel.DialogGoTravelTasks {

    private static final int DELETE = 0;

    @BindView(R.id.service_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.service_list_view)
    ListView listView;
    @BindView(R.id.service_ok_button)
    FloatingActionButton okButton;

    private List<PrestadorServicio> servicesList;
    private ServiceListAdapter serviceListAdapter;
    private int index = 0;

    private MyPreference preference;
    private ServiceAdapter serviceAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private OrderApi orderApi;

    private ServicesFragmentCallBack servicesFragmentCallBack;

    public ServiceFragment() {
    }

    public interface ServicesFragmentCallBack {
        void handleServiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, root);
        registerForContextMenu(listView);

        okButton.setOnClickListener(this);

        servicesList = new ArrayList<>();
        serviceListAdapter = new ServiceListAdapter(getContext(), servicesList);
        listView.setAdapter(serviceListAdapter);
        preference = new MyPreference(getContext());

        ServiceApi servicesListApi = new ServiceApi(this);
        servicesListApi.getServiceList();

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        servicesFragmentCallBack = (ServicesFragmentCallBack) activity;
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, DELETE, Menu.NONE, getString(R.string.delete_option));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case DELETE:
                serviceListAdapter.remove(servicesList.get(info.position));
                break;
        }

        return super.onContextItemSelected(item);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_ok_button:
                DialogGoTravel dialogGoTravel = new DialogGoTravel(getContext(), this);
                dialogGoTravel.showGoTravelDialog(getString(R.string.are_you_sure));
        }
    }

    @Override
    public void OnItemClick(PrestadorServicio service, int position) {
        if (!servicesList.contains(service))
            servicesList.add(service);
        serviceListAdapter.notifyDataSetChanged();
    }


    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(List<PrestadorServicio> serviceList) {
        serviceAdapter = new ServiceAdapter(serviceList, this);
        recyclerView.setAdapter(serviceAdapter);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


    @Override
    public void doCancelTask(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void doOkTask(Dialog dialog) {
        index = 0;
        dialog.dismiss();
        OrderApi deletion = new OrderApi(deletionListeners);
        deletion.deleteServicesByOrder(preference.getOrderId());
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    OrderApi.OrderListeners orderListeners = new OrderApi.OrderListeners() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(Orden order) {
            if (order.getPrestadores().size() == servicesList.size())
                servicesFragmentCallBack.handleServiceFragment();
            else {
                index++;
                orderApi.addService(servicesList.get(index).getId(), preference.getOrderId());
            }
        }
    };

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    OrderApi.DeletionListeners deletionListeners = new OrderApi.DeletionListeners() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response) {
            orderApi = new OrderApi(orderListeners);
            if (servicesList.size() > 0)
                orderApi.addService(servicesList.get(index).getId(), preference.getOrderId());
            else
                servicesFragmentCallBack.handleServiceFragment();
        }
    };
}
