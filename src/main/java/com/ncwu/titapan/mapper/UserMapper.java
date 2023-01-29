package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:42
 */
@Mapper
public interface UserMapper {
    // 查询用户实体
    User getUserInfoByUserName(String u_name);
    User getUserInfoByUserId(int uid);

    // 查询用户状态
    int getUserStateByUserName(String u_name);
    int getUserStateByUserId(int uid);

    // 查询用户密码
    String getUserPasswordByUserName(String u_name);
    String getUserPasswordByUserId(int uid);

    void insertNewUser(User user);


}
