package ali.imanifard.raspberrycart.data.cart.repository.source;

import java.util.List;

import ali.imanifard.raspberrycart.data.db.CartEntity;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface CartDataSource {

    Completable addToCart(CartEntity cartEntity);

    Single<List<CartEntity>> getCart();

    Completable removeCart(int productId);


    Completable updateCart(CartEntity cartEntity);

    Single<Integer> getCartItemCount();

    Single<Integer> getSingleCartItemCount(int cartId);

}
