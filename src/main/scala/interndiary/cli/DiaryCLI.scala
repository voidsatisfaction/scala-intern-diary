package interndiary.cli

import scala.sys.process
import interndiary.model.{Article}
import interndiary.service.{DiaryApp, Context}

object DiaryCLI {
  def main(args: Array[String]): Unit = {
    val exitStatus = run(args)
    sys.exit(exitStatus)
  }

  private def createApp(userName: String): DiaryApp = new DiaryApp(userName)

  def run(args: Array[String]): Int = {
    try {
      printWhiteSpace

      Context.setup("db.default")
      implicit val ctx = Context.createContext()
      args.toList match {
        case userName :: "addDiary" :: diaryTitle :: _ =>
          val app = createApp(userName)
          addDiary(app, diaryTitle)
        case userName:: "listDiary" :: _ =>
          val app = createApp(userName)
          listDiary(app)
        case userName :: "addArticle" :: diaryTitle :: articleTitle :: articleBody :: _ =>
          val app = createApp(userName)
          addArticle(app, diaryTitle, articleTitle, articleBody)
        case userName :: "listArticle" :: diaryTitle :: _ =>
          val app = createApp(userName)
          listArticle(app, diaryTitle)
        case userName :: "deleteArticle" :: diaryTitle :: articleTitle :: _ =>
          val app = createApp(userName)
          deleteArticle(app, diaryTitle, articleTitle)
        case _ =>
          help()
      }
    } finally Context.destroy
  }

  def addDiary(app: DiaryApp, title: String)(implicit ctx: Context): Int = {
    app.addDiary(title) match {
      case Right(diary) =>
        println(s"diary ${diary.title} is added")
        0
      case Left(error) =>
        process.stderr.println(error.toString())
        1
    }
  }

  def listDiary(app: DiaryApp)(implicit ctx: Context): Int = {
    println(s"--- ${app.currentUser.name}'s Diary ---")
    app.listDiary(app.currentUser).zipWithIndex.foreach { case(diary, i:Int) =>
      println(s"$i: ${diary.title}")
    }
    0
  }

  def addArticle(
    app: DiaryApp,
    diaryTitle: String,
    articleTitle: String,
    articleBody: String
  )(implicit ctx: Context): Int = {
    app.addArticle(diaryTitle, articleTitle, articleBody) match {
      case Right(article) =>
        printArticle(article, "verbose")
        0
      case Left(error) =>
        process.stderr.println(error.toString())
        1
    }
  }

  def listArticle(
    app: DiaryApp,
    diaryTitle: String
  )(implicit ctx: Context): Int = {
    app.listArticle(app.currentUser, diaryTitle, 10, 0) match {
      case Right(articles) =>
        articles.zipWithIndex.foreach { case(article, i) =>
          println(s"-------$i-------")
          printArticle(article, "verbose")
        }
        0
      case Left(error) =>
        process.stderr.println(error.toString())
        1
    }
  }

  def deleteArticle(
    app: DiaryApp,
    diaryTitle: String,
    articleTitle: String
  )(implicit ctx: Context): Int = {
    app.deleteArticleByDiaryTitleAndTitle(diaryTitle, articleTitle) match {
      case Right(_) =>
        println(s"article $articleTitle has been deleted")
        0
      case Left(error) =>
        process.stderr.println(error.toString())
        1
    }
  }

  def help(): Int = {
    process.stderr.println(
      """
        | usage:
        |   run {username} addDiary {title}
        |   run {username} listDiary
        |   run {username} addArticle {diaryTitle} {title} {body}
        |   run {username} listArticle {diaryTitle}
        |   run {username} deleteArticle {diaryTitle} {articleTitle}
      """.stripMargin)
    1
  }

  private def printWhiteSpace(): Unit = {
    println("\n\n")
  }

  private def printArticle(article: Article, option: String): Unit = {
    option match {
      case "verbose" =>
        println("\n\n")
        println("--------title-------")
        println(article.title)
        println("--------body-------")
        println(article.body)
        println("--------created-------")
        println(s"@${article.created}")
        println("--------last updated-------")
        println(s"@${article.updated}")
        println("\n\n")
      case "title" =>
        println("--------title-------")
        println("\n\n")
        println(article.title)
        println("\n\n")
      case _ =>
        println("--------title-------")
        println("\n\n")
        println(article.title)
        println("--------body-------")
        println(article.body)
        println("\n\n")
    }
  }
}
