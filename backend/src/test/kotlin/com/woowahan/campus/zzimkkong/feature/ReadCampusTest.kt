package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.MapGetSingle
import openapi.model.MapPost
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
        val request = MapPost(campus.name, campus.drawing, campus.thumbnail, slackUrl)

        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`().post("/api/maps")
            .then().log().all()
            .extract()

        When("캠퍼스 정보를 모두 조회한다.") {
            val response = RestAssured
                .given().log().all()
                .`when`().get("/api/maps")
                .then().log().all()
                .extract()

            Then("200 응답과 저장된 캠퍼스 정보들을 반환한다.") {
                assertSoftly {
                    response.statusCode() shouldBe 200
                    response.body().jsonPath().getList<MapGetSingle>(".", MapGetSingle::class.java) shouldContain
                        MapGetSingle(
                            mapId = givenSpec.header(HttpHeaders.LOCATION).shouldNotBeNull().split("/").last().toInt(),
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
            val response = RestAssured
                .given().log().all()
                .`when`().get("/api/maps/$mapId")
                .then().log().all()
                .extract()

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
