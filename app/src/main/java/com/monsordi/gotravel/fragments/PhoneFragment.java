package com.monsordi.gotravel.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.monsordi.gotravel.MyPreference;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.adapters.PhoneAdapter;
import com.monsordi.gotravel.api.CelularApi;
import com.monsordi.gotravel.api.OrderApi;
import com.monsordi.gotravel.api.UserApi;
import com.monsordi.gotravel.dialog.DialogGoTravel;
import com.monsordi.gotravel.dto.Celular;
import com.monsordi.gotravel.dto.Orden;
import com.monsordi.gotravel.dto.Usuario;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneFragment extends Fragment implements View.OnClickListener, CelularApi.CelularListeners, PhoneAdapter.OnItemClickListener, DialogGoTravel.DialogGoTravelTasks {

    @BindView(R.id.cel_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.cel_selected_image)
    ImageView phoneImage;
    @BindView(R.id.cel_selected_text)
    TextView phoneText;
    @BindView(R.id.cel_ok_button)
    FloatingActionButton okButton;

    private Long phoneId = -1L;
    private MyPreference preference;
    private PhoneAdapter phoneAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private PhoneFragmentCallBack phoneFragmentCallBack;

    public PhoneFragment() {
    }

    public interface PhoneFragmentCallBack {
        void handlePhoneFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_phone, container, false);
        ButterKnife.bind(this, root);
        okButton.setOnClickListener(this);

        preference = new MyPreference(getContext());

        CelularApi celularListApi = new CelularApi(this);
        celularListApi.getCelularList();

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        phoneFragmentCallBack = (PhoneFragmentCallBack) activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cel_ok_button:
                if (phoneId != -1) {
                    DialogGoTravel dialogGoTravel = new DialogGoTravel(getContext(), this);
                    dialogGoTravel.showGoTravelDialog(getString(R.string.are_you_sure));

                } else {
                    Toast.makeText(getContext(), getString(R.string.select_phone), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //Handles clicking on cell phones
    @Override
    public void OnItemClick(Celular phone, int position) {
        phoneId = phone.getId();
        phoneText.setText(phone.getModel());
        Glide.with(getContext())
                .load(phone.getImageUrl())
                .into(phoneImage);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(List<Celular> celularList) {
        phoneAdapter = new PhoneAdapter(celularList, this);
        recyclerView.setAdapter(phoneAdapter);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    public void doCancelTask(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void doOkTask(Dialog dialog) {
        dialog.dismiss();
        OrderApi orderApi = new OrderApi(orderListeners);
        orderApi.setCelular(phoneId,preference.getOrderId());
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    OrderApi.OrderListeners orderListeners = new OrderApi.OrderListeners() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(Orden order) {
            phoneFragmentCallBack.handlePhoneFragment();
        }
    };
}
