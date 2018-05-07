package interndiary
import slick.dbio.{NoStream, DBIOAction}

package object repository {
  // QUESTION: private 後ろの[repository]はなんだ？
  private[repository] def run[R](a: DBIOAction[R, NoStream, Nothing])(implicit ctx: Context): R = Context.runDBIO(a)
}
