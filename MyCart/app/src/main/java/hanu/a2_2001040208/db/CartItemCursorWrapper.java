package hanu.a2_2001040208.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040208.models.CartItem;

public class CartItemCursorWrapper extends CursorWrapper {

    public CartItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CartItem getCartItem() {
        long id = getLong(getColumnIndex(CartItemDbSchema.CartItemsTable.Cols.ID));
        long productId = getLong(getColumnIndex(CartItemDbSchema.CartItemsTable.Cols.PRODUCT_ID));
        String thumbnail = getString(getColumnIndex(CartItemDbSchema.CartItemsTable.Cols.THUMBNAIL));
        String name = getString(getColumnIndex(CartItemDbSchema.CartItemsTable.Cols.NAME));
        long unitPrice = getLong(getColumnIndex(CartItemDbSchema.CartItemsTable.Cols.UNIT_PRICE));
        long quantity = getLong(getColumnIndex(CartItemDbSchema.CartItemsTable.Cols.QUANTITY));
        return new CartItem(id, productId, thumbnail, name, unitPrice, quantity);
    }

    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            CartItem cartItem = getCartItem();
            cartItems.add(cartItem);
            moveToNext();
        }
        return cartItems;
    }
}