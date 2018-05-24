package interndiary.web

import interndiary.service.DiaryApp
import org.scalatra._

class DiaryWeb extends DiaryWebStack with AppContextSupport {
  final val guestUserName: String = "interndiary_guest"
  def createApp(): DiaryApp = {
    // not looks so good?
    new DiaryApp(getCurrentUserNameWithGuest)
  }

  def getCurrentUserName(): Option[String] = {
    request.cookies.get("userName")
  }

  def getCurrentUserNameWithGuest(): String = {
    getCurrentUserName.getOrElse(guestUserName)
  }

  def isLoggedIn(): Boolean = {
    getCurrentUserName match {
      case Some(name) => true
      case _ => false
    }
  }

  def checkLoginOrRedirect(): Unit = {
    if(!isLoggedIn()) {
      redirect("/login")
    }
  }

  // QUESTION: DiaryWebというclassの中で, getは関数？正体はなんだ？
  get("/") {
    // QUSTION: 同じロジックが重複している(userLoggedIn)どうしたら、より綺麗にまとめられるのか？
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest
    println(request.cookies)
    interndiary.html.index("Write your best diary, please!")
  }

  get("/login") {
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest
    interndiary.html.login()
  }

  post("/login") {
    val userName: String = params("user_name")
    val app = createApp()

    app.findOrAddUser(userName)

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
    implicit val currentUserName: String = getCurrentUserNameWithGuest
    val userName: String = params("userName")
    val app = createApp()

    (for {
      user <- app.findUser(userName).right
      diaries <- Right(app.listDiary(user)).right
    } yield (user, diaries)) match {
      case Right(result) => interndiary.html.myDiary(result._1, result._2)
      case Left(errorResult) => errorResult
    }
  }

  get("/diaries/new") {
    checkLoginOrRedirect
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest
  }

  // TODO: diary articles
  // get("/users/:userName/diaries/:diaryName") {
  //   implicit val userLoggedIn: Boolean = isLoggedIn
  //   val userName: String = params("userName")
  //   val diaryName: String = params("diaryName")
  //   val app = createApp()
  // }
}
