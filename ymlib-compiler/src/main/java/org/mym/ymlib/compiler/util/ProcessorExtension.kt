package org.mym.ymlib.compiler.util

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

fun ProcessingEnvironment.note(message: String) {
    messager.printMessage(
        Diagnostic.Kind.WARNING,
        message
    )
}

fun ProcessingEnvironment.error(message: String) {
    messager.printMessage(
        Diagnostic.Kind.ERROR,
        message
    )
}