package com.wesal.askhail.core.extentions

fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}


fun String.toBool(): Boolean {
    if (this == "active")
        return true
    return false
}
fun Boolean.convertToString():String{
    return if (this){
        "active"
    }else{
        "block"
    }
}