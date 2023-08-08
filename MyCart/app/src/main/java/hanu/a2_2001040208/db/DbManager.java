package hanu.a2_2001040208.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_2001040208.models.CartItem;

public class DbManager {
    private static final String INSERT_CART_ITEM_STMT = "INSERT INTO " + CartItemDbSchema.CartItemsTable.NAME + "(product_id, thumbnail, name, unit_price, quantity) VALUES (?,?,?,?,?)";
    private static final String UPDATE_CART_ITEM_STMT = "UPDATE " + CartItemDbSchema.CartItemsTable.NAME + " SET product_id = ?, thumbnail = ?, name = ?, unit_price = ?, quantity = ? WHERE id = ?";
    private static DbManager instance;
    private final DbHelper dbHelper;
    private final SQLiteDatabase db;

    private DbManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // singleton
    public static DbManager getInstance(Context context) {
        if (instance == null) {
            instance = new DbManager(context);
        }
        return instance;
    }

    public List<CartItem> getAllCartItems() {
        String sql = "SELECT * FROM cart_items";
        Cursor cursor = db.rawQuery(sql, null);
        CartItemCursorWrapper cursorWrapper = new CartItemCursorWrapper(cursor);
        return cursorWrapper.getCartItems();
    }

    public CartItem getCartItemByProductId(long id) {
        String sql = "SELECT * FROM cart_items WHERE product_id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        CartItemCursorWrapper cursorWrapper = new CartItemCursorWrapper(cursor);
        boolean hasNext = cursorWrapper.moveToNext();
        if (hasNext) {
            return cursorWrapper.getCartItem();
        } else {
            return null;
        }
    }

    public boolean addCartItem(CartItem cartItem) {
        SQLiteStatement statement = db.compileStatement(INSERT_CART_ITEM_STMT);
        statement.bindLong(1, cartItem.getProductId());
        statement.bindString(2, cartItem.getThumbnail());
        statement.bindString(3, cartItem.getName());
        statement.bindLong(4, cartItem.getUnitPrice());
        statement.bindLong(5, cartItem.getQuantity());
        long id = statement.executeInsert();
        if (id > 0) {
            cartItem.setId(id);
            return true;
        }
        return false;
    }

    public boolean updateCartItem(CartItem cartItem) {
        SQLiteStatement statement = db.compileStatement(UPDATE_CART_ITEM_STMT);
        statement.bindLong(1, cartItem.getProductId());
        statement.bindString(2, cartItem.getThumbnail());
        statement.bindString(3, cartItem.getName());
        statement.bindLong(4, cartItem.getUnitPrice());
        statement.bindLong(5, cartItem.getQuantity());
        statement.bindLong(6, cartItem.getId());
        int result = statement.executeUpdateDelete();
        return result > 0;
    }

    public boolean deleteCartItem(long id) {
        int result = db.delete(CartItemDbSchema.CartItemsTable.NAME, "id = ?", new String[]{id + ""});
        return result > 0;
    }
}

