package com.no99.edunexususer_and_role_management.config;

import com.no99.edunexususer_and_role_management.config.UserRoleTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan("com.no99.edunexususer_and_role_management.mapper")
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // 配置MyBatis
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 启用下划线到驼峰命名转换
        configuration.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(configuration);

        // 注册类型处理器
        sessionFactory.setTypeHandlers(new UserRoleTypeHandler());

        return sessionFactory.getObject();
    }
}
