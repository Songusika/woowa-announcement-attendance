package com.woowahan.campus.zzimkkong.feature.space

import com.woowahan.campus.zzimkkong.fixture.CampusFixture
import com.woowahan.campus.zzimkkong.fixture.SettingFixture
import com.woowahan.campus.zzimkkong.fixture.SpaceFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateSpaceTest(
    @LocalServerPort
    val port: Int
) : BehaviorSpec({

    RestAssured.port = port

    Given("이름, 색상, 공간 좌표 정보, 예약 가능 여부, 예약 설정을 받는다.") {
        val campus = CampusFixture.잠실_캠퍼스()
        val slackUrl = "https://slackexample.com"
        val campusId = CampusFixture.캠퍼스_생성_ID_반환(campus, slackUrl)
        val settings = listOf(SettingFixture.회의실_예약_설정_1())
        val space = SpaceFixture.랜딩_강의장(campusId, settings)
        val thumbnail = "thumbnail"

        When("공간 정보를 저장한다.") {
            val response = SpaceFixture.회의실_생성(space, thumbnail, settings)

            Then("201 응답과 저장된 공간의 URI를 Location Header로 반환한다.") {
                response.statusCode() shouldBe 201
                response.header(HttpHeaders.LOCATION).shouldNotBeNull()
            }

            Then("캠퍼스의 썸네일 정보가 업데이트된다.") {
                val campusResponse = CampusFixture.캠퍼스_단건_조회(campusId.toString())
                campusResponse.statusCode() shouldBe 200
                campusResponse.body().jsonPath().getString("thumbnail") shouldBe thumbnail
            }
        }
    }
})
