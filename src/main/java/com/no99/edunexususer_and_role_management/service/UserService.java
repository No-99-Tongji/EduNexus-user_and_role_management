package com.no99.edunexususer_and_role_management.service;

import com.no99.edunexususer_and_role_management.dto.*;
import com.no99.edunexususer_and_role_management.entity.User;
import com.no99.edunexususer_and_role_management.mapper.UserMapper;
import com.no99.edunexususer_and_role_management.exception.ResourceNotFoundException;
import com.no99.edunexususer_and_role_management.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务类
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     */
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        System.out.println("Registering new user with username: " + request.getUsername());

        // 检查用户名是否已存在
        if (userMapper.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        // 检查邮箱是否已存在
        if (userMapper.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email Address already in use!");
        }

        // 创建新用户
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(User.UserRole.STUDENT) // 默认角色为学生
                .timezone(request.getTimezone() != null ? request.getTimezone() : "UTC")
                .build();

        userMapper.insert(user);
        System.out.println("User registered successfully with ID: " + user.getId());

        return UserResponse.fromUser(user);
    }

    /**
     * 根据ID获取用户
     */
    public UserResponse getUserById(Long id) {
        User user = userMapper.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserResponse.fromUser(user);
    }

    /**
     * 根据用户名获取用户
     */
    public UserResponse getUserByUsername(String username) {
        User user = userMapper.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return UserResponse.fromUser(user);
    }

    /**
     * 获取所有用户
     */
    public List<UserResponse> getAllUsers() {
        return userMapper.findAll().stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    /**
     * 分页获取用户
     */
    public List<UserResponse> getUsersWithPagination(int page, int size) {
        int offset = page * size;
        return userMapper.findAllWithPagination(offset, size).stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    /**
     * 根据角色获取用户
     */
    public List<UserResponse> getUsersByRole(User.UserRole role) {
        return userMapper.findByRole(role.getCode()).stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        System.out.println("Updating user with ID: " + id);

        User user = userMapper.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // 检查邮箱是否被其他用户使用
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userMapper.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email Address already in use!");
            }
            user.setEmail(request.getEmail());
        }

        // 更新其他字段
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getTimezone() != null) {
            user.setTimezone(request.getTimezone());
        }

        // 更新密码
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            userMapper.updatePassword(id, user.getPasswordHash());
        }

        userMapper.update(user);
        System.out.println("User updated successfully with ID: " + id);

        return UserResponse.fromUser(user);
    }

    /**
     * 更新用户角色
     */
    @Transactional
    public UserResponse updateUserRole(Long id, User.UserRole role) {
        System.out.println("Updating user role for ID: " + id + " to role: " + role);

        User user = userMapper.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setRole(role);
        userMapper.update(user);

        System.out.println("User role updated successfully for ID: " + id);
        return UserResponse.fromUser(user);
    }

    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        System.out.println("Deleting user with ID: " + id);

        if (!userMapper.findById(id).isPresent()) {
            throw new ResourceNotFoundException("User", "id", id);
        }

        userMapper.deleteById(id);
        System.out.println("User deleted successfully with ID: " + id);
    }

    /**
     * 检查用户名是否可用
     */
    public boolean isUsernameAvailable(String username) {
        return !userMapper.existsByUsername(username);
    }

    /**
     * 检查邮箱是否可用
     */
    public boolean isEmailAvailable(String email) {
        return !userMapper.existsByEmail(email);
    }

    /**
     * 获取用户总数
     */
    public long getUserCount() {
        return userMapper.count();
    }
}
