package ali.imanifard.raspberrycart.data.cart.repository.source;

import java.util.List;

import ali.imanifard.raspberrycart.data.db.CartEntity;
import ali.imanifard.raspberrycart.data.db.DbDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class CartLocalDataSource implements CartDataSource {

    private final DbDao dao;

    public CartLocalDataSource(DbDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable addToCart(CartEntity cartEntity) {
        return dao.addToCart(cartEntity);
    }

    @Override
    public Single<List<CartEntity>> getCart() {
        return dao.getCart();
    }

    @Override
    public Completable removeCart(int productId) {
        return dao.remove(productId);
    }

    @Override
    public Completable updateCart(CartEntity cartEntity) {
        return dao.updateCart(cartEntity);
    }

    @Override
    public Single<Integer> getCartItemCount() {
        return dao.getItemCartCount();
    }

    @Override
    public Single<Integer> getSingleCartItemCount(int cartId) {
        return dao.getSingleItemCartCount(cartId);
    }
}
