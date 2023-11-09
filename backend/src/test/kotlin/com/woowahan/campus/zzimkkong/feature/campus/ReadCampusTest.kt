package com.woowahan.campus.zzimkkong.feature.campus

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannel
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.support.DatabaseInitializer
import com.woowahan.campus.zzimkkong.support.asPrettyJson
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReadCampusTest(
    @LocalServerPort
    val port: Int,
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
    val databaseInitializer: DatabaseInitializer
) : BehaviorSpec({

    extensions(databaseInitializer)

    RestAssured.port = port

    Given("캠퍼스 정보를 등록한다.") {
        val slackUrl = "https://slackexample.com"
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        slackChannelRepository.save(SlackChannel(slackUrl, campus.id))

        When("캠퍼스 정보를 모두 조회한다.") {
            val response = RestAssured
                .given().log().all()
                .`when`().get("/api/maps")
                .then().log().all()
                .extract()

            Then("200 응답과 저장된 캠퍼스 정보들을 반환한다.") {
                assertSoftly {
                    response.statusCode() shouldBe 200
                    response.asPrettyJson() shouldBe
                        """
                            [
                                {
                                    "mapId": ${campus.id},
                                    "mapName": "${campus.name}",
                                    "mapDrawing": "${campus.drawing}",
                                    "thumbnail": "${campus.thumbnail}",
                                    "slackUrl": "$slackUrl"
                                }
                            ]
                        """.trimIndent()
                }
            }
        }

        When("캠퍼스 단건 정보를 조회한다.") {
            val response = RestAssured
                .given().log().all()
                .`when`().get("/api/maps/${campus.id}")
                .then().log().all()
                .extract()

            Then("200 응답과 저장된 캠퍼스 정보들을 반환한다.") {
                assertSoftly {
                    response.statusCode() shouldBe 200
                    response.asPrettyJson() shouldBe
                        """
                            {
                                "mapId": ${campus.id},
                                "mapName": "${campus.name}",
                                "mapDrawing": "${campus.drawing}",
                                "thumbnail": "${campus.thumbnail}",
                                "slackUrl": "$slackUrl"
                            }
                        """.trimIndent()
                }
            }
        }
    }
})
