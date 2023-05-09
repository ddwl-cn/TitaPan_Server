package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/4 20:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    int cid;

    int belong_to; // 属于谁
    int reply_to; // 回复的谁 默认-1 其余楼中楼 为回复的评论的id
    String reply_nike_name;
    int uid; // 谁评论的
    int fid;
    int star;
    int reply_num;
    boolean is_star; // 是否已经点赞
    String nike_name; // 单词拼错了... 但是不想把前面的一个个修改了
    String avatar_url; // 头像链接
    String comment_date; // 评论日期
    String content; // 评论内容

    // 把某条评论的回复打包到一起 方便返回给前端
    Comment []replies;
}
