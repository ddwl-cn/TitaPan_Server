package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.PublicFileTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/10 18:54
 */
@Mapper
public interface PublicFileTaskMapper {

    void insertTask(PublicFileTask publicFileTask);

    void updateTask(PublicFileTask publicFileTask);

    void deleteTaskByTid(int tid);

    void deleteTask(int tid, int uid);

    PublicFileTask getTaskById(int tid);

    PublicFileTask[] getAllTasks();

    PublicFileTask[] getTaskByUid(int uid);

}
