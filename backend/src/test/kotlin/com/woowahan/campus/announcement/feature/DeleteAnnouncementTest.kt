package com.woowahan.campus.announcement.feature

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.CreateAnnouncementRequest
import openapi.model.CreateAnnouncementRequestSlackChannel
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteAnnouncementTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({

    RestAssured.port = port




    Given("공지글 ID, 관리자 비밀번호를 받는다.") {
        val title = "민트는 짱이다."
        val contents = "민트는 짱이다. 이상 전달 끗"
        val slackChannelRequest = CreateAnnouncementRequestSlackChannel(1, "민트 채널")
        val writer = "민트"
        val password = "민트짱1234".toByteArray()
        val createAnnouncementRequest =
            CreateAnnouncementRequest(title, contents, writer, slackChannelRequest)
        val extract = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Basic ${String(Base64.getEncoder().encode(password))}"
            )
            .body(createAnnouncementRequest)
            .`when`().post("/api/announcements")
            .then().log().all()

        val id = 1L

        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Basic ${String(Base64.getEncoder().encode(password))}"
            )

        When("저장된 글을 삭제한다.") {

            val response = givenSpec.`when`().delete("/api/announcements/{id}", id)
                .then().log().all()
                .extract()

            Then("204 응답을 반환한다.") {
                response.statusCode() shouldBe 204
            }
        }
    }


})
