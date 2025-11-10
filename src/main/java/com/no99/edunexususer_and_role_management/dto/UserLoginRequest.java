package com.no99.edunexususer_and_role_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录请求DTO
 */
@Schema(description = "用户登录请求")
public class UserLoginRequest {

    @Schema(description = "用户名或邮箱", example = "john_doe", required = true)
    @NotBlank(message = "用户名或邮箱不能为空")
    private String usernameOrEmail;

    @Schema(description = "密码", example = "password123", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    public UserLoginRequest() {}

    public UserLoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
