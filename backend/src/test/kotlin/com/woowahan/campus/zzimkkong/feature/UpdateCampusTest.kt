package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.MapGetSingle
import openapi.model.MapPost
import openapi.model.MapPut
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

        val givenSpec = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(MapPost(campus.name, campus.drawing, campus.thumbnail, slackUrl))
            .`when`().post("/api/maps")
            .then().log().all()
            .extract()

        val request = MapPut("뉴 잠실 캠퍼스", "drawing", "thumbnail", "slackUrl")

        When("캠퍼스 정보를 수정한다.") {
            val mapId = givenSpec.header(HttpHeaders.LOCATION).shouldNotBeNull().split("/").last()
            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .`when`().put("/api/maps/$mapId")
                .then().log().all()
                .extract()

            Then("200 응답을 반환한다.") {
                response.statusCode() shouldBe 200
            }

            Then("수정된 캠퍼스 정보를 확인한다.") {
                val mapGetSingle = RestAssured
                    .given().log().all()
                    .`when`().get("/api/maps/$mapId")
                    .then().log().all()
                    .extract()
                    .body().jsonPath().getObject(".", MapGetSingle::class.java)

                assertSoftly {
                    mapGetSingle.mapId shouldBe mapId.toInt()
                    mapGetSingle.mapName shouldBe request.mapName
                    mapGetSingle.mapDrawing shouldBe request.mapDrawing
                    mapGetSingle.thumbnail shouldBe request.thumbnail
                    mapGetSingle.slackUrl shouldBe request.slackUrl
                }
            }
        }
    }
})
