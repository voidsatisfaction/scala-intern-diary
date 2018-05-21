package interndiary.web

import interndiary.service.DiaryApp
import org.scalatra._

class DiaryWeb extends DiaryWebStack {
  def createApp(): DiaryApp = {
    new DiaryApp("hello")
  }

  def getUser(): Option[String] = {
    request.cookies.get("userName")
  }

  def isLoggedIn(): Boolean = {
    request.cookies.get("userName") match {
      case Some(name) => true
      case _ => false
    }
  }

  def checkLoginOrRedirect(): Unit = {
    println(request.cookies)
    if(!isLoggedIn()) {
      redirect("/login")
    }
    return
  }

  // QUESTION: DiaryWebというclassの中で, getは関数？正体はなんだ？
  get("/") {
    // QUSTION: 同じロジックが重複している(userLoggedIn)どうしたら、より綺麗にまとめられるのか？
    implicit val userLoggedIn: Boolean = isLoggedIn
    println(request.cookies)
    val app = createApp()
    interndiary.html.index("Write your best diary, please!")
  }

  get("/login") {
    implicit val userLoggedIn: Boolean = isLoggedIn
    interndiary.html.login()
  }

  post("/login") {
    val userName: String = params("user_name")
    response.setHeader(
      "Set-Cookie", s"""userName=${userName}; Path=/; Domain=localhost; Expires=Thu, 01 Jan 2019 00:00:00 GMT"""
    )
    redirect("/")
  }

  post("/logout") {
    response.setHeader(
      "Set-Cookie", s"""userName=deleted; Path=/; Domain=localhost; Expires=Thu, 01 Jan 2020 00:00:00 GMT"""
    )
    // response.setHeader(
    //   "Set-Cookie", s"""userName=deleted; Path=/; Domain=localhost"""
    // )
    redirect("/")
  }
}
