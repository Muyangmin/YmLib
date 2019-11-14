package org.mym.ymlib.ext

/**
 * 标记接口，用于提供一个较为通用的手动退出应用的方法。
 */
interface ManualExitApplication {

    /**
     * 退出应用。
     *
     * @param[code] 用于说明退出原因。按照惯例，为 0 表示正常退出。
     */
    fun onExit(code: Int)
}