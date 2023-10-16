package ali.imanifard.raspberrycart.data.cart;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;


public class CartItem {
    int cart_item_id;
    int cartItemCount;
    ProductDTO productDTO;
    boolean changeCountProgressBarIsVisible = false;


    public CartItem(int cart_item_id,
                    int cartItemCount,
                    ProductDTO productDTO,
                    boolean changeCountProgressBarIsVisible) {

        this.cart_item_id = cart_item_id;
        this.cartItemCount = cartItemCount;
        this.productDTO = productDTO;
        this.changeCountProgressBarIsVisible = changeCountProgressBarIsVisible;
    }

    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    public boolean isChangeCountProgressBarIsVisible() {
        return changeCountProgressBarIsVisible;
    }

    public void setChangeCountProgressBarIsVisible(boolean changeCountProgressBarIsVisible) {
        this.changeCountProgressBarIsVisible = changeCountProgressBarIsVisible;
    }
}
