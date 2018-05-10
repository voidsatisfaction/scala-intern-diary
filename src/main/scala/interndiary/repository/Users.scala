package interndiary.repository

import interndiary.model.User
import scala.concurrent.ExecutionContext.Implicits.global
import org.joda.time.LocalDateTime
import slick.driver.MySQLDriver.api._
import slick.jdbc.GetResult
import com.github.tototoshi.slick.MySQLJodaSupport._

object Users {
  private implicit val getUserRowResult = GetResult(r => User(r.<<, r.<<, r.<<))
  def create(name: String)(implicit ctx: Context): User = {
    /* QUESTION: なんで、|　が使えないでしょうか。使ったらエラー */
    val userId: Long = Identifier.generate
    val user = User(userId, name, new LocalDateTime())
    run(sqlu"""
    INSERT INTO user (user_id, name, created)
    VALUES (
      ${user.userId},
      ${user.name},
      ${user.created}
    )
    """)
    user
  }

  def findByName(name: String)(implicit ctx: Context): Option[User] = {
    run(sql"""
    SELECT * FROM user
    WHERE name = $name
    LIMIT 1
    """.as[User].map(_.headOption))
  }

  def findOrCreateByName(name: String)(implicit ctx: Context): User = {
    findByName(name).getOrElse(create(name))
  }
}
