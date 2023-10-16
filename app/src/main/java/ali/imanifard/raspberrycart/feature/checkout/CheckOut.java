package ali.imanifard.raspberrycart.feature.checkout;

public class CheckOut {
    private int payablePrice;
    private String paymentStatus;
    private boolean purchaseSuccess;

    public int getPayablePrice() {
        return payablePrice;
    }

    public void setPayablePrice(int payablePrice) {
        this.payablePrice = payablePrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isPurchaseSuccess() {
        return purchaseSuccess;
    }

    public void setPurchaseSuccess(boolean purchaseSuccess) {
        this.purchaseSuccess = purchaseSuccess;
    }
}
