package com.monsordi.gotravel.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.dto.PrestadorServicio;

import java.util.List;

/**
 * Created by Soriano on 08/04/18.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    
    private List<PrestadorServicio> serviceList;
    private OnItemClickListener listener;

    public ServiceAdapter(List<PrestadorServicio> serviceList, OnItemClickListener listener) {
        this.serviceList = serviceList;
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void OnItemClick(PrestadorServicio service, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            RequestListener<String,GlideDrawable>{
        public CircularImageView serviceImage;
        public TextView serviceName;
        public TextView serviceType;
        public ProgressBar progressBar;
        
        public ViewHolder(View itemView){
            super(itemView);
            this.serviceImage = itemView.findViewById(R.id.service_image);
            this.serviceName = itemView.findViewById(R.id.service_text);
            this.serviceType = itemView.findViewById(R.id.service_type);
            this.progressBar = itemView.findViewById(R.id.service_progress);
        }

        public void bind(final PrestadorServicio myService, final OnItemClickListener listener){

            serviceName.setText(myService.getName());
            serviceType.setText(myService.getService());
            Glide.with(itemView.getContext())
                    .load(myService.getImageUrl())
                    .listener(this)
                    .into(serviceImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(myService,getAdapterPosition());
                }
            });
        }

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_services,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(serviceList.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }
}
