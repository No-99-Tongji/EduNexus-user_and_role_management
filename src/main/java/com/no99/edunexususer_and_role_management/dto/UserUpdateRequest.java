package com.no99.edunexususer_and_role_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * 用户更新请求DTO
 */
public class UserUpdateRequest {

    @Email(message = "邮箱格式不正确")
    private String email;

    private String firstName;

    private String lastName;

    private String avatarUrl;

    private String timezone;

    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;

    public UserUpdateRequest() {}

    public UserUpdateRequest(String email, String firstName, String lastName,
                           String avatarUrl, String timezone, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.timezone = timezone;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

