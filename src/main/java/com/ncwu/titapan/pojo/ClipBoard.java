package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/3/9 11:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClipBoard {
    int type; // 0: 复制 1: 剪切
    UserFileList userFileList;
}
