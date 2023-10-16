package ali.imanifard.raspberrycart.feature.main.product_detail_screen;

import static org.koin.core.parameter.ParametersHolderKt.parametersOf;
import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;
import static org.koin.java.KoinJavaComponent.inject;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartActivity;
import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
import ali.imanifard.raspberrycart.databinding.ActivityProductDetailBinding;
import ali.imanifard.raspberrycart.feature.scroll.ObservableScrollViewCallbacks;
import ali.imanifard.raspberrycart.feature.scroll.ScrollState;
import ali.imanifard.raspberrycart.services.image_loading.ImageLoadingService;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Lazy;

public class ProductDetailActivity extends RaspberryCartActivity implements ObservableScrollViewCallbacks {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    ActivityProductDetailBinding binding;
    private ProductDetailViewModel productDetailViewModel;
    private Lazy<ImageLoadingService> imageLoadingServiceLazy;
    private CommentRecyclerViewAdapter commentRecyclerViewAdapter = new CommentRecyclerViewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productDetailViewModel = get(ProductDetailViewModel.class,
                named("myProductDetailViewModelQualifier"),
                () -> parametersOf(getIntent().getExtras()));

        imageLoadingServiceLazy = inject(ImageLoadingService.class);

        productDetailViewModel.productLiveData.observe(this, product -> {
            imageLoadingServiceLazy.getValue().load(binding.sdvProductImage, product.getImage());
            binding.tvProductTitle.setText(String.valueOf(product.getTitle()));
            binding.tvProductPriceBeforeDiscount.setText(String.valueOf(product.getPrice() * 1.2));
            binding.tvProductPriceBeforeDiscount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            binding.tvProductPrice.setText(String.valueOf(product.getPrice()));
            binding.tvProductDescription.setText(String.valueOf(product.getDescription()));

            binding.tvToolbarTitle.setText(String.valueOf(product.getTitle()));

            commentRecyclerViewAdapter.setProducts(product);


            //if (product.getLastComments().size() > 3)
            // binding.btnViewAllComments.setVisibility(View.VISIBLE);

            binding.btnAddToFavorite.setOnClickListener(v -> {
                productDetailViewModel.onFavoriteBtn(product)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                            @Override
                            public void onComplete() {
                                Toast.makeText(ProductDetailActivity.this, getResources().getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();
                            }
                        });
            });


        });

        binding.observableScrollView.setScrollViewCallbacks(this);

        initViews();


        binding.btnBack.setOnClickListener(v -> finish());


        binding.btnAddToCart.setOnClickListener(v -> productDetailViewModel
                .onAddToCartBtn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        //showSnackbar(getResources().getString(R.string.added_to_cart));
                        Toast.makeText(ProductDetailActivity.this, getResources().getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
                    }
                }));


    }

    private void initViews() {

        binding.rvLastThreeComments.setHasFixedSize(true);
        binding.rvLastThreeComments.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rvLastThreeComments.setAdapter(commentRecyclerViewAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        SimpleDraweeView sdvProductImageAlternate = binding.sdvProductImage;
        int sdvProductImageHeight = binding.sdvProductImage.getHeight();
        binding.cardViewToolbar.setAlpha((float) scrollY / (float) sdvProductImageHeight);
        sdvProductImageAlternate.setTranslationY((float) scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}