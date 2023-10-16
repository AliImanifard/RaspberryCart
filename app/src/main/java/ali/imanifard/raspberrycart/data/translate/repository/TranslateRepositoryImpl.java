package ali.imanifard.raspberrycart.data.translate.repository;

import ali.imanifard.raspberrycart.data.translate.TranslateResponse;
import ali.imanifard.raspberrycart.data.translate.repository.source.TranslateDataSource;
import ali.imanifard.raspberrycart.data.translate.repository.source.TranslateRemoteDataSource;
import io.reactivex.rxjava3.core.Single;

public class TranslateRepositoryImpl implements TranslateRepository {

    private final TranslateRemoteDataSource remoteDataSource;

    public TranslateRepositoryImpl(TranslateRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Single<TranslateResponse> getTranslatedQuery(String query) {
        return remoteDataSource.getTranslatedQuery(query);
    }
}
