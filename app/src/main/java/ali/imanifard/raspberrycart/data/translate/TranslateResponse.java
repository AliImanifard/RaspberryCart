package ali.imanifard.raspberrycart.data.translate;

import com.google.gson.annotations.SerializedName;

public class TranslateResponse {

    @SerializedName("result")
    private String translateResult;
    @SerializedName("status")
    private int status;

    public String getTranslateResult() {
        return translateResult;
    }

    public int getStatus() {
        return status;
    }
}
