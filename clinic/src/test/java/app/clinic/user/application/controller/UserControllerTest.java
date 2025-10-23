package app.clinic.user.application.controller;

import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.user.application.dto.*;
import app.clinic.user.application.usecase.*;
import app.clinic.user.domain.model.Role;
import app.clinic.user.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import app.clinic.shared.infrastructure.config.JwtUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Pruebas del controlador de usuarios")
@WebMvcTest(UserController.class)
@TestPropertySource(properties = {
    "spring.security.enabled=false"
})
class UserControllerTest {

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private GetAllUsersUseCase getAllUsersUseCase;

    @MockBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @MockBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockBean
    private AuthenticateUserUseCase authenticateUserUseCase;

    @MockBean
    private ChangePasswordUseCase changePasswordUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private LoginRequestDto loginRequestDto;
    private LoginResponseDto loginResponseDto;

    @BeforeEach
    void setUp() {
        // Mock JWT utility
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mock-jwt-token");
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("testuser");
        when(jwtUtil.getRoleFromToken(anyString())).thenReturn("MEDICO");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setFullName("Test User");
        testUser.setCedula("123456789");
        testUser.setEmail("test@example.com");
        testUser.setPhone("555-1234");
        testUser.setBirthDate(LocalDate.of(1990, 1, 1));
        testUser.setAddress("Test Address");
        testUser.setRole(Role.MEDICO);

        userRequestDto = new UserRequestDto(
                "testuser", "password123", "Test User",
                "123456789", "test@example.com", "555-1234",
                "1990-01-01", "Test Address", "MEDICO"
        );

        userResponseDto = new UserResponseDto(
                1L, "testuser", "Test User", "123456789",
                "test@example.com", "555-1234", LocalDate.of(1990, 1, 1),
                "Test Address", "MEDICO"
        );

        loginRequestDto = new LoginRequestDto("testuser", "password123");
        loginResponseDto = new LoginResponseDto("mock-jwt-token", "testuser", "MEDICO");
    }

    @Test
    @DisplayName("Debe crear usuario exitosamente")
    @WithMockUser(roles = "ADMINISTRATIVO")
    void shouldCreateUserSuccessfully() throws Exception {
        // Given
        when(createUserUseCase.execute(any(User.class), eq(Role.ADMINISTRATIVO))).thenReturn(testUser);

        // When & Then
        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.fullName").value("Test User"));
    }

    @Test
    @DisplayName("Debe retornar lista de usuarios")
    @WithMockUser(roles = "ADMINISTRATIVO")
    void shouldReturnUserList() throws Exception {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(getAllUsersUseCase.execute(Role.ADMINISTRATIVO)).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    @DisplayName("Debe retornar usuario por ID")
    @WithMockUser(roles = "ADMINISTRATIVO")
    void shouldReturnUserById() throws Exception {
        // Given
        when(getUserByIdUseCase.execute(1L, Role.ADMINISTRATIVO)).thenReturn(testUser);

        // When & Then
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("Debe actualizar usuario exitosamente")
    @WithMockUser(roles = "ADMINISTRATIVO")
    void shouldUpdateUserSuccessfully() throws Exception {
        // Given
        UserUpdateDto updateDto = new UserUpdateDto(
                "Updated Name", "updated@example.com",
                "987654321", LocalDate.of(1990, 1, 1), "Updated Address"
        );
        when(updateUserUseCase.execute(eq(1L), any(User.class), eq(Role.ADMINISTRATIVO), eq("admin"))).thenReturn(testUser);

        // When & Then
        mockMvc.perform(put("/users/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Debe eliminar usuario exitosamente")
    @WithMockUser(roles = "ADMINISTRATIVO")
    void shouldDeleteUserSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(delete("/users/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe autenticar usuario exitosamente")
    void shouldAuthenticateUserSuccessfully() throws Exception {
        // Given
        when(authenticateUserUseCase.execute("testuser", "password123")).thenReturn(loginResponseDto);

        // When & Then
        mockMvc.perform(post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.role").value("MEDICO"));
    }

    @Test
    @DisplayName("Debe cambiar contraseña exitosamente")
    @WithMockUser(roles = "ADMINISTRATIVO")
    void shouldChangePasswordSuccessfully() throws Exception {
        // Given
        ChangePasswordDto changePasswordDto = new ChangePasswordDto(
                "testuser", "oldpass", "newpass"
        );

        // When & Then
        mockMvc.perform(post("/users/1/change-password")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 401 cuando no hay token de autenticación")
    void shouldReturn401WhenNoAuthToken() throws Exception {
        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debe retornar 403 cuando rol no tiene permisos")
    @WithMockUser(roles = "ENFERMERA")
    void shouldReturn403WhenRoleNotAuthorized() throws Exception {
        // Given
        when(getAllUsersUseCase.execute(Role.ENFERMERA)).thenThrow(new ValidationException("Acceso denegado"));

        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debe manejar errores de validación")
    @WithMockUser(roles = "ADMINISTRATIVO")
    void shouldHandleValidationErrors() throws Exception {
        // Given - DTO inválido (username vacío)
        UserRequestDto invalidDto = new UserRequestDto(
                "", "password123", "Test User",
                "123456789", "test@example.com", "555-1234",
                "1990-01-01", "Test Address", "MEDICO"
        );

        // When & Then
        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}