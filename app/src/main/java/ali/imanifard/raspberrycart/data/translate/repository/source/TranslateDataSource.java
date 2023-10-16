package ali.imanifard.raspberrycart.data.translate.repository.source;

import ali.imanifard.raspberrycart.data.translate.TranslateResponse;
import io.reactivex.rxjava3.core.Single;

public interface TranslateDataSource {

    Single<TranslateResponse> getTranslatedQuery(String query);

}
