package com.example.minus_1_update;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Empty;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ximage, xname, xnewPrice, xoldPrice;
    AutoCompleteTextView xquantity1, xquantity2, proTags;
    TagEditText tgedtxt;
    String txt;
    Button btnadd;
    FirebaseFirestore db;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        ximage = findViewById(R.id.image);
        xname = findViewById(R.id.name);
        xnewPrice = findViewById(R.id.newPrice);
        xoldPrice = findViewById(R.id.oldPrice);
        xquantity1 = findViewById(R.id.quantityKg);
        xquantity2 = findViewById(R.id.quantityGms);
        proTags = findViewById(R.id.pro_tags);
        tgedtxt = findViewById(R.id.tgedtxt);

        String[] proTagsArray = getResources().getStringArray(R.array.product_tags);
        ArrayAdapter<String> adapterTags = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, proTagsArray);
        proTags.setAdapter(adapterTags);
        tgedtxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                proTags.showDropDown();
                return false;
            }
        });

        proTags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt = proTags.getText().toString();
                tgedtxt.append(txt + " ");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        String[] quantitykg = getResources().getStringArray(R.array.quantity1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quantitykg);
        xquantity1.setAdapter(adapter1);

        xquantity1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xquantity1.showDropDown();
                return false;
            }
        });

        String[] quantitygms = getResources().getStringArray(R.array.quantity2);
        ArrayAdapter<String> adapeter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quantitygms);
        xquantity2.setAdapter(adapeter2);

        xquantity2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xquantity2.showDropDown();
                return false;
            }
        });

        btnadd = findViewById(R.id.addProduct);
        btnadd.setOnClickListener(this);
        findViewById(R.id.view_products).setOnClickListener(this);
    }

    private boolean validateInputs(TagEditText tgedtxt, String image, String name, String newPrice, int oldPrice, AutoCompleteTextView xquantity1) {

        if (Objects.requireNonNull(tgedtxt.getText()).toString().isEmpty()) {
            tgedtxt.setError("Tags required");
            tgedtxt.requestFocus();
            return true;
        }
        if (name.isEmpty()) {
            xname.setError("Name required");
            xname.requestFocus();
            return true;
        }
        if (image.isEmpty()) {
            ximage.setError("Image required");
            ximage.requestFocus();
            return true;
        }
        if (newPrice.isEmpty()) {
            xnewPrice.setError("Price required");
            xnewPrice.requestFocus();
            return true;
        }
        if (String.valueOf(oldPrice).isEmpty()) {
            xoldPrice.setError("Market Price required");
            xoldPrice.requestFocus();
            return true;
        }
        if (xquantity1.getText().toString().isEmpty()) {
            xquantity1.showDropDown();
            Toast.makeText(this, "Quantity required", Toast.LENGTH_SHORT).show();
            xquantity1.requestFocus();
            return true;
        }
        return false;
    }

    private void saveProduct() {

        String image = ximage.getText().toString().trim();
        String name = xname.getText().toString().trim();
        String newPrice = xnewPrice.getText().toString().trim();
        int oldPrice = Integer.parseInt(xoldPrice.getText().toString().trim());

        String proTagsInput = Objects.requireNonNull(tgedtxt.getText()).toString();
        String[] proTagsArray = proTagsInput.split("\\s* \\s*");
        List<String> Tags = Arrays.asList(proTagsArray);

        String xquantity1Input = xquantity1.getText().toString().trim();
        String[] xquantity1Array = xquantity1Input.split("\\s*,\\s*");
        List<String> quantity1 = Arrays.asList(xquantity1Array);

        String xquantity2Input = xquantity2.getText().toString().trim();
        String[] xquantity2Array = xquantity2Input.split("\\s*,\\s*");
        List<String> quantity2 = Arrays.asList(xquantity2Array);

        if (!validateInputs(tgedtxt, image, name, newPrice, oldPrice, xquantity1)) {
            CollectionReference dbProducts = db.collection("ProductStore");

            Product product = new Product(Tags, image, name, newPrice, oldPrice, quantity1, quantity2);
            Product1 product1 = new Product1(Tags, image, name, newPrice, oldPrice, quantity1);
            if (quantity2.get(0).isEmpty()) {
                dbProducts.add(product1)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                ximage.setText(null);
                                xname.setText(null);
                                tgedtxt.setText(null);
                                proTags.setText(null);
                                xnewPrice.setText(null);
                                xoldPrice.setText(null);
                                xquantity1.setText(null);
                                xquantity2.setText(null);
                                Toast.makeText(MainActivity.this, "Product Added", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                dbProducts.add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                ximage.setText(null);
                                xname.setText(null);
                                tgedtxt.setText(null);
                                proTags.setText(null);
                                xnewPrice.setText(null);
                                xoldPrice.setText(null);
                                xquantity1.setText(null);
                                xquantity2.setText(null);
                                Toast.makeText(MainActivity.this, "Product Added", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.view_products:
                startActivity(new Intent(this, ViewProducts.class));
                break;
            case R.id.addProduct:
                saveProduct();
                break;
        }
    }
}