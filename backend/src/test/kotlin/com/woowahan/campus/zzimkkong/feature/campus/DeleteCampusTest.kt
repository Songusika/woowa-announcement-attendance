package com.woowahan.campus.zzimkkong.feature.campus

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteCampusTest(
    @LocalServerPort
    val port: Int,
    val campusRepository: CampusRepository,
) : BehaviorSpec({

    RestAssured.port = port

    Given("캠퍼스 정보를 등록한다.") {
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())

        When("캠퍼스 정보를 삭제한다.") {
            val response = RestAssured
                .given().log().all()
                .`when`().delete("/api/maps/${campus.id}")
                .then().log().all()
                .extract()

            Then("204 응답을 반환한다.") {
                response.statusCode() shouldBe 204
            }

            Then("조회 시 캠퍼스 정보가 존재하지 않는다.") {
                campusRepository.findById(campus.id) shouldBe null
            }
        }
    }
})
