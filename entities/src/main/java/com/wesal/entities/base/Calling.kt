package com.wesal.entities.base

data class Calling<T> (
    val status:Boolean,
    val data:T,
    val message:String?,
    val error:String
)

