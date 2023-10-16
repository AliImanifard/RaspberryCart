package ali.imanifard.raspberrycart.data.translate.repository.source;

import static org.koin.java.KoinJavaComponent.inject;

import androidx.core.content.res.ResourcesCompat;

import ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys;
import ali.imanifard.raspberrycart.data.translate.TranslateResponse;
import ali.imanifard.raspberrycart.services.retrofit.TranslateService;
import io.reactivex.rxjava3.core.Single;
import kotlin.Lazy;

public class TranslateRemoteDataSource implements TranslateDataSource{

    private final Lazy<TranslateService> translateServiceLazy;

    public TranslateRemoteDataSource() {
        translateServiceLazy = inject(TranslateService.class);
    }

    @Override
    public Single<TranslateResponse> getTranslatedQuery(String query) {
        return translateServiceLazy.getValue().translateEnToFa(
                RaspberryCartExtraKeys.apiToken,
                "google",
                "fa",
                query);
    }




}
