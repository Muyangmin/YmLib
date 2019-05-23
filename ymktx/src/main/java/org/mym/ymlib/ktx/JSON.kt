package org.mym.ymlib.ktx

import org.json.JSONArray
import org.json.JSONObject

/**
 * Generate a json string represent this map.
 */
fun <K, V> Map<K, V>.toJsonString(): String {
    val json = JSONObject()
    for ((key, value) in this) {
        //直接传递 null 值将不会产生效果
        json.put(key.toString(), value ?: JSONObject.NULL)
    }
    return json.toString()
}

/**
 * Generate a json array represent this collection.
 */
fun <E> Collection<E>.toJSONArray() = JSONArray(this)