package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAbandon extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_abandon);
        editText = findViewById(R.id.edtProductIds);
        findViewById(R.id.addToCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editText.getText().toString();
                if (!TextUtils.isEmpty(id)){
                    Map<String,Object> productId = new HashMap<>();
                    productId.put("ProductId",id);
                    MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("Add To Cart",productId);
                    ArrayList<String> ids = new ArrayList<String>();
                    ids.add(id);
                    MyApplication.getInstance().getClevertapDefaultInstance().addMultiValuesForKey("Product_ids",ids);
                }

            }
        });

        findViewById(R.id.charged).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editText.getText().toString();
                if (!TextUtils.isEmpty(id)){
                    Map<String,Object> productId = new HashMap<>();
                    productId.put("ProductId",id);
                    MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("Order Completed",productId);
                    ArrayList<String> ids = new ArrayList<String>();
                    ids.add(id);
                    MyApplication.getInstance().getClevertapDefaultInstance().removeMultiValuesForKey("Product_ids",ids);
                }

            }
        });

        findViewById(R.id.removeProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editText.getText().toString();
                if (!TextUtils.isEmpty(id)){
                    ArrayList<String> ids = new ArrayList<String>();
                    ids.add(id);
                    MyApplication.getInstance().getClevertapDefaultInstance().removeMultiValuesForKey("Product_ids",ids);
                }

            }
        });
    }
}