package hanu.a2_2001040208;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_2001040208.adapters.CartItemAdapter;
import hanu.a2_2001040208.db.DbManager;
import hanu.a2_2001040208.models.CartItem;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        DbManager dbManager = DbManager.getInstance(this);
        List<CartItem> cartItems = dbManager.getAllCartItems();

        RecyclerView cartItemList = findViewById(R.id.myCartRecycleView);
        cartItemList.setLayoutManager(new LinearLayoutManager(this));
        TextView totalPrice = findViewById(R.id.totalPrice);
        CartItemAdapter cartItemAdapter = new CartItemAdapter(cartItems, this, totalPrice);
        cartItemList.setAdapter(cartItemAdapter);
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
        return false;
    }
}