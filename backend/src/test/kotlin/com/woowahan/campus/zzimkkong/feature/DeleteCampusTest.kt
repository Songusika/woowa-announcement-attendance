package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteCampusTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({

    RestAssured.port = port

    Given("캠퍼스 정보를 등록한다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val givenSpec = CampusFixture.캠퍼스_생성(campus, slackUrl)

        When("캠퍼스 정보를 삭제한다.") {
            val mapId = givenSpec.header("Location").split("/").last()
            val response = CampusFixture.캠퍼스_단건_삭제(mapId)

            Then("204 응답을 반환한다.") {
                response.statusCode() shouldBe 204
            }

            Then("조회 시 200 응답을 반환하지 않는다.") {
                CampusFixture.캠퍼스_단건_조회(mapId)
                    .statusCode() shouldNotBe 200
            }
        }
    }
})
