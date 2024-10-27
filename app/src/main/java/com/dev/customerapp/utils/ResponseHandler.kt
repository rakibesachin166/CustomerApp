package com.dev.customerapp.utils


class ResponseHandler<T>(
    val code: Int,
    val message: String,
    val response: T
)
