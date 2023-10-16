package ali.imanifard.raspberrycart.common;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class RaspberryCartCompletableObserver implements CompletableObserver {
    private final CompositeDisposable compositeDisposable;

    public RaspberryCartCompletableObserver(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        compositeDisposable.add(d);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        EventBus.getDefault().post(RaspberryCartExceptionMapper.map(e));
    }
}
