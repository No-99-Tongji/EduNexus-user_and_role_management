package com.no99.edunexususer_and_role_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * EduNexus用户和角色管理微服务主应用类
 */
@SpringBootApplication
@EnableTransactionManagement
public class EduNexusUserAndRoleManagementApplication {

    public static void main(String[] args) {
        System.out.println("Starting EduNexus User and Role Management Service...");
        SpringApplication.run(EduNexusUserAndRoleManagementApplication.class, args);
        System.out.println("EduNexus User and Role Management Service started successfully!");
    }

}
