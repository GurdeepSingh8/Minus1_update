package com.example.minus_1_update;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    Context context;
    ArrayList<Product> list;
    public ProductsAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = list.get(position);
       // Product1 product1 = list1.get(position);
        holder.mImage.setText(product.getImage());
        holder.mName.setText(product.getName());
        holder.mOldPrice.setText(String.valueOf(product.getOldPrice()));
        holder.mNewPrice.setText(product.getNewPrice());

        String tagsString = product.getTags().stream().map(Object::toString)
                .collect(Collectors.joining(", "));
        holder.mtags.setText(tagsString);

        String QuanString1 = product.getQuantity1().stream().map(Object::toString)
                .collect(Collectors.joining(", "));
        holder.mQuantityKg.setText(QuanString1);



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
                        String QuanString2 = product.getQuantity2().stream().map(Object::toString)
                                .collect(Collectors.joining(", "));

                        holder.mQuantityGms.setText(QuanString2);
                    }
                }
            }
        });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mImage, mName, mOldPrice, mNewPrice, mQuantityKg, mQuantityGms, mtags;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mtags = itemView.findViewById(R.id.pro_tags_row);
            mImage = itemView.findViewById(R.id.item_Product_Image);
            mName = itemView.findViewById(R.id.product_name);
            mNewPrice = itemView.findViewById(R.id.new_price);
            mOldPrice = itemView.findViewById(R.id.old_price);
            mQuantityKg = itemView.findViewById(R.id.quantity_spinner_kg);
            mQuantityGms = itemView.findViewById(R.id.quantity_spinner_gms);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Product product = list.get(getAdapterPosition());
            Intent intent = new Intent(context, UpdateProductActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        }
    }
}
