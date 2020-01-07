package com.example.dailynews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;

import java.util.ArrayList;

public class AdapterOne extends RecyclerView.Adapter<AdapterOne.MyViewHolder> {


    private ArrayList<Data> data;
    Context context;


    public AdapterOne(ArrayList<Data> dataitems, Context context) {

        this.data = dataitems;
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_1, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        Data objData = data.get(position);

        Glide.with(holder.imageView.getContext())
                .load(objData.image)
                .into(holder.imageView);

        holder.textView.setText(objData.headline);
        holder.textView1.setText(objData.date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailsPage.class);
                intent.putExtra(DetailsPage.EXTRA_IMAGE, data.get(holder.getAdapterPosition()).image);
                intent.putExtra(DetailsPage.EXTRA_HEADLINE, data.get(holder.getAdapterPosition()).headline);
                intent.putExtra(DetailsPage.EXTRA_DATA, data.get(holder.getAdapterPosition()).date);
                intent.putExtra(DetailsPage.EXTRA_DESCRIPTION, data.get(holder.getAdapterPosition()).description);
                context.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView, textView1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.headImage);
            textView = itemView.findViewById(R.id.headLineText);
            textView1 = itemView.findViewById(R.id.dateText);
        }
    }
}
