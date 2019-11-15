package org.mym.ymlib.compiler.util

import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

/**
 * 字段名。
 */
internal fun decideFieldName(fieldType: TypeElement): String {
    return fieldType.simpleName.toString().unCapitalize()
}

/**
 * 被代理对象的 this 引用名。
 */
internal fun decideRefArgName(ref: TypeElement): String {
    return ref.simpleName.toString().lastWord()
}

/**
 * 方法参数名。
 */
internal fun decideMethodArgName(arg: VariableElement): String {
    return arg.simpleName.toString()
}