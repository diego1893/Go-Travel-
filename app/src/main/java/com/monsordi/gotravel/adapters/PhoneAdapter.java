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
import com.monsordi.gotravel.R;
import com.monsordi.gotravel.dto.Celular;

import java.util.List;

/**
 * Created by Soriano on 08/04/18.
 */

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {
    
    private List<Celular> phone;
    private OnItemClickListener listener;

    public PhoneAdapter(List<Celular> phone, OnItemClickListener listener) {
        this.phone = phone;
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void OnItemClick(Celular phone, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            RequestListener<String,GlideDrawable>{
        public ImageView phoneImage;
        public TextView phoneName;
        public ProgressBar progressBar;
        
        public ViewHolder(View itemView){
            super(itemView);
            this.phoneImage = itemView.findViewById(R.id.celular_image);
            this.phoneName = itemView.findViewById(R.id.celular_text);
            this.progressBar = itemView.findViewById(R.id.celular_progress);
        }

        public void bind(final Celular myCelular, final OnItemClickListener listener){

            phoneName.setText(myCelular.getModel());
            Glide.with(itemView.getContext())
                    .load(myCelular.getImageUrl())
                    .listener(this)
                    .into(phoneImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(myCelular,getAdapterPosition());
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_celular,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(phone.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return phone.size();
    }
}
