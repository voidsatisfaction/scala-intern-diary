package interndiary.service

import interndiary.model.{User, Diary, Article}
import interndiary.repository

class DiaryApp(currentUserName: String) {
  def currentUser(implicit ctx: Context): User = {
    repository.Users.findOrCreateByName(currentUserName)
  }

  def addDiary(title: String)(implicit ctx: Context): Either[Error, Diary] = {
    val user = currentUser
    // QUESTION: どうすれば綺麗にできるのか？
    repository.Diaries.findByUserAndTitle(user, title) match {
      case None => Right(repository.Diaries.create(user, title))
      case _ => Left(DiaryAlreadyExistsError)
    }
  }

  def listDiary()(implicit ctx: Context): List[Diary] = {
    val user = currentUser

    repository.Diaries.listAll(user).toList
  }

  def addArticle(diaryTitle: String, title: String, body: String)(implicit ctx: Context): Either[Error, Article] = {
    val user = currentUser

    // QUESTION 以下ができる綺麗なコードは？
    // すでに作成された同じダイアリーの同じタイトルの記事があるか確認
    // ある場合、ArticleAlreadyExistsErrorエラーを返す
    // なかったら、記事を作成
    repository.Diaries.findByUserAndTitle(user, diaryTitle) match {
      case Some(diary) =>
        repository.Articles.findByDiaryAndTitle(diary, title) match {
          case None => Right(repository.Articles.create(diary, title, body))
          case _ => Left(ArticleAlreadyExistsError)
        }
      case None => Left(DiaryNotFoundError)
    }
  }

  def listArticle(diaryTitle: String)(implicit ctx: Context): Either[Error, List[Article]] = {
    val user = currentUser

    repository.Diaries.findByUserAndTitle(user, diaryTitle) match {
      case Some(diary) =>
        Right(repository.Articles.listAll(diary).toList)
      case None => Left(DiaryNotFoundError)
    }
  }

  def deleteArticle(diaryTitle: String, title: String)(implicit ctx: Context): Either[Error, Unit] = {
    val user = currentUser

    for {
      diary <- repository.Diaries.findByUserAndTitle(user, diaryTitle).toRight(DiaryNotFoundError).right
      article <- repository.Articles.findByDiaryAndTitle(diary, title).toRight(ArticleNotFoundError).right
    } yield repository.Articles.delete(article)
  }
}
