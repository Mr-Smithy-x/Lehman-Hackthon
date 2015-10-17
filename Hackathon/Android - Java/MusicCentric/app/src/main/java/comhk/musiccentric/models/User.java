package comhk.musiccentric.models;

import java.io.Serializable;

/**
 * Created by charlton on 10/16/15.
 */
public class User implements Serializable {
    private String user, email, password;

    public String getUser() {
        return user;
    }

    public static User Build() {
        return new User();
    }

    public User setUser(String user) {
        this.user = user;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}
