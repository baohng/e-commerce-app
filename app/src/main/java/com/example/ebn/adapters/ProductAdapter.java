package com.example.ebn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebn.R;
import com.example.ebn.entities.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context mContext;
    private List<Product> mListProduct;

    public ProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Product> list){
        this.mListProduct = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mListProduct.get(position);
        if (product == null) {
            return;
         }

        holder.imageProduct.setImageResource(product.getResourceImage());
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductCost.setText(product.getCost());
        holder.imageStatus.setImageResource(product.getResourceStatus());
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageProduct, imageStatus;
        private TextView textViewProductName, textViewProductCost;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProduct = itemView.findViewById(R.id.imageProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductCost = itemView.findViewById(R.id.textViewProductCurrency);
            imageStatus = itemView.findViewById(R.id.imageStatus);

        }
    }
}
