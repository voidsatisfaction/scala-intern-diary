package interndiary.web

import interndiary.service.DiaryApp
import org.scalatra._

class DiaryWeb extends DiaryWebStack with AppContextSupport {
  final val guestUserName: String = "interndiary_guest"
  def createApp(): DiaryApp = {
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
      case None => false
    }
  }

  def checkLoginOrRedirect(): Unit = {
    if(!isLoggedIn()) {
      redirect("/login")
    }
  }

  // QUESTION: DiaryWebというclassの中で, getは関数？
  // 正体はなんだ？
  // どうやってroutingの役割を果たしているのだ？
  get("/") {
    // QUSTION: 同じロジックが重複している(userLoggedIn, currentUserName등등)どうしたら、より綺麗にまとめられるのか？
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest

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

    app.findOrCreateUser(userName)

    response.setHeader(
      "Set-Cookie", s"""userName=${userName}; Path=/; Expires=Thu, 01 Jan 2019 00:00:00 GMT"""
    )
    redirect("/")
  }

  post("/logout") {
    response.setHeader(
      "Set-Cookie", s"""userName=; Path=/; Expires=Thu, 01 Jan 2000 00:00:00 GMT"""
    )
    redirect("/")
  }

  get("/users/:userName/diaries") {
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest
    val userName: String = params("userName")
    val app = createApp()

    app.findUser(userName).toRight(BadRequest()) match {
      case Right(user) => {
        val diaries = app.listDiary(user)
        interndiary.html.myDiary(user, diaries)
      }
      case Left(errorResult) => errorResult
    }
  }

  get("/diaries/new") {
    checkLoginOrRedirect
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest

    interndiary.html.newDiary()
  }

  post("/diaries") {
    checkLoginOrRedirect
    val userName: String = getCurrentUserNameWithGuest
    val diaryTitle: String = params("diary_title")

    val app = createApp()

    app.createDiary(diaryTitle) match {
      case Right(_) => Found(s"/users/${userName}/diaries")
      case Left(errorResult) => errorResult
    }
  }

  get("/users/:userName/diaries/:diaryTitle/articles") {
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest
    val userName: String = params("userName")
    val diaryTitle: String = params("diaryTitle")

    val page: Int = params.getAsOrElse[Int]("page", 1)
    val limit: Int = 5
    val offset: Int = (page - 1) * limit
    val app = createApp()

    app.findUser(userName).toRight(BadRequest()) match {
      case Right(user) => {
        val articles = app.listArticle(user, diaryTitle, limit, offset)
        interndiary.html.articles(diaryTitle, articles, page)
      }
      case Left(errorResult) => errorResult
    }
  }

  get("/articles/new") {
    checkLoginOrRedirect
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest

    val app = createApp()

    app.findUser(getCurrentUserNameWithGuest) match {
      case Some(user) => interndiary.html.newArticle(app.listDiary(user))
      case None => checkLoginOrRedirect
    }
  }

  post("/articles") {
    checkLoginOrRedirect
    val diaryTitle: String = params("diary_title")
    val title: String = params("title")
    val body: String = params("body")

    val app = createApp()
    val userName = getCurrentUserNameWithGuest

    app.createArticle(diaryTitle, title, body) match {
      case Right(_) => Found(s"/users/${userName}/diaries/${diaryTitle}/articles")
      case Left(errorResult) => errorResult
    }
  }

  delete("/articles/:articleId") {
    checkLoginOrRedirect
    val articleId: Long = params.getAs[Long]("articleId").getOrElse(redirect("/"))

    val app = createApp()
    app.deleteArticle(articleId)
  }
}
