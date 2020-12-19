// QUESTION: これはどうやって動いているのだ？
// mixinとして入ることになる。
package interndiary.web

import org.scalatra._
import interndiary.service.Context

object AppContextSupport {
  val key = {
    val n = getClass.getName
    if (n.endsWith("$")) n.dropRight(1) else n
  }
}

trait AppContextSupport { this: ScalatraBase =>

  implicit def appContext = request.get(AppContextSupport.key).orNull.asInstanceOf[Context]

  before() {
    request(AppContextSupport.key) = Context.createContext
  }

  after() {
    Context.releaseContext(appContext)
  }

}
