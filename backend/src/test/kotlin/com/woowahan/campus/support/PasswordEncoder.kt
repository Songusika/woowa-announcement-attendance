package com.woowahan.campus.support

import java.util.Base64

fun basicEncodePassword(password: String): String {
    return "Basic " + String(Base64.getEncoder().encode(password.toByteArray()))
}
