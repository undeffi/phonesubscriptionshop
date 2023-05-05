package com.example.ppshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> {
    private ArrayList<ShoppingItem> mShopingItemData = new ArrayList<>();
    private ArrayList<ShoppingItem> mShopingItemDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;


    ShoppingItemAdapter(Context context, ArrayList<ShoppingItem> itemsData) {
        this.mShopingItemData = itemsData;
        this.mShopingItemDataAll = itemsData;
        this.mContext = context;
    }


    @Override
    public ShoppingItemAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ShoppingItemAdapter.ViewHolder holder, int position) {
        ShoppingItem currentItem = mShopingItemData.get(position);
        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Random random = new Random();
            int randomNumber = random.nextInt(2);
            Animation animation = AnimationUtils.loadAnimation(mContext, randomNumber == 1 ? R.anim.fade_in : R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return mShopingItemData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private ImageView mItemImage;
        private RatingBar mRatingBar;

        ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mItemImage = itemView.findViewById(R.id.itemImage);
            mPriceText = itemView.findViewById(R.id.price);
        }

        void bindTo(ShoppingItem currentItem){
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getInfo());
            mPriceText.setText(currentItem.getPrice());
            Picasso.get().load(currentItem.getImageResource()).into(mItemImage);
        }
    }
}
