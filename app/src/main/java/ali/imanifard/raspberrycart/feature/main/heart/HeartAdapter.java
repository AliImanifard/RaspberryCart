package ali.imanifard.raspberrycart.feature.main.heart;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.services.image_loading.ImageLoadingService;

public class HeartAdapter extends RecyclerView.Adapter<HeartAdapter.ViewHolder> {


    private final ImageLoadingService imageLoadingService;
    private List<ProductDTO> productList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    private WishlistProductEventListener wishlistProductEventListener;
    private ItemTouchHelper.SimpleCallback callback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView,
                                      @NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull RecyclerView.ViewHolder target) {
                    // Not needed for swipe-to-delete functionality
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    //Remove swiped item from list and notify the RecyclerView
                    int position = viewHolder.getAdapterPosition();
                    wishlistProductEventListener.onSwipeToDelete(productList.get(position));
                    productList.remove(position);
                    //notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            };

    public HeartAdapter(ImageLoadingService imageLoadingService, WishlistProductEventListener wishlistProductEventListener) {
        this.imageLoadingService = imageLoadingService;
        this.wishlistProductEventListener = wishlistProductEventListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindProduct(productList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setWishlistProducts(List<ProductDTO> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        // Attach swipe-to-delete functionality to the cardView or any other item's root view
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(getRecyclerView());
    }

    public interface WishlistProductEventListener {
        void onProductClick(ProductDTO product);

        void onSwipeToDelete(ProductDTO productDTO);

        void onAddToCartBtn(ProductDTO productDTO);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView ivWishlistProduct;
        TextView tvWishlistProductTitle;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivWishlistProduct = itemView.findViewById(R.id.iv_wishlist_product);
            tvWishlistProductTitle = itemView.findViewById(R.id.tv_wishlist_product_title);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);

            itemView.setOnTouchListener((v, event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    itemTouchHelper.startSwipe(this);
                    v.performClick();
                }
                return false;
            });

        }

        public void bindProduct(ProductDTO product, int position) {
            imageLoadingService.load(ivWishlistProduct, product.getImage());

            tvWishlistProductTitle.setText(product.getTitle());
            btnAddToCart.setOnClickListener(v -> {
                if (wishlistProductEventListener != null) {
                    wishlistProductEventListener.onAddToCartBtn(product);
                }
            });

            ivWishlistProduct.setOnClickListener(v -> wishlistProductEventListener.onProductClick(product));

        }
    }
}
