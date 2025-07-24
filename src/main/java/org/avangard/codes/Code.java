package org.avangard.codes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Code {
    private String id;
    private String code;
    private boolean unlimited;
    private int activations;
    private String permission;
    private LocalDate time;
    private long permissionTime;

    public static Code generate(boolean unlimited, int activations, String permission, long permissionTime, int code_time_days) {
        String id = generateRandom(8, "abcdefghijklmnopqrstuvwxyz0123456789");
        String code = generateRandom(15, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*");
        LocalDate time = LocalDate.now().plusDays(code_time_days);
        return new Code(id, code, unlimited, activations, permission, time, permissionTime);
    }

    public static String generateRandom(int length, String chars) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
