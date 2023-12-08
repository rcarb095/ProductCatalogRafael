package com.example.productcatalog;


import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPrice;
    Button buttonAddProduct;
    ListView listViewProducts;

    List<Product> products;

    ArrayList<Product> databaseProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseProducts = new ArrayList<>();
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        listViewProducts = (ListView) findViewById(R.id.listViewProducts);
        buttonAddProduct = (Button) findViewById(R.id.addButton);

        products = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = products.get(i);
                showUpdateDeleteDialog(i, product.getProductName());
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    private void showUpdateDeleteDialog(final int productId, String productName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextPrice  = (EditText) dialogView.findViewById(R.id.editTextPrice);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);

        dialogBuilder.setTitle(productName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                double price = Double.parseDouble(String.valueOf(editTextPrice.getText().toString()));
                if (!TextUtils.isEmpty(name)) {
                    updateProduct(productId, name, price);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(productId);
                b.dismiss();
            }
        });
    }

    private void updateProduct(int id, String name, double price) {
        //getting the specified product reference

        //updating product
        Product product = new Product(databaseProducts.get(id).getId(), name, price);
        databaseProducts.remove(id);
        databaseProducts.add(product);
        updateList();
        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();
    }

    private boolean deleteProduct(int id) {
        //getting the specified product reference
        databaseProducts.remove(id);
        updateList();
        //removing prodct
        Toast.makeText(getApplicationContext(), "Product Deleted", Toast.LENGTH_LONG).show();
        updateList();
        return true;
    }

    private void addProduct() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        double price = Double.parseDouble(String.valueOf(editTextPrice.getText().toString()));

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            Random random = new Random();

            String id = String.valueOf(random.nextInt());

            //creating an Product Object
            Product product = new Product(id, name, price);

            //Saving the Product
            databaseProducts.add(product);

            //setting edittext to blank again
            editTextName.setText("");
            editTextPrice.setText("");

            updateList();
            //displaying a success toast
            Toast.makeText(this, "Product added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
    public void updateList(){
        products.clear();
        for(Product product: databaseProducts){
            products.add(product);
        }
        ProductList productList = new ProductList(MainActivity.this, products);
        listViewProducts.setAdapter(productList);
    }
}