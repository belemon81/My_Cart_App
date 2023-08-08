package hanu.a2_2001040208;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import hanu.a2_2001040208.adapters.ProductAdapter;
import hanu.a2_2001040208.helper.Helper;
import hanu.a2_2001040208.models.Product;

public class MainActivity extends AppCompatActivity {
    private List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        loadProducts("https://hanu-congnv.github.io/mpr-cart-api/products.json");
        RecyclerView productList = findViewById(R.id.productListRecycleView);
        productList.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(products, this);
        productList.setAdapter(productAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.viewCart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (productAdapter != null) {
            productAdapter.notifyDataSetChanged();
        }
    }

    public void loadProducts(String link) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Handler handler = new Handler(Looper.getMainLooper());
        Helper.executorService.execute(() -> {
            try {
                URL endpoint = new URL(link);
                HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() == 200) {
                    InputStream response = connection.getInputStream();
                    InputStreamReader responseReader = new InputStreamReader(response);
                    JsonReader jsonReader = new JsonReader(responseReader);
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        long id = 0;
                        String thumbnail = null;
                        String name = null;
                        String category = null;
                        long unitPrice = 0;
                        while (jsonReader.hasNext()) {
                            String key = jsonReader.nextName();
                            switch (key) {
                                case "id":
                                    id = jsonReader.nextLong();
                                    break;
                                case "thumbnail":
                                    thumbnail = jsonReader.nextString().trim();
                                    break;
                                case "name":
                                    name = jsonReader.nextString().trim();
                                    break;
                                case "category":
                                    category = jsonReader.nextString().trim();
                                    break;
                                case "unitPrice":
                                    unitPrice = jsonReader.nextLong();
                                    break;
                                default:
                                    jsonReader.skipValue();
                                    break;
                            }
                        }
                        Product product = new Product(id, thumbnail, name, category, unitPrice);
                        products.add(product);
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    jsonReader.close();
                    connection.disconnect();
                } else {
                    Log.e("error", "Error code: " + connection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("error", e.getLocalizedMessage());
            }
            handler.post(() -> {
                progressDialog.dismiss();
                populateProducts();
            });
        });
    }

    public void populateProducts() {
        RecyclerView productList = findViewById(R.id.productListRecycleView);
        productList.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(products, this);
        productList.setAdapter(productAdapter);
        SearchView searchEngine = findViewById(R.id.searchEngine);
        searchEngine.clearFocus();
        searchEngine.setSubmitButtonEnabled(true);
        searchEngine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            final List<Product> clone = products;
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!s.trim().isEmpty()) {
                    List<Product> filteredProducts = new ArrayList<>();
                    for (Product product : products) {
                        if (product.getName().toLowerCase().contains(s.trim().toLowerCase())) {
                            filteredProducts.add(product);
                        }
                    }
                    productAdapter.setProducts(filteredProducts);
                    if (!filteredProducts.isEmpty()) {
                        Helper.toastMessage(MainActivity.this, filteredProducts.size() + " products found.");
                    } else {
                        Helper.toastMessage(MainActivity.this, "No products found.");
                    }
                } else {
                    products = clone;
                    productAdapter.setProducts(products);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().equals("")) {
                    onQueryTextSubmit(s);
                    return true;
                }
                return false;
            }
        });
    }
}