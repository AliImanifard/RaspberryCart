package ali.imanifard.raspberrycart.data.Product.repository.source;

import static org.koin.java.KoinJavaComponent.inject;

import java.util.List;

import ali.imanifard.raspberrycart.services.retrofit.ApiService;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import kotlin.Lazy;

public class ProductRemoteDataSource implements ProductDataSource{

    private final Lazy<ApiService> apiServiceLazy;

    public ProductRemoteDataSource() {
        apiServiceLazy = inject(ApiService.class);
    }

    @Override
    public Single<List<ProductDTO>> getAllProducts() {
        return apiServiceLazy.getValue().getAllProducts();
    }

    @Override
    public Single<List<ProductDTO>> getFavoriteProducts() {
        return null;
    }

    @Override
    public Completable addToFavorite(ProductDTO productDTO) {
        return null;
    }

    @Override
    public Completable deleteFromFavorites(ProductDTO productDTO) {
        return null;
    }

    @Override
    public Single<ProductDTO> getSingleProductById(int productID) {
        return apiServiceLazy.getValue().getSingleProductById(productID);
    }


}
