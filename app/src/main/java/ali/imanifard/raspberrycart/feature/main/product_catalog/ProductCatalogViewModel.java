package ali.imanifard.raspberrycart.feature.main.product_catalog;

import androidx.lifecycle.MutableLiveData;

import java.util.Comparator;
import java.util.List;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.common.RaspberrySingleObserver;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.data.Product.repository.ProductRepository;
import ali.imanifard.raspberrycart.data.translate.repository.TranslateRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductCatalogViewModel extends RaspberryCartViewModel {
    public int sort;
    public MutableLiveData<List<ProductDTO>> productsLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> selectedSortLiveData = new MutableLiveData<>();
    private ProductRepository productRepository;
    private TranslateRepository translateRepository;
    private int[] sortTitles = new int[]{R.string.sort_best_selling, R.string.sort_populars,
            R.string.sort_price_low_to_high, R.string.sort_price_high_to_low};

    public ProductCatalogViewModel(ProductRepository productRepository,
                                   TranslateRepository translateRepository,
                                   int sort) {

        this.productRepository = productRepository;
        this.translateRepository = translateRepository;

        this.sort = sort;

        getProducts();

        selectedSortLiveData.setValue(sortTitles[sort]);
    }

    private void getProducts() {

        loadingLottie.setValue(true);


        productRepository.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> loadingLottie.setValue(false))
                .subscribe(new RaspberrySingleObserver<List<ProductDTO>>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull List<ProductDTO> productDTOList) {
                        List<ProductDTO> productDTOS = productDTOList;
                        if (sortTitles[sort] == sortTitles[0]) { //sort_best_selling
                            productsLiveData.setValue(productDTOS);
                        } else if (sortTitles[sort] == sortTitles[1]) {   // sort_populars
                            productDTOS.sort((o1, o2) -> Double.compare(o2.getRating().getRate(), o1.getRating().getRate()));
                            productsLiveData.setValue(productDTOS);
                        } else if (sortTitles[sort] == sortTitles[2]) {   // sort_price_low_to_high
                            productDTOS.sort(Comparator.comparingDouble(ProductDTO::getPrice));
                            productsLiveData.setValue(productDTOS);
                        } else if (sortTitles[sort] == sortTitles[3]) {   // sort_price_high_to_low
                            productDTOS.sort((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice()));
                            productsLiveData.setValue(productDTOS);
                        } else
                            productsLiveData.setValue(productDTOList);
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
                .doFinally(() -> loadingLottie.setValue(false))
                .subscribe(new RaspberrySingleObserver<List<ProductDTO>>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull List<ProductDTO> productDTOList) {
                        // handle sort
                        productsLiveData.setValue(productDTOList);
                    }
                });

         */

    }

    public void onSelectedSortByUser(int sort) {
        this.sort = sort;
        this.selectedSortLiveData.setValue(sortTitles[sort]);
        getProducts();
    }

    public Completable onFavoriteBtn(ProductDTO productDTO) {
        return productRepository.addToFavorite(productDTO);
    }


}
