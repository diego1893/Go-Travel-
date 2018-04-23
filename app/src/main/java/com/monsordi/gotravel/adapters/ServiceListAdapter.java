package com.monsordi.gotravel.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.dto.PrestadorServicio;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Soriano on 08/04/18.
 */

public class ServiceListAdapter extends ArrayAdapter<PrestadorServicio> {

    private Context context;

    public ServiceListAdapter(Context context,List<PrestadorServicio> serviceList){
        super(context, R.layout.row_service_list,serviceList);
        this.context = context;
    }

    static class ViewHolder{
        @BindView(R.id.service_list_name)
        TextView nameTextView;
        @BindView(R.id.service_list_image)
        CircularImageView imageView;
        @BindView(R.id.service_list_type)
        TextView typeTextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Binding views
        ViewHolder viewHolder;
        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_service_list,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        PrestadorServicio currentService = getItem(position);

        //Sets fields to views
        viewHolder.nameTextView.setText(currentService.getName());
        viewHolder.typeTextView.setText(currentService.getService());
        Glide.with(parent.getContext())
                .load(currentService.getImageUrl())
                .into(viewHolder.imageView);

        return convertView;
    }
}
