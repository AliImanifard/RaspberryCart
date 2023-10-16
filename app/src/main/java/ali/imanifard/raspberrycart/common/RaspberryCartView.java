package ali.imanifard.raspberrycart.common;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.feature.main.auth.AuthActivity;

public interface RaspberryCartView {

    CoordinatorLayout getRootView();

    Context getViewContext();

    default void setLoadingLottieIndicator(boolean mustShow) {
        CoordinatorLayout rootView = getRootView();
        if (rootView != null) {
            Context context = getViewContext();
            if (context != null) {
                View loadingView = rootView.findViewById(R.id.loading_view);
                if (loadingView == null && mustShow) {
                    loadingView = LayoutInflater.from(context)
                            .inflate(R.layout.loading_view, rootView, false);
                    rootView.addView(loadingView);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(mustShow ? View.VISIBLE : View.GONE);
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN) // eventbus
    default void showError(RaspberryCartException raspberryCartException) {
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


    default void showSnackbar(String message) {
        if (getRootView() != null) {
            Snackbar.make(getRootView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    default View showEmptyState(@LayoutRes int layoutResId) {

        if (getRootView() != null) {
            if (getViewContext() != null) {

                View emptyState = getRootView().findViewById(R.id.empty_state_root_view);

                if (emptyState == null) {
                    emptyState = LayoutInflater.from(getViewContext()).inflate(layoutResId, getRootView(), false);
                    getRootView().addView(emptyState);
                }

                emptyState.setVisibility(View.VISIBLE);
                return emptyState;
            }
        }
        return null;
    }

}


