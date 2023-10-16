package ali.imanifard.raspberrycart.data.Product.repository.source;

import androidx.room.Dao;

import java.util.List;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.data.db.DbDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public class ProductLocalDataSource implements ProductDataSource {

    private final DbDao dao;

    public ProductLocalDataSource(DbDao dao) {
        this.dao = dao;
    }


    @Override
    public Single<List<ProductDTO>> getAllProducts(){
        return null;
    }

    @Override
    public Single<List<ProductDTO>> getFavoriteProducts(){
        return dao.getFavoriteProducts();
    }

    @Override
    public Completable addToFavorite(ProductDTO productDTO){
        return dao.addToFavorite(productDTO);
    }

    @Override
    public Completable deleteFromFavorites(ProductDTO productDTO){
        return dao.deleteFromFavorites(productDTO);
    }

    @Override
    public Single<ProductDTO> getSingleProductById(int productID) {
        return null;
    }


}
