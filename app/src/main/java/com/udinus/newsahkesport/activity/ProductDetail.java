package com.udinus.newsahkesport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.udinus.newsahkesport.GlideApp;
import com.udinus.newsahkesport.R;
import com.udinus.newsahkesport.databinding.ActivityProductDetailBinding;

public class ProductDetail extends AppCompatActivity {

    private ActivityProductDetailBinding uiBinding;
    private TextView tvDescription, tvPrice;
    private RatingBar rbRating;
    private ImageView imgDetail;
    private TextView tvCheckout;

    private String product_desc = "";
    private String product_img_url = "";
    private int product_price = 0;
    private int product_rating = 0;
    private String product_id = "";
    private String user_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());

        getSupportActionBar().setTitle("TOKO OLAHRAGA NEWSAHKE");

        tvDescription = uiBinding.tvDetailDesc;
        tvPrice = uiBinding.tvDetailPrice;
        rbRating = uiBinding.rbRating;
        imgDetail = uiBinding.ivDetailImg;
        tvCheckout = uiBinding.tvDetailCheckout;

        product_desc = getIntent().getStringExtra("PRODUCT_DESCRIPTION");
        product_price = getIntent().getIntExtra("PRODUCT_PRICE", 0);
        product_rating = getIntent().getIntExtra("PRODUCT_RATING", 0);
        product_id = getIntent().getStringExtra("PRODUCT_ID");
        product_img_url = getIntent().getStringExtra("PRODUCT_IMG_URL");
        user_role = getIntent().getStringExtra("USER_ROLE");

        tvDescription.setText(product_desc);
        tvPrice.setText("Rp "+product_price);
        rbRating.setRating((float)product_rating);

        GlideApp.with(getApplicationContext())
                .load(product_img_url)
                .apply(new RequestOptions().override(400, 400))
                .into(imgDetail);

        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProductDetail.this, SuccessActivity.class);
                i.putExtra("USER_ROLE", user_role);
                startActivity(i);
            }
        });

        setContentView(uiBinding.getRoot());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (user_role.equalsIgnoreCase("customer"))
            inflater.inflate(R.menu.customer_menu, menu);
        else
            inflater.inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_edit:
                Intent editDetail = new Intent(ProductDetail.this, EditActivity.class);
                editDetail.putExtra("PRODUCT_PRICE", product_price);
                editDetail.putExtra("PRODUCT_DESCRIPTION", product_desc);
                editDetail.putExtra("PRODUCT_ID", product_id);
                editDetail.putExtra("PRODUCT_RATING", product_rating);
                editDetail.putExtra("PRODUCT_IMG_URL", product_img_url);
                startActivity(editDetail);
                return true;

            case R.id.mnu_exit:
                Intent i = new Intent(ProductDetail.this, LoginActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}