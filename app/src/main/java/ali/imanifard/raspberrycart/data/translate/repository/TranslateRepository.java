package ali.imanifard.raspberrycart.data.translate.repository;

import ali.imanifard.raspberrycart.data.translate.TranslateResponse;
import io.reactivex.rxjava3.core.Single;

public interface TranslateRepository {

    Single<TranslateResponse> getTranslatedQuery(String query);
}
