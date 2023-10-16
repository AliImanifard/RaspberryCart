package ali.imanifard.raspberrycart.data.cart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class CartResponse {

    @SerializedName("products")
    private List<ProductsInCart> products;
    @SerializedName("date")
    private String date;
    @SerializedName("userId")
    private int userid;
    @SerializedName("id")
    private int id;


    public List<ProductsInCart> getProducts() {
        return products;
    }

    public String getDate() {
        return date;
    }

    public int getUserid() {
        return userid;
    }

    public int getId() {
        return id;
    }

    public static class ProductsInCart {
        @SerializedName("quantity")
        private int quantity;
        @SerializedName("productId")
        private int productid;

        public int getQuantity() {
            return quantity;
        }

        public int getProductid() {
            return productid;
        }
    }
}
