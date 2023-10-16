package ali.imanifard.raspberrycart.feature.main.cart;

import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;
import static org.koin.java.KoinJavaComponent.inject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
import ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys;
import ali.imanifard.raspberrycart.common.RaspberryCartFragment;
import ali.imanifard.raspberrycart.data.db.CartEntity;
import ali.imanifard.raspberrycart.databinding.FragmentCartBinding;
import ali.imanifard.raspberrycart.feature.main.MainActivity;
import ali.imanifard.raspberrycart.feature.main.auth.AuthActivity;
import ali.imanifard.raspberrycart.feature.main.product_detail_screen.ProductDetailActivity;
import ali.imanifard.raspberrycart.feature.shipping.ShippingActivity;
import ali.imanifard.raspberrycart.services.image_loading.ImageLoadingService;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Lazy;


public class CartFragment extends RaspberryCartFragment implements CartItemAdapter.CartItemViewCallbacks {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected MainActivity mainActivity;
    protected Context context;
    private FragmentCartBinding binding;
    private CartViewModel cartViewModel;
    private CartItemAdapter cartItemAdapter;
    private Lazy<ImageLoadingService> imageLoadingService;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartViewModel = get(CartViewModel.class, named("myCartViewModel"));
        imageLoadingService = inject(ImageLoadingService.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartViewModel.loadingLottie.observe(getViewLifecycleOwner(), this::setLoadingLottieIndicator);

        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false));
        binding.rvCartItems.setHasFixedSize(true);

        cartViewModel.cartItemsLiveData.observe(getViewLifecycleOwner(), cartItems -> {
            cartItemAdapter = new CartItemAdapter(cartItems, imageLoadingService.getValue(), this);
            binding.rvCartItems.setAdapter(cartItemAdapter);
        });


        cartViewModel.purchaseDetailTotalPriceLiveData.observe(getViewLifecycleOwner(), purchaseDetail -> {
            if (cartItemAdapter != null) {
                cartItemAdapter.purchaseDetailTotalPrice = purchaseDetail;
                cartItemAdapter.setPurchaseDetailTotalPrice(purchaseDetail);
                //cartItemAdapter.notifyItemChanged(cartItemAdapter.getCartEntities().size());
                cartItemAdapter.notifyDataSetChanged();
            }
        });


        cartViewModel.emptyStateLiveData.observe(getViewLifecycleOwner(), emptyStateDataClass -> {
            if (emptyStateDataClass.isMustShow()) {
                View emptyState = showEmptyState(R.layout.view_cart_empty_state);
                if (emptyState != null) {

                    TextView tvEmptyStateMessage = emptyState.findViewById(R.id.tv_empty_state_message);
                    Button btnEmptyStateCta = emptyState.findViewById(R.id.btn_empty_state_cta);

                    tvEmptyStateMessage.setText(getResources().getString(emptyStateDataClass.getMessageStringResourceId()));

                    btnEmptyStateCta.setVisibility((emptyStateDataClass.isMustShowCallToActionButton()) ? View.VISIBLE : View.GONE);
                    btnEmptyStateCta.setOnClickListener(v -> startActivity(new Intent(mainActivity, AuthActivity.class)));

                }
            } else {
                //getRootView().findViewById(R.id.empty_state_root_view).setVisibility(View.GONE);

            }
        });


        binding.btnPay.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, ShippingActivity.class);
            //intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, (Parcelable) cartViewModel.cartItemsLiveData.getValue());
            //intent.putParcelableArrayListExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, new ArrayList<>(cartViewModel.cartItemsLiveData.getValue()));
            intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, cartViewModel.purchaseDetailTotalPriceLiveData.getValue());
            startActivity(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        cartViewModel.refresh();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity)
            this.mainActivity = (MainActivity) context;
    }

    @Override
    public void onRemoveCartItemButtonClick(CartEntity cartEntity) {
        cartViewModel.removeItemFromCart(cartEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        cartItemAdapter.removeCartItem(cartEntity);
                    }
                });
    }

    @Override
    public void onIncreaseCartItemButtonClick(CartEntity cartEntity) {
        cartViewModel.increaseCartItemCount(cartEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        cartItemAdapter.increaseCount(cartEntity);
                    }
                });
    }


    @Override
    public void onDecreaseCartItemButtonClick(CartEntity cartEntity) {
        cartViewModel.decreaseCartItemCount(cartEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        cartItemAdapter.decreaseCount(cartEntity);
                    }
                });
    }


    @Override
    public void onImageCartItemButtonClick(CartEntity cartEntity) {
        Intent intent = new Intent(mainActivity, ProductDetailActivity.class);
        intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, cartEntity.getId());
        startActivity(intent);
    }


}