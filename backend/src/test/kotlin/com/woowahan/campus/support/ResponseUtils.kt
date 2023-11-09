package com.woowahan.campus.support

import io.restassured.response.ExtractableResponse
import io.restassured.response.Response

fun ExtractableResponse<Response>.asPrettyJson(): String {
    return this.body().asPrettyString().replace("\\\"", "\"")
}
