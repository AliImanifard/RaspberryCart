package ali.imanifard.raspberrycart.feature.main.cart;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PurchaseDetailTotalPrice implements Parcelable {
    public static final Creator<PurchaseDetailTotalPrice> CREATOR = new Creator<PurchaseDetailTotalPrice>() {
        @Override
        public PurchaseDetailTotalPrice createFromParcel(Parcel in) {
            return new PurchaseDetailTotalPrice(in);
        }

        @Override
        public PurchaseDetailTotalPrice[] newArray(int size) {
            return new PurchaseDetailTotalPrice[size];
        }
    };
    private double totalPrice;

    protected PurchaseDetailTotalPrice(Parcel in) {
        totalPrice = in.readDouble();
    }

    public PurchaseDetailTotalPrice() {
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(totalPrice);
    }


}
