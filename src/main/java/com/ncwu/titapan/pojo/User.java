package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
