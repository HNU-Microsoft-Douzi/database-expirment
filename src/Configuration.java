/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Configuration
 * Author:   Administrator
 * Date:     2019/5/6 0006 8:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


public class Configuration {
    public String getSERVER_RECEIVE_DIR() {
        return SERVER_RECEIVE_DIR;
    }

    public String getSERVER_SHARED_DIR() {
        return SERVER_SHARED_DIR;
    }

    private static final String SERVER_RECEIVE_DIR = "src\\ftp_receive_dic";

    private static final String SERVER_SHARED_DIR = "src\\ftp_shared_dic";
}
