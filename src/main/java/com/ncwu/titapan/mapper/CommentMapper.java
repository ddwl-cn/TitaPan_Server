package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/4 21:07
 */
@Mapper
public interface CommentMapper {
    void insertComment(Comment comment);

    Comment[] getComment(int fid);

    Comment getCommentByCid(int cid);

    Comment[] getCommentBelongTo(int cid);

    void updateReplyNumber(int cid);

    void starComment(int cid);

    void cancelStarComment(int cid);

    void updateCommentAvatarURL(@Param("uid") int uid, @Param("avatar_url") String avatar_url);

    void deleteComment(int cid);
}
