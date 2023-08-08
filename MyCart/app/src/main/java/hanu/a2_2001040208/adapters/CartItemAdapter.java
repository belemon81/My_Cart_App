package hanu.a2_2001040208.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

import hanu.a2_2001040208.R;
import hanu.a2_2001040208.db.DbManager;
import hanu.a2_2001040208.helper.Helper;
import hanu.a2_2001040208.models.CartItem;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder> implements Serializable {
    private List<CartItem> cartItems;
    private Context context;
    private DbManager dbManager;
    private TextView totalPrice;

    public CartItemAdapter(List<CartItem> cartItems, Context context, TextView totalPrice) {
        this.cartItems = cartItems;
        this.context = context;
        this.totalPrice = totalPrice;
        totalPrice.setText(MessageFormat.format("{0}", getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public long getTotalPrice() {
        dbManager = DbManager.getInstance(context);
        long value = 0;
        for (CartItem cartItem : cartItems) {
            value += cartItem.getSumPrice();
        }
        return value;
    }

    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
        return new CartItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        public CartItemHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(CartItem cartItem) {
            dbManager = DbManager.getInstance(context);
            ImageView itemImg = itemView.findViewById(R.id.itemImg);
            Handler handler = new Handler(Looper.getMainLooper());
            Helper.executorService.execute(() -> {
                Bitmap image = Helper.downloadImage(cartItem.getThumbnail());
                handler.post(() -> {
                    if (image != null) {
                        itemImg.setImageBitmap(image);
                    } else {
                        itemImg.setImageResource(R.drawable.image_not_found);
                    }
                });
            });

            TextView itemName = itemView.findViewById(R.id.itemName);
            itemName.setText(cartItem.getName());

            TextView itemPrice = itemView.findViewById(R.id.itemPrice);
            itemPrice.setText(MessageFormat.format("{0}", cartItem.getUnitPrice()));

            TextView itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemQuantity.setText(MessageFormat.format("{0}", cartItem.getQuantity()));

            TextView itemTotalPrice = itemView.findViewById(R.id.itemTotalPrice);
            itemTotalPrice.setText(MessageFormat.format("{0}", cartItem.getSumPrice()));

            ImageButton plusBtn = itemView.findViewById(R.id.plusBtn);
            ImageButton minusBtn = itemView.findViewById(R.id.minusBtn);

            plusBtn.setOnClickListener(view -> {
                int index = cartItems.indexOf(cartItem);
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                dbManager.updateCartItem(cartItem);
                cartItems.set(index, cartItem);
                totalPrice.setText(MessageFormat.format("{0}", getTotalPrice()));
                notifyDataSetChanged();
            });

            minusBtn.setOnClickListener(view -> {
                long newQuantity = cartItem.getQuantity() - 1;
                int index = cartItems.indexOf(cartItem);
                if (newQuantity <= 0) {
                    dbManager.deleteCartItem(cartItem.getId());
                    cartItems.remove(index);
                } else {
                    cartItem.setQuantity(newQuantity);
                    dbManager.updateCartItem(cartItem);
                    cartItems.set(index, cartItem);
                }
                totalPrice.setText(MessageFormat.format("{0}", getTotalPrice()));
                notifyDataSetChanged();
            });
        }
    }
}
