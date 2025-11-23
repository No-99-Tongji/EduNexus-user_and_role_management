package com.no99.edunexususer_and_role_management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "系统监控", description = "系统健康检查和服务信息API")
public class HealthController {

    /**
     * 健康检查端点
     */
    @Operation(summary = "健康检查", description = "检查服务是否正常运行")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "服务运行正常")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "EduNexus User and Role Management",
            "timestamp", LocalDateTime.now(),
            "version", "1.0.0"
        ));
    }

    /**
     * 服务信息
     */
    @Operation(summary = "获取服务信息", description = "获取服务的详细信息和功能特性")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功")
    })
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
            "service", "EduNexus User and Role Management Service",
            "description", "Microservice for managing users and roles in EduNexus platform",
            "version", "1.0.0",
            "author", "EduNexus Team",
            "features", new String[]{
                "User Registration & Authentication",
                "Role-based Authorization",
                "User Profile Management",
                "Password Encryption"
            }
        ));
    }
}
