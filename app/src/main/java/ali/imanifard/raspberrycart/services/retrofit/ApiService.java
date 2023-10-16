package ali.imanifard.raspberrycart.services.retrofit;

import com.google.gson.JsonObject;

import java.util.List;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;
import ali.imanifard.raspberrycart.data.cart.CartResponse;
import ali.imanifard.raspberrycart.data.user.SignUpResponse;
import ali.imanifard.raspberrycart.data.user.TokenResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    static ApiService createApiServiceInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

    @GET("products/")
    Single<List<ProductDTO>> getAllProducts();

    @GET("products/{productId}")
    Single<ProductDTO> getSingleProductById(@Path("productId") int productId);

    @GET("products/category/{jewelery}")
    Single<List<ProductDTO>> getProductsInASpecificCategory(@Path("jewelery") String categoryName);

    @POST("carts/")
    Single<CartResponse> addToCart(@Body JsonObject jsonObject);

    @POST("auth/login/")
    Single<TokenResponse> login(@Body JsonObject jsonObject);

    @POST("users/")
    Single<SignUpResponse> signUp(@Body JsonObject jsonObject);

    @DELETE("carts/{cart_id}")
    Single<CartResponse> removeItemFromCart(@Path("cart_id") int cart_id);

    @GET("carts/{cart_id}")
    Single<CartResponse> getSingleCart(@Path("cart_id") int cart_id);

    @PUT("carts/{cart_id}")
    Single<CartResponse> updateCart(@Path("cart_id") int cart_id, @Body JsonObject jsonObject);


}


