package ali.imanifard.raspberrycart.data.user;

public final class TokenContainer_Singleton {
    private static TokenContainer_Singleton INSTANCE;
    private String token;

    private TokenContainer_Singleton() {
        // Private constructor to prevent instantiation from outside the class
    }

    public static synchronized TokenContainer_Singleton getInstance(){
        if (INSTANCE == null)
            INSTANCE = new TokenContainer_Singleton();

        return INSTANCE;
    }

    public String getToken(){
        return token;
    }

    public void update(String token){
        this.token = token;
    }

}
