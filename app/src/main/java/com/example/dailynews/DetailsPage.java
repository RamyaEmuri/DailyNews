package com.example.dailynews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailsPage extends AppCompatActivity {

    public static final String EXTRA_IMAGE = "image";
    public static final String EXTRA_HEADLINE = "headline";
    public static final String EXTRA_DATA = "date";
    public static final String EXTRA_DESCRIPTION = "description";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);

        ImageView imageView = findViewById(R.id.iconImg);
        Glide.with(this)
                .load(R.drawable.person3)
                .placeholder(R.drawable.person3)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        Intent i = getIntent();
        TextView textView = findViewById(R.id.textViewTitle);
        TextView textView1 = findViewById(R.id.textViewDate);
        TextView textView2 = findViewById(R.id.textViewDes);


        String headline = i.getStringExtra(EXTRA_HEADLINE);
        String date = i.getStringExtra(EXTRA_DATA);
        String description = i.getStringExtra(EXTRA_DESCRIPTION);
        String image = i.getStringExtra(EXTRA_IMAGE);

        textView.setText(headline);
        textView1.setText(date);
        textView2.setText(description);
        loadBackdrop(image);


    }

    private void loadBackdrop (String image){
        final ImageView imageView = findViewById(R.id.targetImg);
        Glide.with(this).load(image).into(imageView);
    }
}











//        Integer image = i.getIntExtra("image", 0);
//        ImageView imageView1 = findViewById(R.id.targetImg);
//        imageView1.setImageResource(image);
//
//        String headline = i.getStringExtra("headline");
//        TextView textViewheadLine = findViewById(R.id.textViewTitle);
//        textViewheadLine.setText(headline);
//
//        String date = i.getStringExtra("date");
//        TextView textViewDate = findViewById(R.id.textViewDate);
//        textViewDate.setText(date);
//
//        String description = i.getStringExtra("description");
//        TextView textViewDes = findViewById(R.id.textViewDes);
//        textViewDes.setText(description);