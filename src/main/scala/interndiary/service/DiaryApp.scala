package interndiary.service

import interndiary.model.{User, Diary}
import interndiary.repository

class DiaryApp(currentUserName: String) {
  def currentUser(implicit ctx: Context): User = {
    repository.Users.findOrCreateByName(currentUserName)
  }

  def addDiary(title: String)(implicit ctx: Context): Diary = {
    val user = currentUser
    // QUESTION: 実はエラーを返したい。どうすれば綺麗にできるのか？Etherをつかう?Core的に処理したい。
    repository.Diaries.findByUserAndTitle(user, title) match {
      case None => repository.Diaries.create(user, title)
      case _ => repository.Diaries.findByUserAndTitle(user, title).get
    }
  }
}
