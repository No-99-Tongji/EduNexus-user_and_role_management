package com.no99.edunexususer_and_role_management.service;

import com.no99.edunexususer_and_role_management.dto.UserRegistrationRequest;
import com.no99.edunexususer_and_role_management.dto.UserResponse;
import com.no99.edunexususer_and_role_management.entity.User;
import com.no99.edunexususer_and_role_management.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户服务测试类
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setFirstName("Test");
        registrationRequest.setLastName("User");
        registrationRequest.setTimezone("UTC");
    }

    @Test
    void testRegisterUser_Success() {
        // Given
        when(userMapper.existsByUsername("testuser")).thenReturn(false);
        when(userMapper.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        User savedUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .firstName("Test")
                .lastName("User")
                .role(User.UserRole.STUDENT)
                .timezone("UTC")
                .build();

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return null;
        }).when(userMapper).insert(any(User.class));

        // When
        UserResponse result = userService.registerUser(registrationRequest);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals("student", result.getRole());
        assertEquals("学生", result.getRoleDescription());

        verify(userMapper).existsByUsername("testuser");
        verify(userMapper).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        // Given
        when(userMapper.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(registrationRequest);
        });

        verify(userMapper).existsByUsername("testuser");
        verify(userMapper, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Given
        when(userMapper.existsByUsername("testuser")).thenReturn(false);
        when(userMapper.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(registrationRequest);
        });

        verify(userMapper).existsByUsername("testuser");
        verify(userMapper).existsByEmail("test@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testIsUsernameAvailable_Available() {
        // Given
        when(userMapper.existsByUsername("newuser")).thenReturn(false);

        // When
        boolean result = userService.isUsernameAvailable("newuser");

        // Then
        assertTrue(result);
        verify(userMapper).existsByUsername("newuser");
    }

    @Test
    void testIsUsernameAvailable_NotAvailable() {
        // Given
        when(userMapper.existsByUsername("existinguser")).thenReturn(true);

        // When
        boolean result = userService.isUsernameAvailable("existinguser");

        // Then
        assertFalse(result);
        verify(userMapper).existsByUsername("existinguser");
    }

    @Test
    void testIsEmailAvailable_Available() {
        // Given
        when(userMapper.existsByEmail("new@example.com")).thenReturn(false);

        // When
        boolean result = userService.isEmailAvailable("new@example.com");

        // Then
        assertTrue(result);
        verify(userMapper).existsByEmail("new@example.com");
    }

    @Test
    void testIsEmailAvailable_NotAvailable() {
        // Given
        when(userMapper.existsByEmail("existing@example.com")).thenReturn(true);

        // When
        boolean result = userService.isEmailAvailable("existing@example.com");

        // Then
        assertFalse(result);
        verify(userMapper).existsByEmail("existing@example.com");
    }
}
