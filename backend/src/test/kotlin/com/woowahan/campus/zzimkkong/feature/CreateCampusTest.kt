package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
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
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val request = MapPost(campus.name, campus.drawing, campus.thumbnail, slackUrl)

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
                response.header(HttpHeaders.LOCATION).shouldNotBeNull()
            }
        }
    }
})
