package com.no99.edunexususer_and_role_management.mapper;

import com.no99.edunexususer_and_role_management.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper {

    /**
     * 根据ID查找用户
     */
    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> findById(Long id);

    /**
     * 根据用户名查找用户
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<User> findByEmail(String email);

    /**
     * 根据用户名或邮箱查找用户
     */
    @Select("SELECT * FROM users WHERE username = #{usernameOrEmail} OR email = #{usernameOrEmail}")
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    /**
     * 查找所有用户
     */
    @Select("SELECT * FROM users ORDER BY created_at DESC")
    List<User> findAll();

    /**
     * 根据角色查找用户
     */
    @Select("SELECT * FROM users WHERE role = #{role} ORDER BY created_at DESC")
    List<User> findByRole(String role);

    /**
     * 分页查询用户
     */
    @Select("SELECT * FROM users ORDER BY created_at DESC LIMIT #{offset}, #{size}")
    List<User> findAllWithPagination(@Param("offset") int offset, @Param("size") int size);

    /**
     * 统计用户总数
     */
    @Select("SELECT COUNT(*) FROM users")
    long count();

    /**
     * 插入新用户
     */
    @Insert("INSERT INTO users (username, email, password_hash, first_name, last_name, role, avatar_url, timezone, created_at, updated_at) " +
            "VALUES (#{username}, #{email}, #{passwordHash}, #{firstName}, #{lastName}, #{role}, #{avatarUrl}, #{timezone}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 更新用户信息
     */
    @Update("UPDATE users SET email = #{email}, first_name = #{firstName}, last_name = #{lastName}, " +
            "role = #{role}, avatar_url = #{avatarUrl}, timezone = #{timezone}, updated_at = NOW() " +
            "WHERE id = #{id}")
    int update(User user);

    /**
     * 更新用户密码
     */
    @Update("UPDATE users SET password_hash = #{passwordHash}, updated_at = NOW() WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);

    /**
     * 删除用户
     */
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    boolean existsByEmail(String email);
}
