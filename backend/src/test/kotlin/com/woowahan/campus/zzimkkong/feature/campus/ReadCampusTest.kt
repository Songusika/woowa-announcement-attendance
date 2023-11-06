package com.woowahan.campus.zzimkkong.feature.campus

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.support.DatabaseInitializer
import com.woowahan.campus.zzimkkong.support.ResponseUtils
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
    val databaseInitializer: DatabaseInitializer
) : BehaviorSpec({

    extensions(databaseInitializer)

    RestAssured.port = port

    Given("캠퍼스 정보를 등록한다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val mapId = CampusFixture.캠퍼스_생성_ID_반환(campus, slackUrl)

        When("캠퍼스 정보를 모두 조회한다.") {
            val response = CampusFixture.캠퍼스_전체_조회()

            Then("200 응답과 저장된 캠퍼스 정보들을 반환한다.") {
                assertSoftly {
                    response.statusCode() shouldBe 200
                    ResponseUtils.getPrettyJson(response) shouldBe
                        """
                            [
                                {
                                    "mapId": $mapId,
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
            val response = CampusFixture.캠퍼스_단건_조회(mapId.toString())

            Then("200 응답과 저장된 캠퍼스 정보들을 반환한다.") {
                assertSoftly {
                    response.statusCode() shouldBe 200
                    ResponseUtils.getPrettyJson(response) shouldBe
                        """
                            {
                                "mapId": $mapId,
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
