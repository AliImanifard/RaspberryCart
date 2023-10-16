package ali.imanifard.raspberrycart.data.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DbDao {
    @Query("SELECT * FROM tb_product")
    Single<List<ProductDTO>> getFavoriteProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = ProductDTO.class)
    Completable addToFavorite(ProductDTO productDTO);

    @Delete
    Completable deleteFromFavorites(ProductDTO productDTO);

    // tb_cart
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = CartEntity.class)
    Completable addToCart(CartEntity cartEntity);

    @Query("SELECT * FROM tb_cart")
    Single<List<CartEntity>> getCart();

    @Query("DELETE FROM tb_cart WHERE id = :id")
    Completable remove(int id);

    @Query("DELETE FROM tb_cart")
    Completable removeAllCarts();

    @Update(onConflict = OnConflictStrategy.REPLACE, entity = CartEntity.class)
    Completable updateCart(CartEntity cartEntity);

    @Query("SELECT SUM(quantity) FROM tb_cart")
    Single<Integer> getItemCartCount();

    @Query("SELECT SUM(quantity) FROM tb_cart WHERE id = :cartId")
    Single<Integer> getSingleItemCartCount(int cartId);


}
