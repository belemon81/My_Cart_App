package hanu.a2_2001040208.models;

public class CartItem {
    private long id;
    private long productId;
    private String thumbnail;
    private String name;
    private long unitPrice;
    private long quantity;

    public CartItem(long id, long productId, String thumbnail, String name, long unitPrice, long quantity) {
        this.id = id;
        this.productId = productId;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public CartItem(long productId, String thumbnail, String name, long unitPrice, long quantity) {
        this.productId = productId;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getSumPrice() {
        return quantity * unitPrice;
    }
}
