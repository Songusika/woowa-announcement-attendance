package com.woowahan.campus.zzimkkong.feature

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.MapPost
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateCampusTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({

    RestAssured.port = port

    Given("이름, 드로잉, 썸네일, 슬랙 URL을 받는다.") {
        val name = "잠실 캠퍼스"
        val drawing =
            "{\"width\":100,\"height\":100,\"mapElements\":[{\"id\":2,\"type\":\"polyline\",\"stroke\":\"#333333\",\"points\":[\"50,40\",\"40,20\"]},{\"id\":3,\"type\":\"polyline\",\"stroke\":\"#333333\",\"points\":[\"40,20\",\"20,20\"]},{\"id\":4,\"type\":\"polyline\",\"stroke\":\"#333333\",\"points\":[\"20,20\",\"10,50\"]},{\"id\":5,\"type\":\"polyline\",\"stroke\":\"#333333\",\"points\":[\"10,50\",\"50,90\"]},{\"id\":6,\"type\":\"polyline\",\"stroke\":\"#333333\",\"points\":[\"50,90\",\"90,50\"]},{\"id\":7,\"type\":\"polyline\",\"stroke\":\"#333333\",\"points\":[\"90,50\",\"80,20\"]},{\"id\":8,\"type\":\"polyline\",\"stroke\":\"#333333\",\"points\":[\"80,20\",\"60,20\"]},{\"id\":9,\"type\":\"polyline\",\"stroke\":\"#333333\",\"points\":[\"60,20\",\"50,40\"]}]}"
        val thumbnail =
            "<?xml version=\"1.0\"?><svg fill=\"#000000\" xmlns=\"http://www.w3.org/2000/svg\"  viewBox=\"0 0 30 30\" width=\"30px\" height=\"30px\">    <path d=\"M 7 4 C 6.744125 4 6.4879687 4.0974687 6.2929688 4.2929688 L 4.2929688 6.2929688 C 3.9019687 6.6839688 3.9019687 7.3170313 4.2929688 7.7070312 L 11.585938 15 L 4.2929688 22.292969 C 3.9019687 22.683969 3.9019687 23.317031 4.2929688 23.707031 L 6.2929688 25.707031 C 6.6839688 26.098031 7.3170313 26.098031 7.7070312 25.707031 L 15 18.414062 L 22.292969 25.707031 C 22.682969 26.098031 23.317031 26.098031 23.707031 25.707031 L 25.707031 23.707031 C 26.098031 23.316031 26.098031 22.682969 25.707031 22.292969 L 18.414062 15 L 25.707031 7.7070312 C 26.098031 7.3170312 26.098031 6.6829688 25.707031 6.2929688 L 23.707031 4.2929688 C 23.316031 3.9019687 22.682969 3.9019687 22.292969 4.2929688 L 15 11.585938 L 7.7070312 4.2929688 C 7.5115312 4.0974687 7.255875 4 7 4 z\"/></svg>"
        val slackUrl = "https://slackexample.com"
        val request = MapPost(name, drawing, thumbnail, slackUrl)

        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)

        When("캠퍼스 정보를 저장한다.") {

            val response = givenSpec.`when`().post("/api/maps")
                .then().log().all()
                .extract()

            Then("201 응답과 저장된 캠퍼스의 URI를 Location Header로 반환한다.") {
                response.statusCode() shouldBe 201
                response.header(HttpHeaders.LOCATION) shouldNotBe null
            }
        }
    }
})
