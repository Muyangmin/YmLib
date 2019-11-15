package org.mym.ymlib.compiler.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import javax.lang.model.element.TypeElement

fun buildFieldSpec(type: TypeElement): FieldSpec {
    return FieldSpec.builder(ClassName.get(type), decideFieldName(type))
        .initializer("new \$T()", type)
        .build()
}