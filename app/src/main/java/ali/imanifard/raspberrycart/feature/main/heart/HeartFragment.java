package ali.imanifard.raspberrycart.feature.main.heart;

import static org.koin.java.KoinJavaComponent.inject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
import ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys;
import ali.imanifard.raspberrycart.common.RaspberryCartFragment;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.databinding.FragmentHeartBinding;
import ali.imanifard.raspberrycart.feature.main.MainActivity;
import ali.imanifard.raspberrycart.feature.main.product_detail_screen.ProductDetailActivity;
import ali.imanifard.raspberrycart.services.image_loading.ImageLoadingService;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Lazy;

public class HeartFragment extends RaspberryCartFragment implements HeartAdapter.WishlistProductEventListener {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected MainActivity mainActivity;
    protected Context context;
    private FragmentHeartBinding binding;
    private Lazy<HeartViewModel> viewModelLazy;
    private HeartAdapter heartAdapter;
    private Lazy<ImageLoadingService> imageLoadingServiceLazy;

    public HeartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModelLazy = inject(HeartViewModel.class);
        imageLoadingServiceLazy = inject(ImageLoadingService.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHeartBinding.inflate(inflater, container, false);
        heartAdapter = new HeartAdapter(imageLoadingServiceLazy.getValue(), this);
        heartAdapter.setRecyclerView(binding.rcWishlist);

        binding.rcWishlist.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false));
        binding.rcWishlist.setHasFixedSize(true);
        binding.rcWishlist.addItemDecoration(new DividerItemDecoration(mainActivity, DividerItemDecoration.HORIZONTAL));
        binding.rcWishlist.setAdapter(heartAdapter);

        viewModelLazy.getValue().productsLiveData.observe(getViewLifecycleOwner(), productDTOList -> {

            if (!productDTOList.isEmpty()) {
                heartAdapter.setWishlistProducts(productDTOList);

            } else {
                // empty state
                showEmptyState(R.layout.view_cart_empty_state);
                TextView tvEmptyStateMessage = container.findViewById(R.id.tv_empty_state_message);
                tvEmptyStateMessage.setText(getResources().getString(R.string.no_products_have_been_added_to_the_wishlist_yet));
                Button btnEmptyStateCta = container.findViewById(R.id.btn_empty_state_cta);
                btnEmptyStateCta.setVisibility(View.GONE);

            }

        });


        binding.btnInfo.setOnClickListener(v -> showSnackbar(getResources().getString(R.string.swipe_to_remove_the_item_from_favorites)));


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
        intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, product);
        startActivity(intent);
    }

    @Override
    public void onSwipeToDelete(ProductDTO productDTO) {
        viewModelLazy.getValue().removeFromWishlist(productDTO);
    }

    @Override
    public void onAddToCartBtn(ProductDTO productDTO) {
        viewModelLazy.getValue().onAddToCartBtn(productDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        Toast.makeText(mainActivity, getResources().getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}