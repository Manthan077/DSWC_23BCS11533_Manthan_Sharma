import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class FortressJwtMintingEngine {

    private static final String SECRET =

            "MyVerySecureSecretKeyForJwtSigning12345678901234567890";

    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes(
                            StandardCharsets.UTF_8));

    public String generateToken(
            String username,
            List<String> roles) {

        Date issuedAt =
                new Date();

        Date expiration =
                new Date(
                        issuedAt.getTime()
                                + 15 * 60 * 1000);

        return Jwts.builder()
                .subject(username)
                .claim(
                        "authorities",
                        roles)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(
            String token) {

        JwtParser parser =
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build();

        Claims claims =
                parser.parseSignedClaims(token)
                        .getPayload();

        return claims.getSubject();
    }

    public static void main(String[] args) {

        FortressJwtMintingEngine provider =
                new FortressJwtMintingEngine();

        String token =
                provider.generateToken(
                        "manthan",
                        List.of(
                                "ROLE_USER",
                                "ROLE_ADMIN"));

        System.out.println(
                "Generated Token:");

        System.out.println(token);

        System.out.println(
                "\nUsername:");

        System.out.println(
                provider.extractUsername(token));
    }
}