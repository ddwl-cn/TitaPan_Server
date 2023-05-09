package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.Mark;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/5 14:27
 */
@Mapper
public interface MarkMapper {

    void insertMark(Mark mark);

    Mark getMark(int uid, int fid);

    Mark[] getMarkNumber(int fid);

    void updateMark(Mark mark);
}
