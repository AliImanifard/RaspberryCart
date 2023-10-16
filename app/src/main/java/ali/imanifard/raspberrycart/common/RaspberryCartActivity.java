package ali.imanifard.raspberrycart.common;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ali.imanifard.raspberrycart.feature.main.auth.AuthActivity;

public abstract class RaspberryCartActivity extends AppCompatActivity implements RaspberryCartView {

    @Override
    public CoordinatorLayout getRootView() {
        ViewGroup viewGroup = getWindow().getDecorView().findViewById(android.R.id.content);
        if (!(viewGroup instanceof CoordinatorLayout)) {
            for (View child : viewGroup.getTouchables()) {
                if (child instanceof CoordinatorLayout) {
                    return (CoordinatorLayout) child;
                }
            }
            Log.e("myLog", "getRootView: RootView must be instance of CoordinatorLayout", new IllegalStateException());
            //throw new IllegalStateException("RootView must be instance of CoordinatorLayout");
            return new CoordinatorLayout(getViewContext());
        } else
            return (CoordinatorLayout) viewGroup;
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showError(RaspberryCartException raspberryCartException) {
        if (getViewContext() != null) {
            switch (raspberryCartException.getType()) {
                case SIMPLE -> showSnackbar(
                        (raspberryCartException.getServerMessage()) == null ?
                                getViewContext()
                                        .getString(raspberryCartException.getUserFriendlyMessage()) :
                                raspberryCartException.getServerMessage());

                case AUTH -> {
                    getViewContext().startActivity(new Intent(getViewContext(), AuthActivity.class));
                    Toast.makeText(getViewContext(), raspberryCartException.getServerMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
