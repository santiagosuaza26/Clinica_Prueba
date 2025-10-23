package app.clinic.shared.infrastructure.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas del utilitario JWT")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "testSecretKeyForJWTTokenGenerationAndValidationPurposesOnlyThisIsLongEnoughFor256Bits";
    private final long testExpiration = 3600000; // 1 hora

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(testSecret, testExpiration);
    }

    @Test
    @DisplayName("Debe generar token válido correctamente")
    void shouldGenerateValidToken() {
        // Given
        String username = "testuser";
        String role = "MEDICO";

        // When
        String token = jwtUtil.generateToken(username, role);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains(".")); // JWT debe tener formato con puntos
    }

    @Test
    @DisplayName("Debe extraer username del token correctamente")
    void shouldExtractUsernameFromToken() {
        // Given
        String username = "testuser";
        String role = "MEDICO";
        String token = jwtUtil.generateToken(username, role);

        // When
        String extractedUsername = jwtUtil.getUsernameFromToken(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    @DisplayName("Debe extraer role del token correctamente")
    void shouldExtractRoleFromToken() {
        // Given
        String username = "testuser";
        String role = "MEDICO";
        String token = jwtUtil.generateToken(username, role);

        // When
        String extractedRole = jwtUtil.getRoleFromToken(token);

        // Then
        assertEquals(role, extractedRole);
    }

    @Test
    @DisplayName("Debe validar token válido correctamente")
    void shouldValidateValidToken() {
        // Given
        String username = "testuser";
        String role = "MEDICO";
        String token = jwtUtil.generateToken(username, role);

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Debe rechazar token inválido")
    void shouldRejectInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debe rechazar token con firma inválida")
    void shouldRejectTokenWithInvalidSignature() {
        // Given - Token generado con diferente secret
        JwtUtil otherJwtUtil = new JwtUtil("differentSecretKeyForJWTTokenGenerationAndValidationPurposesOnly", testExpiration);
        String tokenWithDifferentSecret = otherJwtUtil.generateToken("testuser", "MEDICO");

        // When
        boolean isValid = jwtUtil.validateToken(tokenWithDifferentSecret);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debe rechazar token malformado")
    void shouldRejectMalformedToken() {
        // Given
        String malformedToken = "not.a.valid.jwt";

        // When
        boolean isValid = jwtUtil.validateToken(malformedToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debe generar tokens diferentes para usuarios diferentes")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        // Given
        String username1 = "user1";
        String username2 = "user2";
        String role = "MEDICO";

        // When
        String token1 = jwtUtil.generateToken(username1, role);
        String token2 = jwtUtil.generateToken(username2, role);

        // Then
        assertNotEquals(token1, token2);
        assertEquals(username1, jwtUtil.getUsernameFromToken(token1));
        assertEquals(username2, jwtUtil.getUsernameFromToken(token2));
    }

    @Test
    @DisplayName("Debe generar tokens diferentes para roles diferentes")
    void shouldGenerateDifferentTokensForDifferentRoles() {
        // Given
        String username = "testuser";
        String role1 = "MEDICO";
        String role2 = "ADMINISTRATIVO";

        // When
        String token1 = jwtUtil.generateToken(username, role1);
        String token2 = jwtUtil.generateToken(username, role2);

        // Then
        assertNotEquals(token1, token2);
        assertEquals(role1, jwtUtil.getRoleFromToken(token1));
        assertEquals(role2, jwtUtil.getRoleFromToken(token2));
    }

    @Test
    @DisplayName("Debe manejar token nulo")
    void shouldHandleNullToken() {
        // When
        boolean isValid = jwtUtil.validateToken(null);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debe manejar token vacío")
    void shouldHandleEmptyToken() {
        // When
        boolean isValid = jwtUtil.validateToken("");

        // Then
        assertFalse(isValid);
    }
}