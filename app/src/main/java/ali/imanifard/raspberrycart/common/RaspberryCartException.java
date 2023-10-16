package ali.imanifard.raspberrycart.common;

import androidx.annotation.StringRes;

public class RaspberryCartException extends Throwable{

    private final TYPE type;

    // userFriendlyMessage -> (payam Karbar pasand)
    // For example, a '401 error' may be received that the user does not understand,
    // so we have to give the solution to the user.
    private final int userFriendlyMessage;
    private final String serverMessage;

    public enum TYPE {
        SIMPLE,
        DIALOG,
        AUTH
    }

    public RaspberryCartException(TYPE type, @StringRes int userFriendlyMessage, String serverMessage) {
        this.type = type;
        this.userFriendlyMessage = userFriendlyMessage;
        this.serverMessage = serverMessage;
    }

    public TYPE getType() {
        return type;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public int getUserFriendlyMessage() {
        return userFriendlyMessage;
    }
}
