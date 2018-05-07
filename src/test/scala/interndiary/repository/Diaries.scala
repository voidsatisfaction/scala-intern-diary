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

    describe("listAll") {
      it("should list all of diaries") {
        val user = dummyUser
        val diary1 = dummyDiary(user)
        val diary2 = dummyDiary(user)
        val diary3 = dummyDiary(user)
        val diaryIds = List(diary1, diary2, diary3).map(_.diaryId)

        val listedDiaries = Diaries.listAll(user)
        val listedDiaryIds = listedDiaries.map(_.diaryId)

        listedDiaryIds.length shouldBe 3
        listedDiaryIds shouldBe diaryIds
      }
    }
  }
}
