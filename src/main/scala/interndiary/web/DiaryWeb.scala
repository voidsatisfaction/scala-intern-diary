package interndiary.web

import interndiary.service.DiaryApp
import org.scalatra._

class DiaryWeb extends DiaryWebStack {
  def createApp(): DiaryApp = {
  }

  def isLoggedIn(): Boolean = {
    request.cookies.get("userName") match {
      case Some(name) => true
      case _ => false
    }
  }

  // QUESTION: DiaryWebというclassの中で, getは関数？正体はなんだ？
  get("/") {
    // QUSTION: 同じロジックが重複している(userLoggedIn)
    implicit val userLoggedIn: Boolean = isLoggedIn
    val app = createApp()
    println(isLoggedIn)
    interndiary.html.index("Write your best diary, please!")
  }

  get("/login") {
    implicit val userLoggedIn: Boolean = isLoggedIn
    interndiary.html.login()
  }

  post("/login") {
    val userName: String = params("user_name")
    response.setHeader(
      "Set-Cookie", s"""userName=${userName}"""
    )
    redirect("/")
  }

  post("/logout") {

  }
}
