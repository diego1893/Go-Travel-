package com.monsordi.gotravel.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monsordi.gotravel.R;
import com.monsordi.gotravel.activities.NewOrderActivity;

public class FinishFragment extends Fragment implements View.OnClickListener {

    public FinishFragment() {}

    private FinishCallback finishCallback;

    public interface FinishCallback{
        void handleFinishCallback();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finish,container,false);
        root.setOnClickListener(this);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        finishCallback = (FinishCallback) activity;
    }

    @Override
    public void onClick(View view) {
        finishCallback.handleFinishCallback();
    }
}
