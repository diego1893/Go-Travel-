package com.monsordi.gotravel.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.adapters.ServiceAdapter;
import com.monsordi.gotravel.dto.Orden;
import com.monsordi.gotravel.dto.PrestadorServicio;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailPeopleFragment extends Fragment implements ServiceAdapter.OnItemClickListener {

    @BindView(R.id.detail_counselor_image)
    CircularImageView counselorImage;
    @BindView(R.id.detail_counselor_name)
    TextView counselorName;
    @BindView(R.id.detail_counselor_email)
    TextView counselorEmail;
    @BindView(R.id.detail_counselor_description)
    TextView counselorDescription;

    @BindView(R.id.detail_attendant_image)
    CircularImageView attendantImage;
    @BindView(R.id.detail_attendant_name)
    TextView attendantName;
    @BindView(R.id.detail_attendant_email)
    TextView attendantEmail;
    @BindView(R.id.detail_attendant_description)
    TextView attendantDescription;

    private Orden order;

    public DetailPeopleFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_people,container,false);
        ButterKnife.bind(this,root);

        order = (Orden) getArguments().getSerializable(getString(R.string.order_key));
        initializeUi();

        return root;
    }

    private void initializeUi() {
        if(order.getAsesor() != null) {
            counselorName.setText(order.getAsesor().getName());
            counselorEmail.setText(order.getAsesor().getEmail());
            counselorDescription.setText(order.getAsesor().getDescription());
            Glide.with(getContext())
                    .load(order.getAsesor().getImageUrl())
                    .into(counselorImage);
        }

        if(order.getEncargado() != null) {
            attendantName.setText(order.getEncargado().getName());
            attendantEmail.setText(order.getEncargado().getEmail());
            attendantDescription.setText(order.getEncargado().getDescription());
            Glide.with(getContext())
                    .load(order.getEncargado().getImageUrl())
                    .into(attendantImage);
        }

    }

    @Override
    public void OnItemClick(PrestadorServicio service, int position) {}
}
