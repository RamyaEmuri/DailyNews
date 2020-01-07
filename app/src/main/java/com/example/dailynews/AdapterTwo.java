package com.example.dailynews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterTwo  extends RecyclerView.Adapter<AdapterTwo.MyViewHolder> {

    private ArrayList<Data2> data2;
    Context context;

    AdapterTwo (ArrayList<Data2> data1){
        this.data2 = data1;
    }

    public AdapterTwo(ArrayList<Data2> dataitems, Context context) {

        this.data2 = dataitems;
        this.context=context;
    }

    @NonNull
    @Override
    public AdapterTwo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_2, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTwo.MyViewHolder holder, int position) {

        Data2 objData = data2.get(position);

        Glide.with(holder.imageView.getContext())
                .load(objData.image)
                .into(holder.imageView);

        holder.textView.setText(objData.headline);
        holder.textView1.setText(objData.date);
    }

    @Override
    public int getItemCount() {
        return data2.size();
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
