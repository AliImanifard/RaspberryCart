package ali.imanifard.raspberrycart.feature.main.product_detail_screen;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys;
import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.common.RaspberrySingleObserver;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.data.Product.repository.ProductRepository;
import ali.imanifard.raspberrycart.data.cart.repository.CartRepository;
import ali.imanifard.raspberrycart.data.db.CartEntity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductDetailViewModel extends RaspberryCartViewModel {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    public MutableLiveData<ProductDTO> productLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> singleItemCountLiveData = new MutableLiveData<>();

    public ProductDetailViewModel(Bundle bundle, CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        //loadingLottie.setValue(true);

        productLiveData.setValue(bundle.getParcelable(RaspberryCartExtraKeys.EXTRA_KEY_DATA));

    }


    public Completable onAddToCartBtn() {

        cartRepository.getSingleCartItemCount(productLiveData.getValue().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberrySingleObserver<Integer>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        singleItemCountLiveData.postValue(integer);
                    }
                });

        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(productLiveData.getValue().getId());
        cartEntity.setQuantity(1);
        cartEntity.setProductDTO(productLiveData.getValue());
        return cartRepository.addToCart(cartEntity);
        //return null;
    }

    public Completable onFavoriteBtn(ProductDTO productDTO) {
        return productRepository.addToFavorite(productDTO);
    }


}
