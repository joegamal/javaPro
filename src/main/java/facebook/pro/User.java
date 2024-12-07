package facebook.pro;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String email;
    public String password;
    public String username;
    public String gender;
    public String birthDate;

    // Constructor
    public User(String email, String password, String username, String gender, String birthDate) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public static ArrayList<String> friends = new ArrayList<String>();
}