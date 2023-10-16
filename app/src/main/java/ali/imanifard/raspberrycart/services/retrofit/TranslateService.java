package ali.imanifard.raspberrycart.services.retrofit;

import ali.imanifard.raspberrycart.data.translate.TranslateResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslateService {

    @GET("translate/")
    Single<TranslateResponse> translateEnToFa(@Query("token") String oneApiToken,
                                              @Query("action") String action,
                                              @Query("lang") String lang,
                                              @Query("q") String query);


    static TranslateService createInstanceForTranslateService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://one-api.ir/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return retrofit.create(TranslateService.class);
    }

}
