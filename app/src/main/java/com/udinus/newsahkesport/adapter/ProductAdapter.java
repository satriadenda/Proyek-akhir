package com.udinus.newsahkesport.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.newsahkesport.GlideApp;
import com.udinus.newsahkesport.R;
import com.udinus.newsahkesport.model.Product;
import com.udinus.newsahkesport.util.ProductClickListener;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>  {

    private List<Product> data = new ArrayList<>();
    private FirebaseStorage mStorage;
    private StorageReference storageRef;
    private ProductClickListener clickListener;

    public ProductAdapter(List<Product> data, ProductClickListener clickListener){
        this.data = data;
        this.clickListener = clickListener;
        mStorage = FirebaseStorage.getInstance();
        storageRef = mStorage.getReferenceFromUrl("gs://newsahkesport.appspot.com/").child("products");
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = data.get(position);
        holder.bind(product);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onProductClicked(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPreview;
        public TextView tvName;
        public TextView tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPreview = itemView.findViewById(R.id.ivPreview);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);

        }

        public void bind(Product product){
            tvName.setText(product.getName());
            tvPrice.setText("Rp "+product.getPrice());
            GlideApp.with(itemView)
                    .load(product.getImgUrl())
                    .apply(new RequestOptions().override(200, 200))
                    .into(ivPreview);
        }

    }
}