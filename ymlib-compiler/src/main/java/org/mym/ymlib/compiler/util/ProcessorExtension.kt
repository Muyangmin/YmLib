package org.mym.ymlib.compiler.util

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

fun ProcessingEnvironment.note(message: String, element: Element? = null) {
    messager.printMessage(
        Diagnostic.Kind.WARNING,
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