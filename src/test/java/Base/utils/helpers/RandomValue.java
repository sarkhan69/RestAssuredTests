package Base.utils.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;


public class RandomValue {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateRandomString(int length) {
        Random random = new SecureRandom();
        if (length <= 0) {
            throw new IllegalArgumentException("String length must be a positive integer");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    public static BigDecimal generateRandomBigDecimal() {
        return new BigDecimal(Math.random() * 1000000).setScale(2, RoundingMode.DOWN);
    }

    public static BigDecimal getBDFromFloat(Float source) {
        return new BigDecimal(source).setScale(2, RoundingMode.DOWN);
    }

    public static int generateRandomInt(int count) {
        return new Random().nextInt(count);
    }

    public static boolean getAnyBoolean() {
        return new Random().nextBoolean();
    }
}
