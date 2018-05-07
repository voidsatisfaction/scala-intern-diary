package interndiary.repository

import interndiary.helper._

class ArticleRepositorySpec extends UnitSpec with SetupDB with Factory {
  describe("Article Repository") {
    describe("create") {
      it("should be created with diary title body") {
        val user = dummyUser
        val diary = dummyDiary(user)
        val title = randomString(20)
        val body = randomString(200)

        val createdArticle = Articles.create(diary, title, body)
        val foundArticle = Articles.findByDiaryAndTitle(diary, title).getOrElse(null)

        createdArticle should not be null
        foundArticle should not be null
        createdArticle.articleId shouldBe foundArticle.articleId
        createdArticle.title shouldBe foundArticle.title
        createdArticle.body shouldBe foundArticle.body
      }
    }

    describe("findByDiaryAndTitle") {
      it("should be found with diary title") {
        val user = dummyUser
        val diary = dummyDiary(user)
        val article = dummyArticle(diary)

        val foundArticle = Articles.findByDiaryAndTitle(diary, article.title).getOrElse(null)

        foundArticle should not be null
        foundArticle.diaryId shouldBe article.diaryId
      }

      it("should not be found with not proper title") {
        val user = dummyUser
        val diary = dummyDiary(user)
        dummyArticle(diary)

        val foundArticle = Articles.findByDiaryAndTitle(diary, randomString(10)).getOrElse(null)

        foundArticle shouldBe null
      }
    }
  }
}
