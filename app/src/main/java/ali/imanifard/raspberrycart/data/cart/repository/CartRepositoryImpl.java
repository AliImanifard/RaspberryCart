package ali.imanifard.raspberrycart.data.cart.repository;

import java.util.List;

import ali.imanifard.raspberrycart.data.cart.repository.source.CartLocalDataSource;
import ali.imanifard.raspberrycart.data.cart.repository.source.CartRemoteDataSource;
import ali.imanifard.raspberrycart.data.db.CartEntity;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class CartRepositoryImpl implements CartRepository {

    private final CartRemoteDataSource remoteDataSouce;
    private final CartLocalDataSource localDataSource;

    public CartRepositoryImpl(CartRemoteDataSource remoteDataSouce,
                              CartLocalDataSource localDataSource) {
        this.remoteDataSouce = remoteDataSouce;
        this.localDataSource = localDataSource;
    }

    @Override
    public Completable addToCart(CartEntity cartEntity) {
        //return remoteDataSouce.addToCart(productId);
        return localDataSource.addToCart(cartEntity);
    }

    @Override
    public Single<List<CartEntity>> getCart() {
        return localDataSource.getCart();
    }

    @Override
    public Completable remove(int productId) {
        return localDataSource.removeCart(productId);
    }

    @Override
    public Completable updateCart(CartEntity cartEntity) {
        return localDataSource.updateCart(cartEntity);
    }

    @Override
    public Single<Integer> getCartItemCount() {
        return localDataSource.getCartItemCount();
    }

    @Override
    public Single<Integer> getSingleCartItemCount(int cartId) {
        return localDataSource.getSingleCartItemCount(cartId);
    }
}
