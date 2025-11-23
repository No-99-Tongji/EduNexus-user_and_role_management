package com.no99.edunexususer_and_role_management.service;

import com.no99.edunexususer_and_role_management.dto.UserResponse;
import com.no99.edunexususer_and_role_management.entity.User;
import com.no99.edunexususer_and_role_management.mapper.UserMapper;
import com.no99.edunexususer_and_role_management.security.CustomUserPrincipal;
import com.no99.edunexususer_and_role_management.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 获取当前认证用户
     */
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        // 检查是否为匿名用户
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            throw new BadRequestException("User is not authenticated");
        }

        // 如果 principal 是 CustomUserPrincipal 类型
        if (principal instanceof CustomUserPrincipal) {
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) principal;
            return UserResponse.fromUser(userPrincipal.getUser());
        }

        // 如果 principal 是 String 类型（username），则从数据库加载用户
        if (principal instanceof String) {
            String username = (String) principal;
            User user = userMapper.findByUsername(username)
                    .orElseThrow(() -> new BadRequestException("User not found: " + username));
            return UserResponse.fromUser(user);
        }

        throw new BadRequestException("Invalid authentication principal type: " + principal.getClass().getName());
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
