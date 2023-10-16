package ali.imanifard.raspberrycart.feature.main;

import static ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys.VIEW_TYPE_LARGE;
import static ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys.VIEW_TYPE_ROUND;
import static ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys.VIEW_TYPE_SMALL;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.services.image_loading.ImageLoadingService;
import ali.imanifard.raspberrycart.services.retrofit.TranslateService;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final ImageLoadingService imageLoadingService;
    public List<ProductDTO> products = new ArrayList<>();
    public ProductClickEventListener productClickEventListener = null;

    public int viewType;


    public ProductListAdapter(TranslateService translateService,
                              ImageLoadingService imageLoadingService,
                              int viewType) {
        this.imageLoadingService = imageLoadingService;
        this.viewType = viewType;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId = switch (viewType) {
            case VIEW_TYPE_ROUND -> R.layout.item_latest_products;
            case VIEW_TYPE_SMALL -> R.layout.item_latest_products_small;
            case VIEW_TYPE_LARGE -> R.layout.item_latest_products_large;
            default -> throw new IllegalStateException("view type is not valid.");
        };

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(layoutResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindProduct(position);
    }

    @Override
    public int getItemCount() {
        if (products != null)
            return products.size();
        else
            return 0;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }


    public interface ProductClickEventListener {
        void onProductClick(ProductDTO product);

        void onFavoriteBtnClick(ProductDTO product);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton productAddToFavorite;
        SimpleDraweeView productImage;
        TextView productTitle, productPrice, productPriceBeforeDiscount,
                productDescription, productRatingRate, productRatingCount, productCategory;
        //TextView productDiscountPercentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productAddToFavorite = itemView.findViewById(R.id.product_add_to_favorite);
            productImage = itemView.findViewById(R.id.sdv_product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            //productDiscountPercentage = itemView.findViewById(R.id.product_discount_percentage);
            productPrice = itemView.findViewById(R.id.product_price);
            productPriceBeforeDiscount = itemView.findViewById(R.id.product_price_before_discount);
            productDescription = itemView.findViewById(R.id.product_description);
            productRatingRate = itemView.findViewById(R.id.product_rating_rate);
            productRatingCount = itemView.findViewById(R.id.product_rating_count);
            productCategory = itemView.findViewById(R.id.product_category);
        }

        public void bindProduct(int position) {

            ProductDTO gottenProduct = products.get(position);


            imageLoadingService.load(productImage, gottenProduct.getImage());


            productTitle.setText(gottenProduct.getTitle());
            productDescription.setText(gottenProduct.getDescription());

            productPrice.setText(String.valueOf(gottenProduct.getPrice())); // sellingPrice -> after discount
            productPriceBeforeDiscount.setText(String.valueOf(new DecimalFormat("0.00").format(gottenProduct.getPrice() * 1.2)));
            productPriceBeforeDiscount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //productDiscountPercentage.setText(gottenProduct.getDefaultVariant().getPrice().getDiscountPercent()+"% OFF");
            productRatingRate.setText(String.valueOf(gottenProduct.getRating().getRate()));
            productRatingCount.setText("(" + gottenProduct.getRating().getCount() + ")");
            productCategory.setText(gottenProduct.getCategory());


            itemView.setOnClickListener(v -> {
                if (productClickEventListener != null)
                    productClickEventListener.onProductClick(gottenProduct);
            });

            if (gottenProduct.isFavorite())
                productAddToFavorite.setColorFilter(ContextCompat
                                .getColor(productAddToFavorite.getContext(), R.color.raspberry),
                        android.graphics.PorterDuff.Mode.SRC_IN);
            else
                productAddToFavorite.setColorFilter(ContextCompat
                                .getColor(productAddToFavorite.getContext(), R.color.white_secondary),
                        android.graphics.PorterDuff.Mode.SRC_IN);


            productAddToFavorite.setOnClickListener(v -> {
                productClickEventListener.onFavoriteBtnClick(gottenProduct);
                gottenProduct.setFavorite(!gottenProduct.isFavorite());
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
