package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 返回信息
 *
 * @author ddwl.
 * @date 2023/1/9 18:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultMessage<T> {
    // 信息码
    private int status;
    // 返回信息
    private String msg;
    private T data;

}

