package hanu.a2_2001040208.db;

public class CartItemDbSchema {
    public static final class CartItemsTable {
        public static final String NAME = "cart_items";

        public static final class Cols {
            public static final String ID = "id";
            public static final String PRODUCT_ID = "product_id";
            public static final String THUMBNAIL = "thumbnail";
            public static final String NAME = "name";
            public static final String UNIT_PRICE = "unit_price";
            public static final String QUANTITY = "quantity";
        }
    }
}
