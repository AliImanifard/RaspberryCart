package ali.imanifard.raspberrycart.feature.main.home;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.common.RaspberrySingleObserver;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.data.Product.repository.ProductRepository;
import ali.imanifard.raspberrycart.data.translate.repository.TranslateRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends RaspberryCartViewModel {


    private final ProductRepository productRepository;
    private final TranslateRepository translateRepository;

    public MutableLiveData<List<ProductDTO>> allProductsLiveData = new MutableLiveData<>();


    public HomeViewModel(ProductRepository productRepository,
                         TranslateRepository translateRepository) {

        this.productRepository = productRepository;
        this.translateRepository = translateRepository;


        loadingLottie.setValue(true);


        productRepository.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> loadingLottie.setValue(false))
                .subscribe(new RaspberrySingleObserver<List<ProductDTO>>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull List<ProductDTO> productDTOList) {
                        allProductsLiveData.postValue(productDTOList);
                    }
                });


        // this is new implementation of above code
        /*
        productRepository.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapObservable(Observable::fromIterable)
                .flatMapSingle(productDTO -> {
                    Single<String> titleSingle = translateRepository.getTranslatedQuery(productDTO.getTitle())
                            .map(TranslateResponse::getTranslateResult)
                            .onErrorReturnItem(productDTO.getTitle());
                    Single<String> descriptionSingle = translateRepository.getTranslatedQuery(productDTO.getDescription())
                            .map(TranslateResponse::getTranslateResult)
                            .onErrorReturnItem(productDTO.getDescription());
                    return Single.zip(titleSingle,descriptionSingle,(title, description) -> {
                        productDTO.setTitle(title);
                        productDTO.setDescription(description);
                        return productDTO;
                    });
                })
                .toList()
                .doFinally(() -> loadingLottie.postValue(false))
                .subscribe(new RaspberrySingleObserver<List<ProductDTO>>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull List<ProductDTO> productDTOList) {
                        allProductsLiveData.postValue(productDTOList);
                    }
                });

         */


        productRepository.getMostPopularProducts()
                .subscribe(new RaspberrySingleObserver<List<ProductDTO>>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull List<ProductDTO> productDTOList) {

                    }
                });


    }


    public void addProductToFavorite(ProductDTO productDTO) {
        if (productDTO.isFavorite())
            productRepository.deleteFromFavorites(productDTO)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                        @Override
                        public void onComplete() {
                            productDTO.setFavorite(false);
                        }
                    });
        else
            productRepository.addToFavorite(productDTO)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                        @Override
                        public void onComplete() {
                            productDTO.setFavorite(true);
                        }
                    });

    }


}
