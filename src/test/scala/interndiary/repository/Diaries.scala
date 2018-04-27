package interndiary.repository

import interndiary.helper._

class DiaryRepositorySpec extends UnitSpec with SetupDB with Factory {
  describe("User Repository") {
    describe("create") {
      it("should be created with user and title") {
        val user = dummyUser
        val title = randomString(20)

        val createdDiary = Diaries.create(user, title)
        val foundedDiary = Diaries.findByUserAndTitle(user, title).getOrElse(null)

        createdDiary.title shouldBe foundedDiary.title
        createdDiary.userId shouldBe foundedDiary.userId
      }
    }

    describe("findByUserAndTitle") {
      it("should return diary") {
        val user = dummyUser
        val diary = dummyDiary(user)

        val foundedDiary = Diaries.findByUserAndTitle(user, diary.title).getOrElse(null)

        foundedDiary should not be null
        foundedDiary.title shouldBe diary.title
        foundedDiary.userId shouldBe diary.userId
      }
    }
  }
}
