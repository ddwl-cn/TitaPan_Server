package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.Star;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/5 11:20
 */
@Mapper
public interface StarMapper {

    Star[] getUserStars(int uid);

    Star getUserStar(int uid, int cid);

    void insertStar(Star star);

    void cancelStar(int uid, int cid);

    void deleteStar(int cid);

}
