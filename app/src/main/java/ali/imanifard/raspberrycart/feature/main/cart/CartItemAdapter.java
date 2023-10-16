package ali.imanifard.raspberrycart.feature.main.cart;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.List;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.data.db.CartEntity;
import ali.imanifard.raspberrycart.services.image_loading.ImageLoadingService;

public class CartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_TYPE_CART_ITEM = 657;
    private static final int VIEW_TYPE_PURCHASE_DETAIL = 291;
    private final List<CartEntity> cartEntities;
    private final ImageLoadingService imageLoadingService;
    private final CartItemViewCallbacks cartItemViewCallbacks;
    private final double totalprice = 0.0;
    public PurchaseDetailTotalPrice purchaseDetailTotalPrice = null;
    private ProgressBar pbChangeCount;
    private TextView tvCartItemCount;


    public CartItemAdapter(List<CartEntity> cartEntities,
                           ImageLoadingService imageLoadingService,
                           CartItemViewCallbacks cartItemViewCallbacks) {

        this.cartEntities = cartEntities;
        this.imageLoadingService = imageLoadingService;
        this.cartItemViewCallbacks = cartItemViewCallbacks;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CART_ITEM)
            return new CartItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false));
        else
            return new PurchaseDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CartItemViewHolder)
            ((CartItemViewHolder) holder).bindProduct(cartEntities.get(position));
        else if (holder instanceof PurchaseDetailViewHolder) {

            if (purchaseDetailTotalPrice != null) {
                ((PurchaseDetailViewHolder) holder).bind(purchaseDetailTotalPrice.getTotalPrice());
            }

        }

    }

    @Override
    public int getItemCount() {
        return cartEntities.size() + 1;   // +1 marboot be purchase detail ast.
    }

    @Override
    public int getItemViewType(int position) {
        if (position == cartEntities.size())
            return VIEW_TYPE_PURCHASE_DETAIL;
        else
            return VIEW_TYPE_CART_ITEM;
    }

    public void removeCartItem(CartEntity cartEntity) {
        int index = cartEntities.indexOf(cartEntity);
        if (index > -1) {
            cartEntities.remove(index);
            //notifyItemRemoved(index);
            notifyDataSetChanged();
        }
    }

    public void increaseCount(CartEntity cartEntity) {
        int index = cartEntities.indexOf(cartEntity);
        if (index > -1) {
            cartEntities.get(index).setChangeCountProgressBarIsVisible(false);
            pbChangeCount.setVisibility(View.GONE);
            tvCartItemCount.setVisibility(View.VISIBLE);
            //notifyItemChanged(index);
            notifyDataSetChanged();
        }
    }

    public void decreaseCount(CartEntity cartEntity) {
        int index = cartEntities.indexOf(cartEntity);
        if (index > -1) {
            cartEntities.get(index).setChangeCountProgressBarIsVisible(false);
            pbChangeCount.setVisibility(View.GONE);
            tvCartItemCount.setVisibility(View.VISIBLE);
            //notifyItemChanged(index);
            notifyDataSetChanged();
        }
    }

    public List<CartEntity> getCartEntities() {
        return cartEntities;
    }

    public double getTotalPrice() {
        return totalprice;
    }

    public void setPurchaseDetailTotalPrice(PurchaseDetailTotalPrice purchaseDetailTotalPrice) {
        this.purchaseDetailTotalPrice = purchaseDetailTotalPrice;
        //notifyItemChanged(getItemCount() - 1); or +1
        notifyDataSetChanged();
    }


    public interface CartItemViewCallbacks {
        void onRemoveCartItemButtonClick(CartEntity cartEntity);

        void onIncreaseCartItemButtonClick(CartEntity cartEntity);

        void onDecreaseCartItemButtonClick(CartEntity cartEntity);

        void onImageCartItemButtonClick(CartEntity cartEntity);

    }

    public static class PurchaseDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tvTotalPrice, tvShippingPrice, tvPayablePrice;

        public PurchaseDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            tvShippingPrice = itemView.findViewById(R.id.tv_shipping_price);
            tvPayablePrice = itemView.findViewById(R.id.tv_payable_price);
        }

        public void bind(double totalPrice) {

            double shippingPrice, payablePrice;


            shippingPrice = 10.0;
            payablePrice = totalPrice + shippingPrice;

            tvTotalPrice.setText(new DecimalFormat("0.00").format(totalPrice));
            tvShippingPrice.setText(new DecimalFormat("0.00").format(shippingPrice));
            tvPayablePrice.setText(new DecimalFormat("0.00").format(payablePrice));
        }
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView sdvProductImage;
        TextView tvProductTitle, tvProductPriceBeforeDiscount,
                tvProductPrice, tvRemoveFromCart;

        ImageButton btnIncrease, btnDecrease;


        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            sdvProductImage = itemView.findViewById(R.id.sdv_product_image);
            tvProductTitle = itemView.findViewById(R.id.tv_product_title);
            tvProductPriceBeforeDiscount = itemView.findViewById(R.id.tv_product_price_before_discount);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvCartItemCount = itemView.findViewById(R.id.tv_cart_item_count);
            tvRemoveFromCart = itemView.findViewById(R.id.tv_remove_from_cart);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            pbChangeCount = itemView.findViewById(R.id.pb_change_count);

        }

        public void bindProduct(CartEntity cartEntity) {
            imageLoadingService.load(sdvProductImage, cartEntity.getProductDTO().getImage());
            tvProductTitle.setText(String.valueOf(cartEntity.getProductDTO().getTitle()));
            tvProductPriceBeforeDiscount.setText(new DecimalFormat("0.00").format(cartEntity.getProductDTO().getPrice() * 1.2 * (double) cartEntity.getQuantity()));
            tvProductPriceBeforeDiscount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvProductPrice.setText(String.valueOf(cartEntity.getProductDTO().getPrice() * cartEntity.getQuantity()));
            tvCartItemCount.setText(String.valueOf(cartEntity.getQuantity()));
            tvRemoveFromCart.setOnClickListener(v -> cartItemViewCallbacks.onRemoveCartItemButtonClick(cartEntity));


            btnIncrease.setOnClickListener(v -> {
                cartEntity.setChangeCountProgressBarIsVisible(true);   // dar fragment migim false va progress bar ra invisible mikonim.
                pbChangeCount.setVisibility(View.VISIBLE);
                tvCartItemCount.setVisibility(View.INVISIBLE);
                cartItemViewCallbacks.onIncreaseCartItemButtonClick(cartEntity);
            });

            btnDecrease.setOnClickListener(v -> {
                if (cartEntity.getQuantity() > 1) {
                    cartEntity.setChangeCountProgressBarIsVisible(true);   // dar fragment migim false va progress bar ra invisible mikonim.
                    pbChangeCount.setVisibility(View.VISIBLE);
                    tvCartItemCount.setVisibility(View.INVISIBLE);
                    cartItemViewCallbacks.onDecreaseCartItemButtonClick(cartEntity);
                } else
                    cartItemViewCallbacks.onRemoveCartItemButtonClick(cartEntity);
            });

            sdvProductImage.setOnClickListener(v -> cartItemViewCallbacks.onImageCartItemButtonClick(cartEntity));

        }
    }
}
