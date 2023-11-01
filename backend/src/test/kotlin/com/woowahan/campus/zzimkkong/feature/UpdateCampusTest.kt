package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.support.ResponseUtils
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateCampusTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({

    RestAssured.port = port

    Given("캠퍼스 정보를 등록한다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val givenSpec = CampusFixture.캠퍼스_생성(campus, slackUrl)
        val campusForUpdate = CampusFixture.선릉_캠퍼스()

        When("캠퍼스 정보를 수정한다.") {
            val mapId = givenSpec.header(HttpHeaders.LOCATION).shouldNotBeNull().split("/").last()
            val response = CampusFixture.캠퍼스_단건_수정(mapId, campusForUpdate, slackUrl)

            Then("200 응답을 반환한다.") {
                response.statusCode() shouldBe 200
            }

            Then("수정된 캠퍼스 정보를 확인한다.") {
                val readCampus = CampusFixture.캠퍼스_단건_조회(mapId)
                assertSoftly {
                    readCampus.statusCode() shouldBe 200
                    ResponseUtils.getPrettyJson(readCampus) shouldBe
                        """
                            {
                                "mapId": $mapId,
                                "mapName": "${campusForUpdate.name}",
                                "mapDrawing": "${campusForUpdate.drawing}",
                                "thumbnail": "${campusForUpdate.thumbnail}",
                                "slackUrl": "$slackUrl"
                            }
                        """.trimIndent()
                }
            }
        }
    }
})
