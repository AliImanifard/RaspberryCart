package ali.imanifard.raspberrycart.feature.main.product_catalog;

import static org.koin.core.parameter.ParametersHolderKt.parametersOf;
import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;
import static ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys.VIEW_TYPE_LARGE;
import static ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys.VIEW_TYPE_SMALL;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartActivity;
import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
import ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.databinding.ActivityProductCatalogBinding;
import ali.imanifard.raspberrycart.feature.main.ProductListAdapter;
import ali.imanifard.raspberrycart.feature.main.product_detail_screen.ProductDetailActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ProductCatalogActivity extends RaspberryCartActivity implements ProductListAdapter.ProductClickEventListener {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActivityProductCatalogBinding binding;
    private ProductCatalogViewModel productCatalogViewModel;
    private ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductCatalogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productListAdapter = get(ProductListAdapter.class, named("myProductListAdapterQualifier"), () -> parametersOf(VIEW_TYPE_SMALL));

        productCatalogViewModel = get(ProductCatalogViewModel.class, named("myProductCatalogViewModel"),
                () -> parametersOf(getIntent().getExtras().getInt(RaspberryCartExtraKeys.EXTRA_KEY_DATA)));


        // So that when we enter here, the items will be displayed as a grid (by default).
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rvProductCatalog.setHasFixedSize(false);
        binding.rvProductCatalog.setLayoutManager(gridLayoutManager);
        binding.rvProductCatalog.setAdapter(productListAdapter);
        productListAdapter.productClickEventListener = this;


        binding.btnViewTypeChanger.setOnClickListener(v -> {
            if (productListAdapter.viewType == VIEW_TYPE_SMALL) {
                binding.btnViewTypeChanger.setImageResource(R.drawable.ic_checkbox_deselected);
                productListAdapter.viewType = VIEW_TYPE_LARGE;
                gridLayoutManager.setSpanCount(1);
                productListAdapter.notifyDataSetChanged();
            } else {
                binding.btnViewTypeChanger.setImageResource(R.drawable.ic_categories);
                productListAdapter.viewType = VIEW_TYPE_SMALL;
                gridLayoutManager.setSpanCount(2);
                productListAdapter.notifyDataSetChanged();
            }
        });

        productCatalogViewModel.selectedSortLiveData.observe(this, integer -> {
            binding.tvSortBasedOn.setText(integer);
        });

        productCatalogViewModel.productsLiveData.observe(this, productDTOS -> {
            productListAdapter.setProducts(productDTOS);
        });

        productCatalogViewModel.loadingLottie.observe(this, this::setLoadingLottieIndicator);

        binding.btnSort.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.sort)
                    .setIcon(R.drawable.ic_sort)
                    .setSingleChoiceItems(R.array.sort, productCatalogViewModel.sort,
                            (dialog, which) -> {
                                dialog.dismiss();
                                productCatalogViewModel.onSelectedSortByUser(which);
                            })
                    .show();
        });

        binding.btnBack.setOnClickListener(v -> finish());


    }


    @Override
    public void onProductClick(ProductDTO product) {
        startActivity(new Intent(ProductCatalogActivity.this, ProductDetailActivity.class)
                .putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, product)
        );
    }

    @Override
    public void onFavoriteBtnClick(ProductDTO productDTO) {
        productCatalogViewModel.onFavoriteBtn(productDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        Toast.makeText(ProductCatalogActivity.this, getResources().getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}