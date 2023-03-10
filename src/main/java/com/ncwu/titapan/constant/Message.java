package com.ncwu.titapan.constant;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/9 18:44
 */
public class Message {
    // 上传任务完成
    public static final String uploadComplete = "uploadComplete";
    // 当前块上传完成
    public static final String uploadChunkComplete = "uploadChunkComplete";
    // 下载成功
    public static final String downloadComplete = "downloadComplete";
    // 数据格式错误
    public static final String dataFormatError = "dataFormatError";
    // 未知错误
    public static final String unknownError = "unknownError";
    // token无效
    public static final String invalidToken = "invalidToken";
    // token过期
    public static final String tokenExpire = "tokenExpire";
    // 用户信息有误
    public static final String userInfoError = "userInfoError";
    // 登陆成功
    public static final String loginSuccess = "loginSuccess";
    // 文件名重复
    public static final String fileNameRepetitive = "fileNameRepetitive";
    // 普传
    public static final String commonUpload = "commonUpload";
    // 快传
    public static final String quickUpload = "quickUpload";
    // 文件存在
    public static final String fileExist = "fileExist";
    // 文件块存在
    public static final String fileChunkExist = "fileChunkExist";
    // 改变路径成功
    public static final String changePathSuccess = "changePathSuccess";
    // 查询用户文件列表成功
    public static final String getUserFileListSuccess = "getUserFileListSuccess";
    // 修改文件名称成功
    public static final String updateFileNameSuccess = "updateFileNameSuccess";
    public static final String createFolderSuccess = "createFolderSuccess";
    // 删除文件成功
    public static final String deleteFileSuccess = "deleteFileSuccess";

    public static final String downloadFolderNotSupport = "downloadFolderNotSupport";

    public static final String registrySuccess = "registrySuccess";

    public static final String userExist = "userExist";

    public static final String createShareLinkSuccess = "createShareLinkSuccess";

    public static final String extractSuccess = "extractSuccess";
    public static final String shareLinkDeleted = "shareLinkDeleted";
    public static final String shareLinkExpired = "shareLinkExpired";
    public static final String shareCodeError = "shareCodeError";
    public static final String getShareListSuccess = "getShareListSuccess";
    public static final String deleteShareLinkSuccess = "deleteShareLinkSuccess";
    public static final String getSharedFileInfoSuccess = "getSharedFileInfoSuccess";
    public static final String getShareLinkInfoSuccess = "getShareLinkInfoSuccess";

    public static final String getUserFolderListSuccess = "getUserFolderListSuccess";
    public static final String getUserInfoSuccess = "getUserInfoSuccess";
    public static final String logoutSuccess = "logoutSuccess";

    public static final String getPublicFileListSuccess = "getPublicFileListSuccess";

    public static final String updatePublicFileInfoSuccess = "updatePublicFileInfoSuccess";
    public static final String deletePublicFileSuccess = "deletePublicFileSuccess";
    public static final String copyFileSuccess = "copyFileSuccess";
    public static final String cutFileSuccess = "cutFileSuccess";
    public static final String pasteFileSuccess = "pasteFileSuccess";
    public static final String pasteRecursive = "pasteRecursive";

    // 成功
    public static final int SUCCESS = 200;
    // 异常
    public static final int WARNING = 300;
    // 错误
    public static final int ERROR = 400;

}
