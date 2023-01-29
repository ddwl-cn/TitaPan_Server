package com.ncwu.titapan.service;

import javax.servlet.http.HttpServletResponse;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/8 16:09
 */
public interface DownloadService {


    boolean downloadSingle(HttpServletResponse response, String res_f_name, String f_name);

}
