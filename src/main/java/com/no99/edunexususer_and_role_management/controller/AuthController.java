package com.no99.edunexususer_and_role_management.controller;

import com.no99.edunexususer_and_role_management.dto.JwtAuthenticationResponse;
import com.no99.edunexususer_and_role_management.dto.UserLoginRequest;
import com.no99.edunexususer_and_role_management.dto.UserRegistrationRequest;
import com.no99.edunexususer_and_role_management.dto.UserResponse;
import com.no99.edunexususer_and_role_management.service.AuthService;
import com.no99.edunexususer_and_role_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "认证管理", description = "用户注册、登录等认证相关API")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "新用户注册账号")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "注册成功",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "409", description = "用户名或邮箱已存在")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Parameter(description = "用户注册信息", required = true)
            @Valid @RequestBody UserRegistrationRequest request) {
        System.out.println("User registration request received for username: " + request.getUsername());
        UserResponse userResponse = userService.registerUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户通过用户名/邮箱和密码登录系统")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功",
                    content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "401", description = "用户名/邮箱或密码错误")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(
            @Parameter(description = "用户登录信息", required = true)
            @Valid @RequestBody UserLoginRequest request) {
        System.out.println("User login request received for: " + request.getUsernameOrEmail());
        JwtAuthenticationResponse jwtResponse = authService.authenticateUser(request);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "未登录或token无效")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse userResponse = authService.getCurrentUser();
        return ResponseEntity.ok(userResponse);
    }

    /**
     * 检查用户名是否可用
     */
    @Operation(summary = "检查用户名可用性", description = "检查指定用户名是否可以使用")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "检查完成"),
            @ApiResponse(responseCode = "400", description = "用户名格式不正确")
    })
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(
            @Parameter(description = "要检查的用户名", required = true)
            @RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(Map.of("available", available));
    }

    /**
     * 检查邮箱是否可用
     */
    @Operation(summary = "检查邮箱可用性", description = "检查指定邮箱是否可以使用")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "检查完成"),
            @ApiResponse(responseCode = "400", description = "邮箱格式不正确")
    })
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailAvailability(
            @Parameter(description = "要检查的邮箱地址", required = true)
            @RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        return ResponseEntity.ok(Map.of("available", available));
    }
}
