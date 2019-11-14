package org.mym.ymlib.compiler.data

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

/**
 * 代理方法映射。
 * @property[annotation] 用于代理的注解。
 * @property[delegateClass] 被代理的类。
 * @property[delegateMethod] 被代理的方法。
 */
data class DelegateMapping(
    val annotation: Class<out Annotation>,
    val delegateClass: TypeElement,
    val delegateMethod: ExecutableElement
)

data class CallMapping(
    val callClass: TypeElement,
    val callMethod: ExecutableElement,
    val callMode: MappingMode
)

/**
 * 指明代理时使用何种方式调用代理方法。
 */
enum class MappingMode {
    /**
     * 无参调用模式。
     */
    NO_ARGS,
    /**
     * 参数精确匹配，将原有的方法直接代理进入。
     */
    ACCURATE_PARAM,
    /**
     * 将被代理对象作为唯一参数注入。
     */
    REF,
    /**
     * 将被代理对象和原有参数一并注入。
     */
    REF_AND_PARAM
}