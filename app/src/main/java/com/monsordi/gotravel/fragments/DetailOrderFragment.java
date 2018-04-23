package com.monsordi.gotravel.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.dto.Orden;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailOrderFragment extends Fragment{

    @BindView(R.id.detail_phone_image)
    ImageView phoneImage;
    @BindView(R.id.detail_phone_name)
    TextView phoneName;
    @BindView(R.id.detail_warehouse_branch)
    TextView warehouseBranch;
    @BindView(R.id.detail_warehouse_address)
    TextView warehouseAddress;

    private Orden order;

    public DetailOrderFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_order,container,false);
        ButterKnife.bind(this,root);

        order = (Orden) getArguments().getSerializable(getString(R.string.order_key));
        initializeUi();

        return root;
    }

    private void initializeUi() {
        phoneName.setText(order.getCelular().getModel());
        Glide.with(getContext())
                .load(order.getCelular().getImageUrl())
                .into(phoneImage);
        if(order.getAlmacen() != null) {
            warehouseBranch.setText(order.getAlmacen().getSucursal());
            warehouseAddress.setText(order.getAlmacen().getAddress());
        }
    }
}
