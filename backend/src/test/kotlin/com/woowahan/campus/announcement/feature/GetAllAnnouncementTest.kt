package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.AnnouncementRepository
import com.woowahan.campus.fixture.createAnnouncement
import com.woowahan.campus.fixture.createAnnouncementsInfoByOffsetResponse
import com.woowahan.campus.utils.DatabaseCleaner
import com.woowahan.campus.utils.basicEncodePassword
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import openapi.model.AnnouncementsInfoByOffsetResponse
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import support.test.beforeRootTest

@Import(DatabaseCleaner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetAllAnnouncementTest(
    @LocalServerPort
    private val port: Int,
    private val announcementRepository: AnnouncementRepository,
    private val databaseCleaner: DatabaseCleaner
) : BehaviorSpec({

    RestAssured.port = port

    Given("등록된 공지가 20개가 있을 때") {

        val announcements = (1..20).map { count ->
            announcementRepository.save(
                createAnnouncement(
                    "${count}번 째 공지",
                    "${count}번 째 공지입니다.",
                    "작성자",
                    1
                )
            )
        }.toList()

        When("옳바른 비밀번호, 페이지 번호, 조회하는 공지 개수를 요청하면") {

            val queryStrings = mapOf(Pair("page", 1), Pair("size", 5))
            val response = sendRequest("/api/announcements/offset", queryStrings, "1234");
            val responseBody = response.`as`(AnnouncementsInfoByOffsetResponse::class.java)

            Then("해당 페이지에 존재하는 공지의 id, 제목, 작성자, 작성일 목록을 요청 개수 만큼 반환한다.") {
                response.statusCode() shouldBe 200
                responseBody shouldBeEqualToComparingFields createAnnouncementsInfoByOffsetResponse(
                    announcements.slice(5..9),
                    1,
                    5,
                    20,
                    4
                )
            }
        }

        When("옳바른 비밀번호와 조회하는 공지 개수만 입력하면") {

            val queryStrings = mapOf(Pair("size", 5))
            val response = sendRequest("/api/announcements/offset", queryStrings, "1234");
            val responseBody = response.`as`(AnnouncementsInfoByOffsetResponse::class.java)

            Then("0번째 페이지가 반환된다.") {
                response.statusCode() shouldBe 200
                responseBody shouldBeEqualToComparingFields createAnnouncementsInfoByOffsetResponse(
                    announcements.slice(0..4),
                    0,
                    5,
                    20,
                    4
                )
            }
        }

        When("옳바른 비밀번호와 조회하는 페이지 번호만 입력하면") {

            val queryStrings = mapOf(Pair("page", 1))
            val response = sendRequest("/api/announcements/offset", queryStrings, "1234");
            val responseBody = response.`as`(AnnouncementsInfoByOffsetResponse::class.java)

            Then("해당 페이지의 10개의 공지 목록이 반환된다.") {
                response.statusCode() shouldBe 200
                responseBody shouldBeEqualToComparingFields createAnnouncementsInfoByOffsetResponse(
                    announcements.slice(10..19),
                    1,
                    10,
                    20,
                    2
                )
            }
        }

        xWhen("옳바른 비밀번호만 입력하면") {

            val queryStrings = emptyMap<String, Int>()
            val response = sendRequest("/api/announcements/offset", queryStrings, "1234");
            val responseBody = response.`as`(AnnouncementsInfoByOffsetResponse::class.java)

            Then("0번째 페이지의 10개의 공지 목록을 조회한다.") {
                response.statusCode() shouldBe 200
                responseBody shouldBeEqualToComparingFields createAnnouncementsInfoByOffsetResponse(
                    announcements.slice(0..9),
                    0,
                    10,
                    20,
                    2
                )
            }
        }

        xWhen("틀린 비밀번호와 페이지 번호, 조회하는 공지 개수를 입력하면") {

            Then("401 응답이 반환된다.") {

            }
        }
    }

    Given("등록된 공지가 없을 때") {

        When("공지 목록을 조회하면") {

            val queryStrings = mapOf(Pair("page", 1), Pair("size", 5))
            val response = sendRequest("/api/announcements/offset", queryStrings, "1234");
            val responseBody = response.`as`(AnnouncementsInfoByOffsetResponse::class.java)

            Then("빈 결과를 응답한다") {
                response.statusCode() shouldBe 200
                responseBody.announcements shouldHaveSize 0
                responseBody.page shouldBe 1
                responseBody.propertySize shouldBe 5
                responseBody.totalElements shouldBe 0
                responseBody.totalPages shouldBe 0
            }
        }
    }

    beforeRootTest {
        databaseCleaner.clean()
    }
})

fun sendRequest(url: String, queryString: Map<String, Int>, password: String): ExtractableResponse<Response> {
    return RestAssured
        .given().log().all()
        .header(HttpHeaders.AUTHORIZATION, basicEncodePassword(password))
        .queryParams(queryString)
        .`when`().get(url)
        .then().log().all()
        .extract()
}
