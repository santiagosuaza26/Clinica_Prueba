package app.clinic.shared.infrastructure.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Utilidad para manejo de tokens JWT.
 * Proporciona métodos para generar, validar y extraer información de tokens JWT.
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtUtil(@Value("${jwt.secret:mySecretKey}") String secret,
                   @Value("${jwt.expiration:86400000}") long expiration) {
        if (secret.length() < 32) {
            logger.warn("La clave secreta JWT es muy corta. Se recomienda usar al menos 256 bits (32 caracteres)");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expiration;
        logger.info("JwtUtil inicializado con expiración de {} ms", expiration);
    }

    /**
     * Genera un token JWT con el username y rol especificados.
     *
     * @param username El nombre de usuario
     * @param role El rol del usuario
     * @return Token JWT generado
     */
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role.toUpperCase()) // Normalizar rol a mayúsculas
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        logger.debug("Token JWT generado para usuario: {} con rol: {}", username, role);
        return token;
    }

    /**
     * Genera un token de refresh JWT con expiración más larga.
     *
     * @param username El nombre de usuario
     * @param role El rol del usuario
     * @return Token de refresh JWT generado
     */
    public String generateRefreshToken(String username, String role) {
        Date now = new Date();
        // Refresh token dura 7 días
        long refreshExpirationTime = 7 * 24 * 60 * 60 * 1000L; // 7 días en ms
        Date expiryDate = new Date(now.getTime() + refreshExpirationTime);

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .claim("role", role.toUpperCase())
                .claim("type", "refresh") // Marcar como token de refresh
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        logger.debug("Token de refresh JWT generado para usuario: {}", username);
        return refreshToken;
    }

    /**
     * Verifica si un token es de tipo refresh.
     *
     * @param token El token JWT
     * @return true si es un token de refresh
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String type = claims.get("type", String.class);
            return "refresh".equals(type);
        } catch (Exception e) {
            logger.warn("Error verificando tipo de token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrae el username del token JWT.
     *
     * @param token El token JWT
     * @return El username extraído
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Extrae el rol del token JWT.
     *
     * @param token El token JWT
     * @return El rol extraído
     */
    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    /**
     * Valida si un token JWT es válido.
     *
     * @param token El token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            logger.debug("Token es nulo o vacío");
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            logger.debug("Token JWT válido");
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.warn("Token JWT expirado: {}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            logger.warn("Token JWT malformado: {}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.SignatureException e) {
            logger.warn("Firma del token JWT inválida: {}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            logger.warn("Token JWT no soportado: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.warn("Argumento ilegal en token JWT: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error inesperado validando token JWT: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un token está próximo a expirar.
     *
     * @param token El token JWT
     * @param minutesBeforeExpiry Minutos antes de la expiración para considerar como "próximo a expirar"
     * @return true si el token está próximo a expirar
     */
    public boolean isTokenNearExpiry(String token, int minutesBeforeExpiry) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            long diffInMillis = expiration.getTime() - now.getTime();
            long diffInMinutes = diffInMillis / (1000 * 60);
            return diffInMinutes <= minutesBeforeExpiry;
        } catch (Exception e) {
            logger.error("Error verificando expiración del token: {}", e.getMessage());
            return true; // Considerar como expirado si hay error
        }
    }

    /**
     * Extrae los claims de un token JWT.
     *
     * @param token El token JWT
     * @return Los claims del token
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}