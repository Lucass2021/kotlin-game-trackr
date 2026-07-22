package com.lucasdias.gametrackr.core.format

fun Int.abbreviated(): String {
    if (this < 1000) return this.toString()
    val thousands = this / 1000.0
    return "%.1fk".format(thousands).replace(".0k", "k")
}
