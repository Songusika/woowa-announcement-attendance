package com.woowahan.campus.zzimkkong.support

import io.restassured.response.ExtractableResponse
import io.restassured.response.Response

class ResponseUtils {

    companion object {
        fun getPrettyJson(response: ExtractableResponse<Response>): String {
            return response.body().asPrettyString().replace("\\\"", "\"")
        }
    }
}
