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

    app.findOrAddUser(userName)

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

    interndiary.html.newDiary()
  }

  post("/diaries") {
    checkLoginOrRedirect
    val userName: String = getCurrentUserNameWithGuest
    val diaryTitle: String = params("diary_title")

    val app = createApp()

    app.addDiary(diaryTitle) match {
      case Right(_) => Found(s"/users/${userName}/diaries")
      case Left(errorResult) => errorResult
    }
  }

  get("/users/:userName/diaries/:diaryTitle/articles") {
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest
    val userName: String = params("userName")
    val diaryTitle: String = params("diaryTitle")

    // QUESTION: query parameterとして、abcみたいな文字列がくる場合の処理はどうすれば綺麗にできるのか？
    val page: Int = params.getOrElse("page", "1").toInt
    val limit: Int = 5
    val offset: Int = (page - 1) * limit
    // QUESTION: ページネーションで最後まで一瞬で移動できるようにするためには
    // 全体レコード数を知る必要があると思われるが、それは毎回軽さんっして表示する感じなのか？
    val app = createApp()

    (for {
      user <- app.findUser(userName).right
      articles <- app.listArticle(user, diaryTitle, limit, offset).right
    } yield articles) match {
      case Right(articles) => interndiary.html.articles(diaryTitle, articles, page)
      case Left(errorResult) => errorResult
    }
  }

  get("/articles/new") {
    checkLoginOrRedirect
    implicit val userLoggedIn: Boolean = isLoggedIn
    implicit val currentUserName: String = getCurrentUserNameWithGuest

    val app = createApp()

    app.findUser(getCurrentUserNameWithGuest) match {
      case Right(user) => interndiary.html.newArticle(app.listDiary(user))
      case Left(errorResult) => errorResult
    }
  }

  post("/articles") {
    checkLoginOrRedirect
    val diaryTitle: String = params("diary_title")
    val title: String = params("title")
    val body: String = params("body")

    val app = createApp()
    val userName = getCurrentUserNameWithGuest

    app.addArticle(diaryTitle, title, body) match {
      case Right(_) => Found(s"/users/${userName}/diaries/${diaryTitle}/articles")
      case Left(errorResult) => errorResult
    }
  }

  delete("/articles/:articleId") {
    checkLoginOrRedirect
    // QUESTION: もし、articleIdにabcみたいな文字列を入れると？
    val articleId: Long = params("articleId").toLong

    val app = createApp()
    val userName = getCurrentUserNameWithGuest

    // QUESTION: このコードをより綺麗にまとめられる方法はあるのか？
    (for {
      article <- app.findArticle(articleId).right
      user <- app.findUser(article).right
    } yield (user, article)) match {
      case Right(result) => {
        val user = result._1
        val article = result._2
        if(user.name == userName) {
          app.deleteArticle(article.articleId) match {
            case Right(_) => Ok()
            case Left(errorResult) => errorResult
          }
        } else {
          NotFound()
        }
      }
      case Left(errorResult) => errorResult
    }
  }
}
