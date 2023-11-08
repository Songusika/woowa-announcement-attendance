package com.woowahan.campus.announcement.feature

import com.ninjasquad.springmockk.MockkBean
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannel
import com.woowahan.campus.announcement.domain.AnnouncementSlackChannelRepository
import com.woowahan.campus.announcement.infrastructure.SlackMessageSender
import com.woowahan.campus.utils.DatabaseCleaner
import com.woowahan.campus.utils.basicEncodePassword
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.CreateAnnouncementRequest
import openapi.model.CreateAnnouncementRequestSlackChannel
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import support.test.beforeRootTest

@Import(DatabaseCleaner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateAnnouncementTest(
    @LocalServerPort
    private val port: Int,
    private val databaseCleaner: DatabaseCleaner,
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
            1L
        )
        every { slackMessageSender.sendMessage(any(), any(), any()) } just Runs
    }

    Given("제목, 내용, 슬랙 채널, 작성자 닉네임, 관리자 비밀번호를 받는다.") {
        val title = "민트는 짱이다."
        val contents = "민트는 짱이다. 이상 전달 끗"
        val slackChannelRequest = CreateAnnouncementRequestSlackChannel(1, "민트 채널")
        val writer = "민트"
        val password = "1234"
        val createAnnouncementRequest = CreateAnnouncementRequest(title, contents, writer, slackChannelRequest)

        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, basicEncodePassword(password))
            .body(createAnnouncementRequest)

        When("작성자, 제목, 내용, 날짜를 저장한다.") {

            val response = givenSpec.`when`().post("/api/announcements")
                .then().log().all()
                .extract()

            Then("201 응답과 저장된 글의 URI를 Location Header로 반환한다.") {
                response.statusCode() shouldBe 201
                response.header(HttpHeaders.LOCATION) shouldNotBe null
            }

            Then("슬랙에 메세지를 요청한다.") {
                verify(exactly = 1) { slackMessageSender.sendMessage(any(), "민트", "민트는 짱이다. 이상 전달 끗") }
            }
        }
    }
})
