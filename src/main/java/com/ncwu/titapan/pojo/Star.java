package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/5 11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Star {
    int sid;
    int uid; // 谁点的赞
    int cid; // 给哪条评论点的赞
}
