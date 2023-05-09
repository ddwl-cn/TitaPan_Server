package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    int uid;
    String u_name;
    String u_password;
    int u_state;
    String nike_name;
    int type;
    String email;
    String avatar_url;
    int age;
    String mobilePhoneNumber;
    String area;
    String sex;
    String work;
    String hobby;
    String design;
    MultipartFile avatar;
}
