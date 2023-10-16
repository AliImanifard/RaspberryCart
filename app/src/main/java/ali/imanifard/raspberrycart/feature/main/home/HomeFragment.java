package ali.imanifard.raspberrycart.feature.main.home;

import static org.koin.core.parameter.ParametersHolderKt.parametersOf;
import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;
import static ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys.VIEW_TYPE_ROUND;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys;
import ali.imanifard.raspberrycart.common.RaspberryCartFragment;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.databinding.FragmentHomeBinding;
import ali.imanifard.raspberrycart.feature.main.MainActivity;
import ali.imanifard.raspberrycart.feature.main.ProductListAdapter;
import ali.imanifard.raspberrycart.feature.main.product_catalog.ProductCatalogActivity;
import ali.imanifard.raspberrycart.feature.main.product_detail_screen.ProductDetailActivity;

public class HomeFragment extends RaspberryCartFragment implements ProductListAdapter.ProductClickEventListener {

    private static final String TAG = "HomeFragment";
    // Activity and context
    protected MainActivity mainActivity;
    protected Context context;
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private ProductListAdapter productListAdapter, productListAdapterForPopularProducts,
            productListAdapterForCheapestProducts, productListAdapterForMostExpensiveProducts;
    private ViewModelStore viewModelStore = new ViewModelStore();


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = get(HomeViewModel.class, named("myHomeViewModel"));

        productListAdapter = get(ProductListAdapter.class, named("myProductListAdapterQualifier"),
                () -> parametersOf(VIEW_TYPE_ROUND));

        productListAdapterForPopularProducts = get(ProductListAdapter.class, named("myProductListAdapterQualifier"),
                () -> parametersOf(VIEW_TYPE_ROUND));

        productListAdapterForCheapestProducts = get(ProductListAdapter.class, named("myProductListAdapterQualifier"),
                () -> parametersOf(VIEW_TYPE_ROUND));

        productListAdapterForMostExpensiveProducts = get(ProductListAdapter.class, named("myProductListAdapterQualifier"),
                () -> parametersOf(VIEW_TYPE_ROUND));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        productListAdapter.setProducts(viewModel.allProductsLiveData.getValue());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvLatestProducts.setHasFixedSize(true);
        binding.rvLatestProducts.setLayoutManager(
                new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false));
        binding.rvLatestProducts.setAdapter(productListAdapter);
        productListAdapter.productClickEventListener = this;

        binding.rvPopularProducts.setHasFixedSize(true);
        binding.rvPopularProducts.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false));
        binding.rvPopularProducts.setAdapter(productListAdapterForPopularProducts);
        productListAdapterForPopularProducts.productClickEventListener = this;

        binding.rvCheapestProducts.setHasFixedSize(true);
        binding.rvCheapestProducts.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false));
        binding.rvCheapestProducts.setAdapter(productListAdapterForCheapestProducts);
        productListAdapterForCheapestProducts.productClickEventListener = this;

        binding.rvMostExpensiveProducts.setHasFixedSize(true);
        binding.rvMostExpensiveProducts.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false));
        binding.rvMostExpensiveProducts.setAdapter(productListAdapterForMostExpensiveProducts);
        productListAdapterForMostExpensiveProducts.productClickEventListener = this;


        viewModel.allProductsLiveData.observe(getViewLifecycleOwner(), productDTOs -> {
            productListAdapter.setProducts(productDTOs);


            List<ProductDTO> popularProductDTOs = new ArrayList<>(productDTOs);
            popularProductDTOs.sort((o1, o2) -> Double.compare(o2.getRating().getRate(), o1.getRating().getRate()));
            productListAdapterForPopularProducts.setProducts(popularProductDTOs);

            List<ProductDTO> cheapestProductDTOs = new ArrayList<>(productDTOs);
            cheapestProductDTOs.sort(Comparator.comparingDouble(ProductDTO::getPrice));
            productListAdapterForCheapestProducts.setProducts(cheapestProductDTOs);

            List<ProductDTO> MostExpensiveProductDTOs = new ArrayList<>(productDTOs);
            MostExpensiveProductDTOs.sort((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice()));
            productListAdapterForMostExpensiveProducts.setProducts(MostExpensiveProductDTOs);
        });

        binding.btnViewAllBestSellingProducts.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, ProductCatalogActivity.class);
            intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, RaspberryCartExtraKeys.SORT_BEST_SELLING);
            startActivity(intent);
        });

        binding.btnViewAllPopularProducts.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, ProductCatalogActivity.class);
            intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, RaspberryCartExtraKeys.SORT_POPULAR);
            startActivity(intent);
        });

        binding.btnViewAllCheapestProducts.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, ProductCatalogActivity.class);
            intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, RaspberryCartExtraKeys.SORT_PRICE_LOW_TO_HIGH);
            startActivity(intent);
        });


        binding.btnViewAllMostExpensiveProducts.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, ProductCatalogActivity.class);
            intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, RaspberryCartExtraKeys.SORT_PRICE_HIGH_TO_LOW);
            startActivity(intent);
        });


        viewModel.loadingLottie.observe(getViewLifecycleOwner(), this::setLoadingLottieIndicator);


        binding.svSearch.setIconifiedByDefault(false);
        binding.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fake Store API does not have such a feature
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity)
            this.mainActivity = (MainActivity) context;
    }

    @Override
    public void onProductClick(ProductDTO product) {
        Intent intent = new Intent(mainActivity, ProductDetailActivity.class);
        // 09/24/2023 put Extras
        intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, product);
        startActivity(intent);
    }

    @Override
    public void onFavoriteBtnClick(ProductDTO product) {
        viewModel.addProductToFavorite(product);
    }

}