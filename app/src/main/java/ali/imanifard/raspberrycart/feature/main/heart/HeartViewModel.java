package ali.imanifard.raspberrycart.feature.main.heart;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
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

public class HeartViewModel extends RaspberryCartViewModel {

    public MutableLiveData<List<ProductDTO>> productsLiveData = new MutableLiveData<>();
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private MutableLiveData<Integer> singleItemCountLiveData = new MutableLiveData<>();


    public HeartViewModel(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;


        productRepository.getFavoriteProducts()
                .subscribeOn(Schedulers.io())
                .subscribe(new RaspberrySingleObserver<List<ProductDTO>>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull List<ProductDTO> productDTOList) {
                        productsLiveData.postValue(productDTOList);
                    }
                });
    }


    public void removeFromWishlist(ProductDTO productDTO) {
        productRepository.deleteFromFavorites(productDTO)
                .subscribeOn(Schedulers.io())
                .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Completable onAddToCartBtn(ProductDTO productDTO) {

        cartRepository.getSingleCartItemCount(productDTO.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberrySingleObserver<Integer>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        singleItemCountLiveData.postValue(integer);
                    }
                });

        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(productDTO.getId());
        cartEntity.setQuantity(1);
        cartEntity.setProductDTO(productDTO);
        return cartRepository.addToCart(cartEntity);
        //return null;
    }


}
