package com.no99.edunexususer_and_role_management.config;

import com.no99.edunexususer_and_role_management.entity.User;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户角色类型处理器
 */
@MappedTypes(User.UserRole.class)
public class UserRoleTypeHandler extends BaseTypeHandler<User.UserRole> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, User.UserRole parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public User.UserRole getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return code != null ? User.UserRole.fromCode(code) : null;
    }

    @Override
    public User.UserRole getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return code != null ? User.UserRole.fromCode(code) : null;
    }

    @Override
    public User.UserRole getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return code != null ? User.UserRole.fromCode(code) : null;
    }
}
