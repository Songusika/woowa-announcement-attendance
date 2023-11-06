package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SettingFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteSpaceTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({
    RestAssured.port = port

    Given("회의실 정보를 등록한다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val campusId = CampusFixture.캠퍼스_생성_ID_반환(campus, slackUrl)
        val settings = listOf(SettingFixture.회의실_예약_설정_1(), SettingFixture.회의실_예약_설정_2())
        val space = SpaceFixture.랜딩_강의장(campusId, settings)
        val thumbnail = "thumbnail"
        val spaceId = SpaceFixture.회의실_생성_ID_반환(space, thumbnail, settings)
        val newThumbnail = "newThumbnail"

        When("회의실 정보를 삭제한다") {
            val response = SpaceFixture.회의실_단건_삭제(
                campusId.toString(),
                spaceId.toString(),
                newThumbnail
            )

            Then("204 응답을 반환한다") {
                response.statusCode() shouldBe 204
            }

            Then("회의실을 조회시 200 응답을 반환하지 않는다.") {
                SpaceFixture.회의실_단건_조회(campusId.toString(), spaceId.toString())
                    .statusCode() shouldNotBe 200
            }

            Then("캠퍼스의 썸네일이 변경된다") {
                val campusResponse = CampusFixture.캠퍼스_단건_조회(campusId.toString())
                campusResponse.statusCode() shouldBe 200
                campusResponse.body().jsonPath().getString("thumbnail") shouldBe newThumbnail
            }
        }
    }
})
