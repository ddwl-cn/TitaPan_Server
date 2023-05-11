package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 用于用户贡献的公共文件审批
 *
 * @author ddwl.
 * @date 2023/5/10 18:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicFileTask {
    int tid; // 任务id
    int uid; // 申请上传的用户
    int fid; // 对应的文件实体
    FileChunk fileChunk; // 用户上传的公共文件信息
    PublicFile publicFile; // 公共文件信息
    int state; // 当前状态 0: 审批中 1：审批已通过 2：审批未通过
    String remark; // 用户备注
    String suggestion; // 审批建议 仅用于审批未通过时管理员返回审批意见
}
