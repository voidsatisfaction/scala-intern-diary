package interndiary.web

import interndiary.service.DiaryApp
import org.scalatra._

class DiaryWeb extends DiaryWebStack with AppContextSupport {
  def createApp(userName: String): DiaryApp = {
    new DiaryApp(userName)
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
      "Set-Cookie", s"""userName=; Path=/; Domain=localhost; Expires=Thu, 01 Jan 2000 00:00:00 GMT"""
    )
    // response.setHeader(
    //   "Set-Cookie", s"""userName=deleted; Path=/; Domain=localhost"""
    // )
    redirect("/")
  }

  get("/users/:userName/diaries") {
    implicit val userLoggedIn: Boolean = isLoggedIn
    val userName: String = params("userName")
    val app = createApp(userName)

    (for {
      user <- app.findUser(userName).right
      diaries <- Right(app.listDiary(user)).right
    } yield (user, diaries)) match {
      case Right(result) => interndiary.html.myDiary(result._1, result._2)
      case Left(errorResult) => errorResult
    }
  }
}
