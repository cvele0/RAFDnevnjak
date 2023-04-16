package rs.raf.rafdnevnjak.models;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {
    private String username;
    private String password;
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public boolean isValidPassword() {
        // Check length
        if (password.length() < 5) return false;
        // Check for uppercase letter
        if (!password.matches(".*[A-Z].*")) return false;
        // Check for digit
        if (!password.matches(".*\\d.*")) return false;
        // Check for invalid characters
        return !password.matches(".*[(~#^|$%&*!)].*");
        // If all checks pass, return true
    }

    public boolean isValidEmail() {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
