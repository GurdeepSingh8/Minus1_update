package com.example.minus_1_update;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UpdateProductActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ximage, xname, xnewPrice, xoldPrice;
    AutoCompleteTextView xquantity1, xquantity2, proTags;
    TagEditText tgedtxt;
    String txt;
    Button btndelete, btnupdate;
    FirebaseFirestore db;
    private Product product;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        ximage = findViewById(R.id.image);
        xname = findViewById(R.id.name);
        xnewPrice = findViewById(R.id.newPrice);
        xoldPrice = findViewById(R.id.oldPrice);
        xquantity1 = findViewById(R.id.quantityKg);
        xquantity2 = findViewById(R.id.quantityGms);
        proTags = findViewById(R.id.pro_tags);
        tgedtxt = findViewById(R.id.tgedtxt);

        btndelete = findViewById(R.id.deleteProduct);
        btnupdate = findViewById(R.id.updateProduct);

        btnupdate.setOnClickListener(this);
        btndelete.setOnClickListener(this);

        product = (Product) getIntent().getSerializableExtra("product");
        db = FirebaseFirestore.getInstance();

        ximage.setText(product.getImage());
        xname.setText(product.getName());
        xnewPrice.setText(product.getNewPrice());
        xoldPrice.setText(String.valueOf(product.getOldPrice()));

        String tagsString = product.getTags().stream().map(Object::toString)
                .collect(Collectors.joining(" "));
        tgedtxt.setText(MessageFormat.format("{0} ", tagsString));

        String QuanString1 = product.getQuantity1().stream().map(Object::toString)
                .collect(Collectors.joining(", "));
        xquantity1.setText(QuanString1);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference codesRef = rootRef.collection("ProductStore").document(product.getId());
        codesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> listt = new ArrayList<>();
                    Map<String, Object> map = task.getResult().getData();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        listt.add(entry.getKey());
                        Log.d("TAG", entry.getKey());


                    }
                    //Do what you want to do with your list
                    if (listt.toString().contains("quantity2")){
                        String QuanString2 = product.getQuantity2().stream().map(Objects::toString)
                                .collect(Collectors.joining(", "));
                        xquantity2.setText(QuanString2);
                    }
                }
            }
        });



        String [] proTagsArray = getResources().getStringArray(R.array.product_tags);
        ArrayAdapter<String> adapterTags = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,proTagsArray);
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
                tgedtxt.append(txt+" ");

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String[] xquantitykg = getResources().getStringArray(R.array.quantity1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, xquantitykg);
        xquantity1.setAdapter(adapter1);

        xquantity1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xquantity1.showDropDown();
                return false;
            }
        });

        String[] xquantitygms = getResources().getStringArray(R.array.quantity2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, xquantitygms);
        xquantity2.setAdapter(adapter2);

        xquantity2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xquantity2.showDropDown();
                return false;
            }
        });
    }

    private boolean validateInputs(TagEditText tgedtxt, String image, String name, String newPrice, int oldPrice, AutoCompleteTextView xquantity1) {

        if (Objects.requireNonNull(tgedtxt.getText()).toString().isEmpty()) {
            tgedtxt.setError("Tags required");
            proTags.showDropDown();
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
            Toast.makeText(this, "Quantity Required", Toast.LENGTH_SHORT).show();
            xquantity1.showDropDown();
            xquantity1.requestFocus();
            return true;
        }

        return false;
    }

    private void updateProduct() {
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
            Product1 p1 = new Product1(Tags, image, name, newPrice, oldPrice, quantity1);
            Product p = new Product(Tags, image, name, newPrice, oldPrice, quantity1,quantity2);
            if (quantity2.get(0).isEmpty()){

                db.collection("ProductStore").document(product.getId())
                        .update(
                                "tags", p1.getTags(),
                                "image", p1.getImage(),
                                "name", p1.getName(),
                                "newPrice", p1.getNewPrice(),
                                "oldPrice", p1.getOldPrice(),
                                "quantity1", p1.getQuantity1()
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateProductActivity.this, "Product Updated", Toast.LENGTH_LONG).show();
                                finish();
                                proTags.setText(null);
                                tgedtxt.setText(null);
                                ximage.setText(null);
                                xname.setText(null);
                                xnewPrice.setText(null);
                                xoldPrice.setText(null);
                                xquantity1.setText(null);
                                xquantity2.setText(null);
                                startActivity(new Intent(UpdateProductActivity.this, ViewProducts.class));
                            }
                        });

            }else {
                db.collection("ProductStore").document(product.getId())
                        .update(
                                "tags", p.getTags(),
                                "image", p1.getImage(),
                                "name", p.getName(),
                                "newPrice", p.getNewPrice(),
                                "oldPrice", p.getOldPrice(),
                                "quantity1", p.getQuantity1(),
                                "quantity2", p.getQuantity2()
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateProductActivity.this, "Product Updated", Toast.LENGTH_LONG).show();
                                finish();
                                proTags.setText(null);
                                tgedtxt.setText(null);
                                ximage.setText(null);
                                xname.setText(null);
                                xnewPrice.setText(null);
                                xoldPrice.setText(null);
                                xquantity1.setText(null);
                                xquantity2.setText(null);
                                startActivity(new Intent(UpdateProductActivity.this, ViewProducts.class));
                            }
                        });
            }

        }
    }

    private void deleteProduct() {
        db.collection("ProductStore").document(product.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateProductActivity.this, "Product deleted", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(UpdateProductActivity.this, ViewProducts.class));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateProduct:
                updateProduct();
                break;
            case R.id.deleteProduct:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("अगर धोके से दबा है तो अभी बतायें !");
                builder.setMessage("उड़ा हुआ Product वापस नहीं आएगा!");
                builder.setPositiveButton("Product उड़ायें", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct();
                    }
                });
                builder.setNegativeButton("धोके से", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
                break;
        }
    }
}