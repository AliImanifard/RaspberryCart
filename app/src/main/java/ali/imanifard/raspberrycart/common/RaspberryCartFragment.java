package ali.imanifard.raspberrycart.common;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ali.imanifard.raspberrycart.feature.main.auth.AuthActivity;

public abstract class RaspberryCartFragment extends Fragment implements RaspberryCartView {

    @Override
    public CoordinatorLayout getRootView() {
        return (CoordinatorLayout) getView();
    }

    @Override
    public Context getViewContext() {
        return getContext();
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
