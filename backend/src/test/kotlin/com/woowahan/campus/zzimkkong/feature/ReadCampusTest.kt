package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import openapi.model.MapGetSingle
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReadCampusTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({

    RestAssured.port = port

    Given("캠퍼스 정보를 등록한다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val givenSpec = CampusFixture.`캠퍼스_생성`(campus, slackUrl)

        When("캠퍼스 정보를 모두 조회한다.") {
            val response = CampusFixture.캠퍼스_전체_조회()

            Then("200 응답과 저장된 캠퍼스 정보들을 반환한다.") {
                assertSoftly {
                    response.statusCode() shouldBe 200
                    response.body().jsonPath().getList(".", MapGetSingle::class.java) shouldContain
                        MapGetSingle(
                            mapId = givenSpec.header(HttpHeaders.LOCATION).shouldNotBeNull().split("/").last()
                                .toInt(),
                            mapName = campus.name,
                            mapDrawing = campus.drawing,
                            thumbnail = campus.thumbnail,
                            slackUrl = slackUrl
                        )
                }
            }
        }

        When("캠퍼스 단건 정보를 조회한다.") {
            val mapId = givenSpec.header(HttpHeaders.LOCATION).shouldNotBeNull().split("/").last()
            val response = CampusFixture.캠퍼스_단건_조회(mapId)

            Then("200 응답과 저장된 캠퍼스 정보들을 반환한다.") {
                assertSoftly {
                    response.statusCode() shouldBe 200
                    val mapGetSingle = response.body().jsonPath().getObject(".", MapGetSingle::class.java)
                    mapGetSingle.mapId shouldBe mapId.toInt()
                    mapGetSingle.mapName shouldBe campus.name
                    mapGetSingle.mapDrawing shouldBe campus.drawing
                    mapGetSingle.thumbnail shouldBe campus.thumbnail
                    mapGetSingle.slackUrl shouldBe slackUrl
                }
            }
        }
    }
})
