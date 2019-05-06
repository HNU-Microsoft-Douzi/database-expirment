/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Model
 * Author:   Administrator
 * Date:     2019/5/6 0006 21:04
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

package gbn;

/**
 * 保证线程安全性
 */
public class Model {

    public volatile int time;

    public  synchronized int getTime() {
        return time;
    }

    public synchronized void setTime(int time) {
        this.time = time;
    }
}