package ali.imanifard.raspberrycart.common;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class RaspberrySingleObserver<T> implements SingleObserver<T> {

    private final CompositeDisposable compositeDisposable;

    public RaspberrySingleObserver(CompositeDisposable disposable) {
        this.compositeDisposable = disposable;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        compositeDisposable.add(d);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        Log.e("RaspberrySingleObserver",
                "RaspberrySingleObserver onError: " + e.getMessage());

        EventBus.getDefault().post(RaspberryCartExceptionMapper.map(e));
    }
}
