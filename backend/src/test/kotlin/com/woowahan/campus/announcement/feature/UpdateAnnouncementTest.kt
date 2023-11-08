package com.woowahan.campus.announcement.feature

import com.ninjasquad.springmockk.MockkBean
import com.woowahan.campus.announcement.domain.AnnouncementRepository
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannel
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannelRepository
import com.woowahan.campus.announcement.domain.Author
import com.woowahan.campus.announcement.domain.Content
import com.woowahan.campus.announcement.infrastructure.SlackMessageSender
import com.woowahan.campus.fixture.createAnnouncementRequest
import com.woowahan.campus.fixture.createUpdateAnnouncementRequest
import com.woowahan.campus.utils.DatabaseCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.CreateAnnouncementRequest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import support.test.beforeRootTest
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateAnnouncementTest(
    @LocalServerPort
    val port: Int,
    private val databaseCleaner: DatabaseCleaner,
    private val announcementRepository: AnnouncementRepository,
    @MockkBean
    private val slackMessageSender: SlackMessageSender,
    @MockkBean
    private val announcementSlackChannelRepository: AnnouncementSlackChannelRepository,
) : BehaviorSpec({

    RestAssured.port = port

    beforeRootTest {
        databaseCleaner.clean()
        every { announcementSlackChannelRepository.findById(any()) } returns AnnouncementSlackChannel(
            "providerId",
            "channelName",
            1L,
        )
        every { slackMessageSender.sendMessage(any(), any(), any()) } just Runs
    }

    Given("글ID, 제목, 내용, 작성자, 관리자 비밀번호를 받는다.") {
        val password = "1234".toByteArray()
        val savedAnnouncementId = createAnnouncement(password, createAnnouncementRequest(author = "author"))

        val updateAnnouncementRequest = createUpdateAnnouncementRequest(author = "author")
        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Basic ${String(Base64.getEncoder().encode(password))}")
            .body(updateAnnouncementRequest)

        When("작성자, 제목, 내용, 날짜를 수정한다.") {
            val response = givenSpec.`when`().put("/api/announcements/{id}", savedAnnouncementId)
                .then().log().all()
                .extract()

            val updatedAnnouncement = announcementRepository.findById(savedAnnouncementId)!!

            Then("200 응답과 저장된 글의 ID를 반환한다.") {
                response.statusCode() shouldBe 200
                updatedAnnouncement.title.title shouldBe "updateTitle"
            }

            Then("슬랙에 수정을 요청한다.") {
                verify(exactly = 1) {
                    slackMessageSender.sendMessage(
                        any(),
                        Author("author"),
                        Content("updateContent"),
                    )
                }
            }
        }
    }

    Given("글ID, 제목, 내용, 실제 작성자와 다른 작성자, 관리자 비밀번호를 받는다.") {
        val password = "1234".toByteArray()
        val savedAnnouncementId = createAnnouncement(password, createAnnouncementRequest(author = "author"))

        val updateAnnouncementRequest = createUpdateAnnouncementRequest(author = "differentAuthor")
        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Basic ${String(Base64.getEncoder().encode(password))}")
            .body(updateAnnouncementRequest)

        When("잘못된 내용 요청을 확인했다.") {
            val response = givenSpec.`when`().put("/api/announcements/{id}", savedAnnouncementId)
                .then().log().all()
                .extract()

            Then("400 상태 코드를 반환한다.") {
                response.statusCode() shouldBe 401
            }
        }
    }
})

private fun createAnnouncement(
    password: ByteArray,
    createAnnouncementRequest: CreateAnnouncementRequest,
) = RestAssured
    .given().log().all()
    .contentType(ContentType.JSON)
    .header(HttpHeaders.AUTHORIZATION, "Basic ${String(Base64.getEncoder().encode(password))}")
    .body(createAnnouncementRequest)
    .`when`().post("/api/announcements")
    .then().log().all()
    .extract()
    .header(HttpHeaders.LOCATION)
    .split("/").last().toLong()
