package ali.imanifard.raspberrycart.common;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class RaspberryCartViewModel extends ViewModel {
    public CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<Boolean> loadingLottie = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }


}
