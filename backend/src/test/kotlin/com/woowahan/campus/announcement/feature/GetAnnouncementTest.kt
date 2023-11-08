package com.woowahan.campus.announcement.feature

import com.woowahan.campus.announcement.domain.AnnouncementRepository
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannel
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannelRepository
import com.woowahan.campus.fixture.createAnnouncement
import com.woowahan.campus.fixture.createAnnouncementResponse
import com.woowahan.campus.utils.DatabaseCleaner
import com.woowahan.campus.utils.basicEncodePassword
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.AnnouncementResponse
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import support.test.beforeRootTest

@Import(DatabaseCleaner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetAnnouncementTest(
    @LocalServerPort
    val port: Int,
    private val announcementRepository: AnnouncementRepository,
    private val announcementSlackRepository: AnnouncementSlackChannelRepository,
    private val databaseCleaner: DatabaseCleaner,
) : BehaviorSpec({

    RestAssured.port = port

    beforeRootTest {
        databaseCleaner.clean()
    }

    Given("하나의 공지가 있을 때") {
        val title = "민트는 짱이다."
        val contents = "민트는 짱이다. 이상 전달 끗"
        val author = "민트"
        val password = "1234"

        val slackChannel = announcementSlackRepository.save(AnnouncementSlackChannel("CX0L", "6기-공지사항"))
        val announcement = announcementRepository.save(createAnnouncement(title, contents, author, slackChannel.id))

        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, basicEncodePassword(password))

        When("해당 공지를 조회하면") {
            val response = givenSpec.`when`().get("/api/announcements/${announcement.id}")
                .then().log().all()
                .extract()

            val responseBody = response.`as`(AnnouncementResponse::class.java)

            Then("200 응답과 공지의 세부 내용을 응답한다.") {
                response.statusCode() shouldBe 200
                responseBody shouldBeEqualToComparingFields createAnnouncementResponse(announcement, slackChannel)
            }
        }
    }
})
