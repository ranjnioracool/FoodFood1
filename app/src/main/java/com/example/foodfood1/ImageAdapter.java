package com.example.foodfood1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> muploads;
    public ImageAdapter(Context context,List<Upload> uploads){
        mContext=context;
        muploads=uploads;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
       return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final Upload uploadCurrent=muploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewPrice.setText(uploadCurrent.getPrice());
        Picasso.with(mContext).load(uploadCurrent.getmImageUri()).placeholder(R.drawable.foodfoo).fit().into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,OpenDetailsActivity.class);
                intent.putExtra("name",uploadCurrent.getName());
                intent.putExtra("price",uploadCurrent.getPrice());
                intent.putExtra("image",uploadCurrent.getmImageUri());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return muploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName,textViewPrice;
        public ImageView imageView;
        public CardView cardView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.tvname);
            textViewPrice=itemView.findViewById(R.id.tvprice);
            imageView=itemView.findViewById(R.id.image_view_uploads);
            cardView=itemView.findViewById(R.id.card_view);

        }
    }
}
