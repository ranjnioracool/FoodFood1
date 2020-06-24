package com.example.foodfood1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    @NonNull
    Context mContext;
    private List<AddToCart> maddtocart;
    public int grandtotal;
    public CartAdapter(Context mContext,List<AddToCart> maddtocart){
        this.maddtocart=maddtocart;
        this.mContext=mContext;
    }
    @Override

    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View v=LayoutInflater.from(mContext).inflate(R.layout.cart_item,parent,false);
       return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
     AddToCart addToCartCurrent=maddtocart.get(position);
     holder.tvname.setText(addToCartCurrent.getNameOfFood());
     holder.tvrate.setText(addToCartCurrent.getRateOfFood());
     holder.tvquantity.setText(addToCartCurrent.getQuantityOfFood());
     holder.tvamount.setText(addToCartCurrent.getAmount());

    }

    @Override
    public int getItemCount() {
        return maddtocart.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        public TextView tvname,tvrate,tvquantity,tvamount;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname=itemView.findViewById(R.id.tvnamecart);
            tvrate=itemView.findViewById(R.id.tvratecart);
            tvquantity=itemView.findViewById(R.id.tvqtycart);
            tvamount=itemView.findViewById(R.id.tvamtcart);
        }
    }
}
