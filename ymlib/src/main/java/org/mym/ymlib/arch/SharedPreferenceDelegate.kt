package org.mym.ymlib.arch

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 为 SharedPreference 操作提供简单的委托模式实现。
 *
 * * 调用者只需要简单声明一个存取位置，无需编写模板代码；
 * * 支持原生 sp 全部数据类型 ([boolean], [int], [long], [float], [string], [stringSet])；
 * * 支持指定默认值。
 *
 * 示例用法：
 *
 * ```Kotlin
 * //repo class
 * object SpData {
 *    private const val FILE_NAME = "local_prefs"
 *    private val prefDelegate = SharedPreferenceDelegate {
 *        someContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
 *    }
 *    var uuid: String? by prefDelegate.string()
 * }
 *
 * //caller
 * val uuid = SpData.uuid
 * SpData.uuid = "abc"
 * ```
 */
class SharedPreferenceDelegate(prefCreator: () -> SharedPreferences) {
    private val preferences: SharedPreferences by lazy(prefCreator)

    fun boolean(defaultValue: Boolean = false) = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return preferences.getBoolean(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            preferences.edit {
                putBoolean(property.name, value)
            }
        }
    }

    fun int(defaultValue: Int = 0) = object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return preferences.getInt(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            preferences.edit {
                putInt(property.name, value)
            }
        }
    }

    fun long(defaultValue: Long = 0L) = object : ReadWriteProperty<Any, Long> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return preferences.getLong(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
            preferences.edit {
                putLong(property.name, value)
            }
        }
    }

    fun float(defaultValue: Float = 0F) = object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return preferences.getFloat(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
            preferences.edit {
                putFloat(property.name, value)
            }
        }
    }

    fun string(defaultValue: String? = null) = object : ReadWriteProperty<Any, String?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String? {
            return preferences.getString(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
            preferences.edit {
                putString(property.name, value)
            }
        }
    }

    fun stringSet(defaultValue: Set<String>? = null) =
        object : ReadWriteProperty<Any, Set<String>?> {
            override fun getValue(thisRef: Any, property: KProperty<*>): Set<String>? {
                return preferences.getStringSet(property.name, defaultValue)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<String>?) {
                preferences.edit {
                    putStringSet(property.name, value)
                }
            }
        }

    /**
     * 任意可序列化的对象，适合格式化的方便存取的数据 (例如使用 gson 或 jackson).
     *
     * @param[serializer] 用于将对象 `T` 序列化为 String, 注意参数可能为 `null`.
     * @param[deserializer] 用于将字符串反序列化为对象 `T`, 注意参数可能为 `null`..
     */
    fun <T> serialObject(serializer: (T?) -> String?, deserializer: (String?) -> T?) =
        object : ReadWriteProperty<Any, T?> {
            override fun getValue(thisRef: Any, property: KProperty<*>): T? {
                return deserializer(preferences.getString(property.name, null))
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
                preferences.edit {
                    putString(property.name, serializer(value))
                }
            }
        }
}