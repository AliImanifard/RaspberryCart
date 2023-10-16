package ali.imanifard.raspberrycart.data.cart.repository.source;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.data.cart.CartResponse;
import ali.imanifard.raspberrycart.data.db.CartEntity;
import ali.imanifard.raspberrycart.services.retrofit.ApiService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class CartRemoteDataSource implements CartDataSource{

    private final ApiService apiService;

    public CartRemoteDataSource(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Completable addToCart(CartEntity cartEntity) {

        /*
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("userId",1);
        jsonObject.addProperty("date", new Date().toString());

        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        jsonArray.add(gson.toJson());

        jsonObject.add("products",jsonArray);

        return apiService.addToCart(jsonObject);

         */
        return null;
    }

    @Override
    public Single<List<CartEntity>> getCart(/*int cartId*/) {
        //return apiService.getSingleCart(cartId);
        return null;
    }

    @Override
    public Completable removeCart(int cartId) {
        //return apiService.removeItemFromCart(cartId);
        return null;
    }

    @Override
    public Completable updateCart(CartEntity cartEntity) {
        /*
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("userId",userid);
        jsonObject.addProperty("date",date);

        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        jsonArray.add(gson.toJson(ProductsInCart));

        jsonObject.add("products",jsonArray);

        return apiService.updateCart(cartId,jsonObject);

         */
        return null;
    }

    @Override
    public Single<Integer> getCartItemCount() {
        /*
        return apiService.getSingleCart(cartId).map(cartResponse -> {
            int quantities = 0;
            for (int i = 0; i < cartResponse.getProducts().size(); i++) {
                quantities += cartResponse.getProducts().get(i).getQuantity();
            }
            return quantities;
        });

         */
        return null;

    }

    @Override
    public Single<Integer> getSingleCartItemCount(int cartId) {
        return null;
    }


}
