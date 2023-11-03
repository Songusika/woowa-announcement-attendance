package com.woowahan.campus.utils

import java.util.Base64

fun basicEncodePassword(password: String): String {
    return "Basic " + String(Base64.getEncoder().encode(password.toByteArray()))
}
