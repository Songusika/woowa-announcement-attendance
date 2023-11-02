package com.woowahan.campus.utils

import java.util.*

fun basicEncodePassword(password: String): String {
    return "Basic "+String(Base64.getEncoder().encode(password.toByteArray()))
}
