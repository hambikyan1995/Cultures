package com.example.culturenearby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CulturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<CultureData> mList;
    private final ItemClickListener mItemClickListener;

    CulturesAdapter(ArrayList<CultureData> list, ItemClickListener listener) {
        mList = list;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_culture, parent, false);
        return new CulturesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CulturesViewHolder) holder).bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    interface ItemClickListener {
        void itemClick(CultureData data);
    }

    class CulturesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName;
        TextView tvInfo;
        TextView tvAddress;

        public CulturesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageCulture);
            tvName = itemView.findViewById(R.id.tvName);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }

        public void bind(CultureData cultureData) {
            Glide.with(itemView.getContext()).load(cultureData.imageUrl).into(imageView);
            tvName.setText(cultureData.name);
            tvInfo.setText(cultureData.info);
            tvAddress.setText(cultureData.address);

            itemView.setOnClickListener(view -> mItemClickListener.itemClick(cultureData));
        }
    }
}
