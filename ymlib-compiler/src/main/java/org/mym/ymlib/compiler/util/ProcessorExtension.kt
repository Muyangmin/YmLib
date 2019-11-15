package org.mym.ymlib.compiler.util

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.tools.Diagnostic

fun ProcessingEnvironment.note(message: String, element: Element? = null) {
    messager.printMessage(
        Diagnostic.Kind.NOTE,
        message,
        element
    )
}

fun ProcessingEnvironment.error(message: String, element: Element? = null) {
    messager.printMessage(
        Diagnostic.Kind.ERROR,
        message,
        element
    )
}

fun ProcessingEnvironment.typeElementFromQualifier(qualifier: CharSequence): TypeElement {
    return requireNotNull(elementUtils.getTypeElement(qualifier)) {
        "Unable to find type $qualifier"
    }
}

fun ProcessingEnvironment.classNameFromQualifier(qualifier: CharSequence): ClassName {
    return ClassName.get(typeElementFromQualifier(qualifier))
}

/**
 * 尝试在指定的类型（及其父类）中寻找匹配的方法对象。如果无法找到则返回 null。
 */
fun ProcessingEnvironment.findTargetExecutableElement(
    typeElement: TypeElement?,
    methodName: String
): ExecutableElement {
    return requireNotNull(findTargetExecutableElementOrNull(typeElement, methodName), {
        "Unable to find mapping method $typeElement.$methodName, please check again."
    })
}

private fun ProcessingEnvironment.findTargetExecutableElementOrNull(
    typeElement: TypeElement?,
    methodName: String
): ExecutableElement? {
    //尝试查找匹配的方法
    var targetElement: ExecutableElement? = null
    var currentType = typeElement
    //递归向上搜索
    while (targetElement == null && currentType != null) {
        targetElement = currentType.enclosedElements.firstOrNull {
            it is ExecutableElement && it.simpleName.toString() == methodName
        } as? ExecutableElement

        val superClass = currentType.superclass
        //到达根类型 java.lang.Object，退出搜索
        if (superClass.kind == TypeKind.NONE) {
            break
        }
        currentType = typeUtils.asElement(superClass) as? TypeElement
    }
    return targetElement
}
