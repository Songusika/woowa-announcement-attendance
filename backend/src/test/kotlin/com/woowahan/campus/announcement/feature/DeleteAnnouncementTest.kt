package com.woowahan.campus.announcement.feature

import com.woowahan.campus.utils.DatabaseCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.CreateAnnouncementRequest
import openapi.model.CreateAnnouncementRequestSlackChannel
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import support.test.beforeRootTest
import java.util.Base64

@Import(DatabaseCleaner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteAnnouncementTest(
    @LocalServerPort
    val port: Int,
    val databaseCleaner: DatabaseCleaner
) : BehaviorSpec({

    RestAssured.port = port

    beforeRootTest {
        databaseCleaner.clean()
    }

    Given("공지글 ID, 관리자 비밀번호를 받는다.") {
        val title = "민트는 짱이다."
        val contents = "민트는 짱이다. 이상 전달 끗"
        val slackChannelRequest = CreateAnnouncementRequestSlackChannel(1, "민트 채널")
        val writer = "민트"
        val password = "1234".toByteArray()
        val createAnnouncementRequest =
            CreateAnnouncementRequest(title, contents, writer, slackChannelRequest)

        RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Basic ${String(Base64.getEncoder().encode(password))}"
            )
            .body(createAnnouncementRequest)
            .`when`().post("/api/announcements")
            .then().log().all()

        When("저장된 글을 삭제한다.") {

            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Basic ${String(Base64.getEncoder().encode(password))}"
                ).`when`().delete("/api/announcements/{id}", 1L)
                .then().log().all()
                .extract()

            Then("204 응답을 반환한다.") {
                response.statusCode() shouldBe 204
            }

            Then("조회를 해도 글이 존재하지 않는다.") {
//                조회 기능 생길 시 구현
            }
        }

        When("저장되지 않은 글을 삭제한다.") {

            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Basic ${String(Base64.getEncoder().encode(password))}"
                ).`when`().delete("/api/announcements/{id}", 2L)
                .then().log().all()
                .extract()

            Then("400 응답을 반환한다.") {
                response.statusCode() shouldBe 400
            }
        }

        When("인증되지 않은 사용자가 저장된 글을 삭제한다.") {

            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Basic ${String(Base64.getEncoder().encode("WRONG".toByteArray()))}"
                ).`when`().delete("/api/announcements/{id}", 2L)
                .then().log().all()
                .extract()

            Then("401 응답을 반환한다.") {
                response.statusCode() shouldBe 401
            }
        }
    }
})
