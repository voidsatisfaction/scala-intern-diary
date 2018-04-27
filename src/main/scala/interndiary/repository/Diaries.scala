package interndiary.repository

import interndiary.model.{Diary, User}
import scala.concurrent.ExecutionContext.Implicits.global
import org.joda.time.LocalDateTime
import slick.driver.MySQLDriver.api._
import slick.jdbc.GetResult
import com.github.tototoshi.slick.MySQLJodaSupport._

object Diaries {
  private implicit val getDiaryRowResult = GetResult(r => Diary(r.<<, r.<<, r.<<, r.<<))
  def create(user: User, title: String)(implicit ctx: Context): Diary = {
    val id: Long = Identifier.generate
    val diary = Diary(id, title, user.userId, new LocalDateTime())
    run(sqlu"""
      INSERT INTO diary
        (diary_id, title, user_id, created)
      VALUES
        (
          ${diary.diaryId},
          ${diary.title},
          ${diary.userId},
          ${diary.created}
        )
    """)
    diary
  }

  def findByUserAndTitle(user: User, title: String)(implicit ctx: Context): Option[Diary] = {
    val userId: Long = user.userId
    run(sql"""
      SELECT * FROM diary
      WHERE
        user_id = $userId AND
        title = $title
    """.as[Diary].map(_.headOption))
  }
}
