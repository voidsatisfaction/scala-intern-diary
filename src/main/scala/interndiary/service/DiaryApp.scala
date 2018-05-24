package interndiary.service

import interndiary.model.{User, Diary, Article}
import interndiary.repository

class DiaryApp(currentUserName: String) {
  def currentUser(implicit ctx: Context): User = {
    repository.Users.findOrCreateByName(currentUserName)
  }

  def findOrAddUser(userName: String)(implicit ctx: Context): User = {
    repository.Users.findOrCreateByName(userName)
  }

  def findUser(userName: String)(implicit ctx: Context): Either[UserError, User] = {
    repository.Users.findByName(userName) match {
      case Some(user) => Right(user)
      case None => Left(UserNotFound)
    }
  }

  def addDiary(title: String)(implicit ctx: Context): Either[DiaryError, Diary] = {
    val user = currentUser
    repository.Diaries.findByUserAndTitle(user, title) match {
      case None => Right(repository.Diaries.create(user, title))
      case _ => Left(DiaryAlreadyExists)
    }
  }

  def listDiary(user: User)(implicit ctx: Context): List[Diary] = {
    repository.Diaries.listAll(user).toList
  }

  def addArticle(diaryTitle: String, title: String, body: String)(implicit ctx: Context): Either[Error, Article] = {
    val user = currentUser

    repository.Diaries.findByUserAndTitle(user, diaryTitle) match {
      case Some(diary) =>
        repository.Articles.findByDiaryAndTitle(diary, title) match {
          case None => Right(repository.Articles.create(diary, title, body))
          case _ => Left(ArticleAlreadyExists)
        }
      case None => Left(DiaryNotFound)
    }
  }

  def listArticle(user: User, diaryTitle: String)(implicit ctx: Context): Either[Error, List[Article]] = {
    repository.Diaries.findByUserAndTitle(user, diaryTitle) match {
      case Some(diary) =>
        Right(repository.Articles.listAll(diary).toList)
      case None => Left(DiaryNotFound)
    }
  }

  def deleteArticle(diaryTitle: String, title: String)(implicit ctx: Context): Either[Error, Unit] = {
    val user = currentUser

    for {
      diary <- repository.Diaries.findByUserAndTitle(user, diaryTitle).toRight(DiaryNotFound).right
      article <- repository.Articles.findByDiaryAndTitle(diary, title).toRight(ArticleNotFound).right
    } yield repository.Articles.delete(article)
  }
}
