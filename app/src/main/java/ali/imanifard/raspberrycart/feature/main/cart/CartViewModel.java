package ali.imanifard.raspberrycart.feature.main.cart;

import androidx.lifecycle.MutableLiveData;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.common.RaspberrySingleObserver;
import ali.imanifard.raspberrycart.data.cart.repository.CartRepository;
import ali.imanifard.raspberrycart.data.db.CartEntity;
import ali.imanifard.raspberrycart.data.empty_state.EmptyStateDataClass;
import ali.imanifard.raspberrycart.data.user.TokenContainer_Singleton;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartViewModel extends RaspberryCartViewModel {

    private final CartRepository cartRepository;

    public MutableLiveData<List<CartEntity>> cartItemsLiveData = new MutableLiveData<>();
    public MutableLiveData<PurchaseDetailTotalPrice> purchaseDetailTotalPriceLiveData = new MutableLiveData<>();

    public MutableLiveData<EmptyStateDataClass> emptyStateLiveData = new MutableLiveData<>();

    public CartViewModel(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    private void getCartItems() {
        if (TokenContainer_Singleton.getInstance().getToken() != null && !TokenContainer_Singleton.getInstance().getToken().isEmpty()) {
            loadingLottie.setValue(true);

            emptyStateLiveData.setValue(new EmptyStateDataClass(false));

            cartRepository.getCart()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> loadingLottie.setValue(false))
                    .subscribe(new RaspberrySingleObserver<List<CartEntity>>(compositeDisposable) {
                        @Override
                        public void onSuccess(@NonNull List<CartEntity> cartEntities) {
                            if (!cartEntities.isEmpty()) {
                                cartItemsLiveData.postValue(cartEntities);
                                PurchaseDetailTotalPrice purchaseDetailTotalPrice = new PurchaseDetailTotalPrice();
                                double totalPrice = 0.0;
                                for (int i = 0; i < cartEntities.size(); i++) {
                                    totalPrice = cartEntities.get(i).getProductDTO().getPrice() * cartEntities.get(i).getQuantity();
                                }
                                purchaseDetailTotalPrice.setTotalPrice(totalPrice);
                                purchaseDetailTotalPriceLiveData.postValue(purchaseDetailTotalPrice);
                            } else {
                                // pasokh khalieh
                                emptyStateLiveData.postValue(new EmptyStateDataClass(true, R.string.cart_empty_state, false));
                            }
                        }
                    });
        } else {
            // login nakardeh
            emptyStateLiveData.postValue(new EmptyStateDataClass(true, R.string.cart_empty_state_login_required, true));
        }
    }

    public Completable removeItemFromCart(CartEntity cartEntity) {
        return cartRepository.remove(cartEntity.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    calculateAndPublishPurchaseDetail();

                    int cartItemCount = EventBus.getDefault().getStickyEvent(Integer.class);
                    cartItemCount += 1;
                    EventBus.getDefault().postSticky(cartItemCount);

                    if (cartItemsLiveData.getValue().isEmpty())
                        emptyStateLiveData.setValue(new EmptyStateDataClass(true, R.string.cart_empty_state, false));
                });
    }


    public Completable increaseCartItemCount(CartEntity cartEntity) {

        return cartRepository.updateCart(cartEntity)
                .doOnComplete(() -> {
                    calculateAndPublishPurchaseDetail();
                    int cartItemCount = EventBus.getDefault().getStickyEvent(Integer.class);
                    cartItemCount += 1;
                    EventBus.getDefault().postSticky(cartItemCount);
                    cartEntity.setQuantity(cartEntity.getQuantity() + 1);
                });

    }

    public Completable decreaseCartItemCount(CartEntity cartEntity) {
        int j = cartEntity.getQuantity() - 1;
        if (j == 0)
            return cartRepository.remove(cartEntity.getId());
        else if (j > 0)
            return cartRepository.updateCart(cartEntity)
                    .doOnComplete(() -> {
                        calculateAndPublishPurchaseDetail();
                        int cartItemCount = EventBus.getDefault().getStickyEvent(Integer.class);
                        cartItemCount -= 1;
                        EventBus.getDefault().postSticky(cartItemCount);
                        cartEntity.setQuantity(cartEntity.getQuantity() - 1);
                    });
        return null;
    }


    public void refresh() {
        getCartItems();
    }

    private void calculateAndPublishPurchaseDetail() {

        double newTotalPrice = 0.0;

        if (cartItemsLiveData.getValue() != null) {
            if (purchaseDetailTotalPriceLiveData.getValue() != null) {
                for (CartEntity cartEntity : cartItemsLiveData.getValue()) {
                    newTotalPrice += cartEntity.getProductDTO().getPrice() * cartEntity.getQuantity();
                }
            }

            purchaseDetailTotalPriceLiveData.getValue().setTotalPrice(newTotalPrice);
            purchaseDetailTotalPriceLiveData.postValue(purchaseDetailTotalPriceLiveData.getValue());


        }
    }
}


