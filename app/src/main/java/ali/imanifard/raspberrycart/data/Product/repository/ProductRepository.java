package ali.imanifard.raspberrycart.data.Product.repository;

import java.util.List;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface ProductRepository {

    Single<List<ProductDTO>> getAllProducts();

    Single<List<ProductDTO>> getMostPopularProducts();

    Single<List<ProductDTO>> getFavoriteProducts(); // from database

    Completable addToFavorite(ProductDTO productDTO);

    Completable deleteFromFavorites(ProductDTO productDTO);

    Single<ProductDTO> getSingleProductById(int productId);


}
