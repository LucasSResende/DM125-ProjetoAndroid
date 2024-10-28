package com.lucas.dm125mytasks.repository

data class ResponseDto<T>(
    val value: T? = null,
    val isError: Boolean = false
)
