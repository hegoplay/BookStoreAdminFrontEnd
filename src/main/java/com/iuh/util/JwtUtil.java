package com.iuh.util;

import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

public class JwtUtil {

    private static final String SECRET_KEY = "uGn1btxMAz77FBExhcThICMJpEWG4fwLu6pJiwlObSIubo7ivat3KSD54PRN0467";  // Thay bằng secret key của bạn

    private static NimbusJwtDecoder nimbusJwtDecoder;
    
    public static String getRoleFromToken(String token) {
        try {
            if (Objects.isNull(nimbusJwtDecoder)) {
                // Tạo NimbusJwtDecoder nếu chưa có
                SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");
                nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.HS512)
                        .build();
            }

            // Giải mã token
            Jwt jwt = nimbusJwtDecoder.decode(token);
            // Lấy thông tin claim (ví dụ: 'role') từ token
            String role = jwt.getClaims().get("scope").toString();
            String arr[] = role.split("_");
            return arr[arr.length-1];  // Trả về role của người dùng

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public static boolean isAdmin(String token) {
		return getRoleFromToken(token).equals("ADMIN");
	}
}
