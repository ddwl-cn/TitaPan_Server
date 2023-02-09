package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/2/8 16:50
 */
@Mapper
public interface TokenMapper {
    Token getUserToken(@Param("uid") int uid, @Param("token") String token);
    void insertNewToken(@Param("uid") int uid, @Param("token") String token);
    void deleteToken(@Param("uid") int uid, @Param("token") String token);
}
