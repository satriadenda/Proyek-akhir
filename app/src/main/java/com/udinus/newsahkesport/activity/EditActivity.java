package com.udinus.newsahkesport.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.udinus.newsahkesport.GlideApp;
import com.udinus.newsahkesport.R;
import com.udinus.newsahkesport.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity {

    private ImageView imgView;
    private EditText edtDescription, edtPrice;
    private RatingBar rbRating;
    private MaterialButton btnUpdate;
    private ActivityEditBinding uiBinding;

    private String product_desc;
    private String product_img_url;
    private String product_id;
    private int product_price;
    private int product_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiBinding = ActivityEditBinding.inflate(getLayoutInflater());

        edtDescription = uiBinding.edDetailDesc;
        edtPrice = uiBinding.edDetailPrice;
        rbRating = uiBinding.rbDetailRating;
        btnUpdate = uiBinding.btnDetailUpdate;
        imgView = uiBinding.ivDetailImg;

        product_desc = getIntent().getStringExtra("PRODUCT_DESCRIPTION");
        product_price = getIntent().getIntExtra("PRODUCT_PRICE", 0);
        product_rating = getIntent().getIntExtra("PRODUCT_RATING", 0);
        product_id = getIntent().getStringExtra("PRODUCT_ID");
        product_img_url = getIntent().getStringExtra("PRODUCT_IMG_URL");

        edtDescription.setText(product_desc);
        edtPrice.setText(String.valueOf(product_price));
        rbRating.setRating((float) product_rating);

        GlideApp.with(getApplicationContext())
                .load(product_img_url)
                .apply(new RequestOptions().override(400, 400))
                .into(imgView);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProductDetail(edtDescription.getText().toString(), Integer.parseInt(edtPrice.getText().toString()), (int)rbRating.getRating());
            }
        });

        setContentView(uiBinding.getRoot());
    }

    private void updateProductDetail(String newDescription, int newPrice, int newRating){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final CollectionReference colRef = firestore.collection("Products");

        colRef.whereEqualTo("id", product_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        colRef.document(document.getId()).update("description", newDescription);
                        colRef.document(document.getId()).update("price", newPrice);
                        colRef.document(document.getId()).update("rating", newRating);

                        Toast.makeText(getApplicationContext(), "Berhasil mengupdate detail produk.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal mengupdate detail produk.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}