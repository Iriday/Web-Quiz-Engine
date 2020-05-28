package engine.user;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserUtils {
    private final static Pattern patternAllowedChars = Pattern.compile("[\\w\\p{Punct} ]+");
    private final static Pattern patternDigit = Pattern.compile(".*\\d.*");

    /**
     * If password is valid return Optional(null) else return Optional(err message).
     */
    public static Optional<String> checkPassword(String password) {
        String errMessage = null;

        if (password.startsWith(" ") || password.endsWith(" ")) {
            errMessage = "Password must not start or end with space(s)";
        } else if (password.length() < 8 || password.length() > 64) {
            errMessage = "Password size must be >=8 and <=64";
        } else if (!patternAllowedChars.matcher(password).matches()) {
            errMessage = "Password contains illegal character(s). Valid characters: alphanumeric, special characters(!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~), and space";
        } else if (password.replaceAll("[^\\p{Alpha}]", "").length() < 3) {
            errMessage = "Password must contain at least 3 letters";
        } else if (!patternDigit.matcher(password).matches()) {
            errMessage = "Password must contain at least 1 digit";
        }
        return Optional.ofNullable(errMessage);
    }
}
