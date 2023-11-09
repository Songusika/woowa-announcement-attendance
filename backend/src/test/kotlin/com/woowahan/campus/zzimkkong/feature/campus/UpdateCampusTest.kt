package com.woowahan.campus.zzimkkong.feature.campus

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannel
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import com.woowahan.campus.zzimkkong.domain.getByCampusId
import com.woowahan.campus.zzimkkong.domain.getById
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.MapPut
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateCampusTest(
    @LocalServerPort
    val port: Int,
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : BehaviorSpec({

    RestAssured.port = port

    Given("캠퍼스 정보를 등록한다.") {
        val slackUrl = "https://slackexample.com"
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        slackChannelRepository.save(SlackChannel("https://slackurl.com", campus.id))

        When("캠퍼스 정보를 수정한다.") {
            val campusForUpdate = CampusFixture.선릉_캠퍼스()
            val mapId = campus.id
            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(MapPut(campusForUpdate.name, campusForUpdate.drawing, campusForUpdate.thumbnail, slackUrl))
                .`when`().put("/api/maps/$mapId")
                .then().log().all()
                .extract()

            Then("200 응답을 반환한다.") {
                response.statusCode() shouldBe 200
            }

            Then("수정된 캠퍼스 정보를 확인한다.") {
                val readCampus = campusRepository.getById(mapId)
                val readSlackUrl = slackChannelRepository.getByCampusId(readCampus.id)

                assertSoftly {
                    readCampus.name shouldBe campusForUpdate.name
                    readCampus.drawing shouldBe campusForUpdate.drawing
                    readCampus.thumbnail shouldBe campusForUpdate.thumbnail
                    readSlackUrl.url shouldBe slackUrl
                }
            }
        }
    }
})
