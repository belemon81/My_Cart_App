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
import hanu.a2_2001040208.models.Product;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Serializable {
    private List<Product> products;
    private Context context;
    private DbManager dbManager;

    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_item, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Product product) {
            ImageView productImg = itemView.findViewById(R.id.productImg);
            Handler handler = new Handler(Looper.getMainLooper());
            Helper.executorService.execute(() -> {
                Bitmap image = Helper.downloadImage(product.getThumbnail());
                handler.post(() -> {
                    if (image != null) {
                        productImg.setImageBitmap(image);
                    } else {
                        productImg.setImageResource(R.drawable.image_not_found);
                    }
                });
            });

            TextView productName = itemView.findViewById(R.id.productName);
            productName.setText(product.getName());

            TextView productPrice = itemView.findViewById(R.id.productPrice);
            productPrice.setText(MessageFormat.format("{0}", product.getUnitPrice()));

            ImageButton addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
            dbManager = DbManager.getInstance(context);
            CartItem cartItem = null;
            if (dbManager.getAllCartItems().size() != 0) {
                cartItem = dbManager.getCartItemByProductId(product.getId());
            }
            if (cartItem == null) {
                addToCartBtn.setImageResource(R.drawable.add_to_cart);
            } else {
                addToCartBtn.setImageResource(R.drawable.added_to_cart);
            }

            addToCartBtn.setOnClickListener(view -> {
                CartItem item = null;
                if (dbManager.getAllCartItems().size() != 0) {
                    item = dbManager.getCartItemByProductId(product.getId());
                }
                if (item == null) {
                    dbManager.addCartItem(new CartItem(product.getId(), product.getThumbnail(), product.getName(), product.getUnitPrice(), 1));
                    Helper.toastMessage(context, "Added product to your cart!");
                    addToCartBtn.animate().scaleYBy(-0.2f).scaleXBy(-0.2f).setDuration(100)
                            .withEndAction(() -> {
                                addToCartBtn.animate().scaleYBy(0.2f).scaleXBy(0.2f).setDuration(100);
                                addToCartBtn.setImageResource(R.drawable.added_to_cart);
                            });
                } else {
                    item.setQuantity(item.getQuantity() + 1);
                    dbManager.updateCartItem(item);
                    Helper.toastMessage(context, "Updated product in your cart!");
                    addToCartBtn.animate().scaleYBy(-0.2f).scaleXBy(-0.2f).setDuration(100)
                            .withEndAction(() -> addToCartBtn.animate().scaleYBy(0.2f).scaleXBy(0.2f).setDuration(100));
                }
            });
        }
    }
}
