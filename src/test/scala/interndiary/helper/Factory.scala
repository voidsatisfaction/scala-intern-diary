package interndiary.helper

import interndiary.model._
import interndiary.repository._
import scala.util.Random.alphanumeric

trait Factory {
  def randomString(length: Int): String =
    alphanumeric.take(length).mkString

  def dummyUser()(implicit ctx: Context): User = {
    Users.create(randomString(15))
  }

  def dummyDiary(user: User)(implicit ctx: Context): Diary = {
    Diaries.create(user, randomString(25))
  }

  def dummyArticle(diary: Diary)(implicit ctx: Context): Article = {
    Articles.create(diary, randomString(20), randomString(150))
  }
}
