package com.cloudlibrary_api.common.handler;

import com.cloudlibrary_api.common.enums.RecordStatusEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import java.sql.*;

@MappedTypes(RecordStatusEnum.class)
public class RecordStatusEnumTypeHandler extends BaseTypeHandler<RecordStatusEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    RecordStatusEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public RecordStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return RecordStatusEnum.fromCode(code);
    }

    @Override
    public RecordStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return RecordStatusEnum.fromCode(code);
    }

    @Override
    public RecordStatusEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return RecordStatusEnum.fromCode(code);
    }
}