package com.woowahan.campus.announcement.feature

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.transaction.Transactional
import openapi.model.CreateAnnouncementRequest
import openapi.model.CreateAnnouncementRequestSlackChannel
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class GetAnnouncementTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({
    RestAssured.port = port

    Given("단건 공지를 확인할 수 있다.") {
        val title = "민트는 짱이다."
        val contents = "민트는 짱이다. 이상 전달 끗"
        val slackChannelRequest = CreateAnnouncementRequestSlackChannel(1, "민트 채널")
        val writer = "민트"
        val password = "민트짱1234".toByteArray()
        val createAnnouncementRequest = CreateAnnouncementRequest(title, contents, writer, slackChannelRequest)

        val announcement = RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Basic ${String(Base64.getEncoder().encode(password))}")
            .body(createAnnouncementRequest)
            .`when`().post("/api/announcements")
            .then().log().all()
            .extract()

        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Basic ${String(Base64.getEncoder().encode(password))}")

        When("해당 공지글이 보인다.") {
            val response = givenSpec.`when`().get("/api/announcements/1")
                .then().log().all()
                .extract()

            Then("201 응답") {
                response.statusCode() shouldBe 200
            }
        }
    }
})
