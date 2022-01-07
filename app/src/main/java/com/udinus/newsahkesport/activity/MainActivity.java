package com.udinus.newsahkesport.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udinus.newsahkesport.R;
import com.udinus.newsahkesport.adapter.ProductAdapter;
import com.udinus.newsahkesport.databinding.ActivityMainBinding;
import com.udinus.newsahkesport.model.Product;
import com.udinus.newsahkesport.util.ProductClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductClickListener {

    private ActivityMainBinding uiBinding;
    private RecyclerView recview_products;
    private FirebaseFirestore mFirestore;
    private List<Product> productList;
    private ProductAdapter adapter;
    private String user_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mFirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("TOKO OLAHRAGA NEWSAHKE");

        user_role = getIntent().getStringExtra("USER_ROLE");
        recview_products = uiBinding.recviewProducts;
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, this);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager lm01
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recview_products.setLayoutManager(lm01);
        recview_products.setAdapter(adapter);
        setContentView(uiBinding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData(){
        productList.clear();
        CollectionReference docRef = mFirestore.collection("Products");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Product product;

                for (QueryDocumentSnapshot data:task.getResult()){
                    String id = data.get("id").toString();
                    String name = data.get("name").toString();
                    String description = data.get("description").toString();
                    int rating = Integer.parseInt(data.get("rating").toString());
                    int price = Integer.parseInt(data.get("price").toString());
                    String imgUrl = data.get("img").toString();
                    product = new Product(id, name, description, imgUrl, price, rating);
                    productList.add(product);
                }
                adapter = new ProductAdapter(productList, MainActivity.this);
                recview_products.setAdapter(adapter);
            }

        });
    }

    @Override
    public void onProductClicked(Product data) {
        Intent showDetail = new Intent(MainActivity.this, ProductDetail.class);
        showDetail.putExtra("PRODUCT_PRICE", data.getPrice());
        showDetail.putExtra("PRODUCT_DESCRIPTION", data.getDescription());
        showDetail.putExtra("PRODUCT_ID", data.getId());
        showDetail.putExtra("PRODUCT_RATING", data.getRating());
        showDetail.putExtra("PRODUCT_IMG_URL", data.getImgUrl());
        showDetail.putExtra("USER_ROLE", user_role);
        startActivity(showDetail);
    }
}