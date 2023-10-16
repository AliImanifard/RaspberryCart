package ali.imanifard.raspberrycart.data.user;

import com.google.gson.annotations.SerializedName;

public class SignUpResponse {

    @SerializedName("password")
    private String password;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("id")
    private int id;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }
}
