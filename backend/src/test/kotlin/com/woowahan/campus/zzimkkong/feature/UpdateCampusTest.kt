package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import openapi.model.MapGetSingle
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
                val mapGetSingle = CampusFixture.캠퍼스_단건_조회(mapId)
                    .body().jsonPath().getObject(".", MapGetSingle::class.java)

                assertSoftly {
                    mapGetSingle.mapId shouldBe mapId.toInt()
                    mapGetSingle.mapName shouldBe campusForUpdate.name
                    mapGetSingle.mapDrawing shouldBe campusForUpdate.drawing
                    mapGetSingle.thumbnail shouldBe campusForUpdate.thumbnail
                    mapGetSingle.slackUrl shouldBe slackUrl
                }
            }
        }
    }
})
