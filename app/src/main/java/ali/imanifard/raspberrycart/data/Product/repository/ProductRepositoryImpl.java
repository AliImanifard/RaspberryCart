package ali.imanifard.raspberrycart.data.Product.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.data.Product.repository.source.ProductLocalDataSource;
import ali.imanifard.raspberrycart.data.Product.repository.source.ProductRemoteDataSource;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


// Here it is going to decide whether to read locally or remotely
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductRemoteDataSource remoteDataSource;
    private final ProductLocalDataSource localDataSource;

    public ProductRepositoryImpl(ProductRemoteDataSource remoteDataSource,
                                 ProductLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public Single<List<ProductDTO>> getAllProducts() {

        return localDataSource.getFavoriteProducts()
                .flatMap(favoriteProducts -> remoteDataSource.getAllProducts()
                        .doOnSuccess(products -> {
                            List<Integer> favoriteProductIds = new ArrayList<>();
                            for (ProductDTO favoriteProduct : favoriteProducts) {
                                favoriteProductIds.add(favoriteProduct.getId());
                            }
                            for (ProductDTO product : products) {
                                if (favoriteProductIds.contains(product.getId())) {
                                    product.setFavorite(true);
                                }
                            }
                        }));

    }

    @Override
    public Single<List<ProductDTO>> getMostPopularProducts() {

        return remoteDataSource.getAllProducts()
                .flatMap(productDTOList -> {
                    List<Double> rates = new ArrayList<>();
                    for (ProductDTO productDTO : productDTOList) {
                        rates.add(productDTO.getRating().getRate());
                    }
                    rates.sort(Collections.reverseOrder());
                    return Single.just(productDTOList);
                });

    }

    @Override
    public Single<List<ProductDTO>> getFavoriteProducts() {
        return localDataSource.getFavoriteProducts();
    }

    @Override
    public Completable addToFavorite(ProductDTO productDTO) {
        return localDataSource.addToFavorite(productDTO);
    }

    @Override
    public Completable deleteFromFavorites(ProductDTO productDTO) {
        return localDataSource.deleteFromFavorites(productDTO);
    }

    @Override
    public Single<ProductDTO> getSingleProductById(int productId) {
        return remoteDataSource.getSingleProductById(productId);
    }


}
