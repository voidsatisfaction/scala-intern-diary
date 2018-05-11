package interndiary.repository

import interndiary.model.{Diary, Article}
// QUESTION: それぞれのパッケージの役割？
import scala.concurrent.ExecutionContext.Implicits.global
import org.joda.time.LocalDateTime
import slick.driver.MySQLDriver.api._
import slick.jdbc.GetResult
import com.github.tototoshi.slick.MySQLJodaSupport._

object Articles {
  private implicit val getArticleRowResult = GetResult(r => Article(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  def create(diary: Diary, title: String, body: String)(implicit ctx: Context): Article = {
    val id: Long = Identifier.generate
    val article = Article(id, title, body, diary.diaryId, new LocalDateTime(), new LocalDateTime())

    // QUESTION: このrun関数はどこからきたものなのか？
    run(sqlu"""
      INSERT INTO article
        (article_id, title, body, diary_id, created, updated)
      VALUES
        (
          ${article.articleId},
          ${article.title},
          ${article.body},
          ${article.diaryId},
          ${article.created},
          ${article.updated}
        )
    """)

    return article
  }

  def findByDiaryAndTitle(diary: Diary, title: String)(implicit ctx: Context): Option[Article] = {
    val diaryId: Long = diary.diaryId
    run(sql"""
      SELECT * FROM article
        WHERE
          diary_id=$diaryId AND
          title=$title
    """.as[Article].map(_.headOption))
  }

  def listAll(diary: Diary)(implicit ctx: Context): Seq[Article] = {
    val diaryId: Long = diary.diaryId
    val rows = run(sql"""
      SELECT * FROM article
        WHERE
          diary_id=$diaryId
    """.as[Article])
    rows
  }

  def delete(article: Article)(implicit ctx: Context): Unit = {
    val articleId: Long = article.articleId
    val _: Int = run(sqlu"""
      DELETE FROM article
        WHERE
          article_id=$articleId
    """)
  }
}
