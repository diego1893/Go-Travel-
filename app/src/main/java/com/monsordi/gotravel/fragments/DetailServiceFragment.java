package com.monsordi.gotravel.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.monsordi.gotravel.R;
import com.monsordi.gotravel.adapters.ServiceListAdapter;
import com.monsordi.gotravel.dto.PrestadorServicio;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailServiceFragment extends Fragment {

    @BindView(R.id.detail_service_list_view)
    ListView listView;

    private List<PrestadorServicio> servicesList;
    private ServiceListAdapter serviceListAdapter;

    public DetailServiceFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_services,container,false);
        ButterKnife.bind(this,root);

        servicesList = (List<PrestadorServicio>) getArguments().getSerializable(getString(R.string.services_list));
        serviceListAdapter = new ServiceListAdapter(getContext(),servicesList);
        listView.setAdapter(serviceListAdapter);

        return root;
    }
}
