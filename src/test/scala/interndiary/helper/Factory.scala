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
}
