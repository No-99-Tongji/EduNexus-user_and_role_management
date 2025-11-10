package com.no99.edunexususer_and_role_management.service;

import com.no99.edunexususer_and_role_management.dto.JwtAuthenticationResponse;
import com.no99.edunexususer_and_role_management.dto.UserLoginRequest;
import com.no99.edunexususer_and_role_management.dto.UserResponse;
import com.no99.edunexususer_and_role_management.entity.User;
import com.no99.edunexususer_and_role_management.mapper.UserMapper;
import com.no99.edunexususer_and_role_management.security.CustomUserPrincipal;
import com.no99.edunexususer_and_role_management.util.JwtTokenProvider;
import com.no99.edunexususer_and_role_management.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务类
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * 用户登录
     */
    public JwtAuthenticationResponse authenticateUser(UserLoginRequest loginRequest) {
        System.out.println("Authenticating user: " + loginRequest.getUsernameOrEmail());

        // 验证用户名/邮箱和密码
        User user = userMapper.findByUsernameOrEmail(loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new BadRequestException("Invalid username/email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid username/email or password");
        }

        // 创建认证对象
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT token
        String jwt = tokenProvider.generateToken(authentication);

        System.out.println("User authenticated successfully: " + user.getUsername());

        return new JwtAuthenticationResponse(jwt, UserResponse.fromUser(user));
    }

    /**
     * 获取当前认证用户
     */
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("No authenticated user found");
        }

        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        return UserResponse.fromUser(userPrincipal.getUser());
    }

    /**
     * 检查当前用户是否有指定角色
     */
    public boolean hasRole(User.UserRole role) {
        try {
            UserResponse currentUser = getCurrentUser();
            return role.getCode().equals(currentUser.getRole());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查当前用户是否为管理员
     */
    public boolean isAdmin() {
        return hasRole(User.UserRole.ADMIN);
    }

    /**
     * 检查当前用户是否为教师
     */
    public boolean isInstructor() {
        return hasRole(User.UserRole.INSTRUCTOR);
    }

    /**
     * 检查当前用户是否为学生
     */
    public boolean isStudent() {
        return hasRole(User.UserRole.STUDENT);
    }
}
