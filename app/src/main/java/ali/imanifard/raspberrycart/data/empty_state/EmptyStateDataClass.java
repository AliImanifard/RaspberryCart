package ali.imanifard.raspberrycart.data.empty_state;

import androidx.annotation.StringRes;

public class EmptyStateDataClass {
    private boolean mustShow;
    @StringRes
    private int messageStringResourceId = 0;
    private boolean mustShowCallToActionButton = false;


    public EmptyStateDataClass(boolean mustShow) {
        this.mustShow = mustShow;
    }

    public EmptyStateDataClass(boolean mustShow, int messageStringResourceId,
                               boolean mustShowCallToActionButton) {
        this.mustShow = mustShow;
        this.messageStringResourceId = messageStringResourceId;
        this.mustShowCallToActionButton = mustShowCallToActionButton;
    }

    public boolean isMustShow() {
        return mustShow;
    }

    public void setMustShow(boolean mustShow) {
        this.mustShow = mustShow;
    }

    public int getMessageStringResourceId() {
        return messageStringResourceId;
    }

    public void setMessageStringResourceId(int messageStringResourceId) {
        this.messageStringResourceId = messageStringResourceId;
    }

    public boolean isMustShowCallToActionButton() {
        return mustShowCallToActionButton;
    }

    public void setMustShowCallToActionButton(boolean mustShowCallToActionButton) {
        this.mustShowCallToActionButton = mustShowCallToActionButton;
    }
}
