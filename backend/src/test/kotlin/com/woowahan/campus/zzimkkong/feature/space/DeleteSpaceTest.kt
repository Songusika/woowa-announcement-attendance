package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SpaceRepository
import com.woowahan.campus.zzimkkong.domain.getById
import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SettingFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import openapi.model.SpaceDelete
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteSpaceTest(
    @LocalServerPort
    val port: Int,
    val campusRepository: CampusRepository,
    val spaceRepository: SpaceRepository,
) : BehaviorSpec({
    RestAssured.port = port

    Given("회의실 정보를 등록한다.") {
        val slackUrl = "https://slackexample.com"
        val campus = campusRepository.save(CampusFixture.잠실_캠퍼스())
        val settings = listOf(SettingFixture.회의실_예약_설정_1(), SettingFixture.회의실_예약_설정_2())
        val space = spaceRepository.save(SpaceFixture.랜딩_강의장(campus.id, settings))
        val newThumbnail = "newThumbnail"

        When("회의실 정보를 삭제한다") {
            val response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(SpaceDelete(newThumbnail))
                .`when`().delete("/api/maps/${campus.id}/spaces/${space.id}")
                .then().log().all()
                .extract()

            Then("204 응답을 반환한다") {
                response.statusCode() shouldBe 204
            }

            Then("회의실을 조회시 존재하지 않는다.") {
                spaceRepository.findById(space.id) shouldBe null
            }

            Then("캠퍼스의 썸네일이 변경된다") {
                val campusResponse = campusRepository.getById(campus.id)
                campusResponse.thumbnail shouldBe newThumbnail
            }
        }
    }
})
