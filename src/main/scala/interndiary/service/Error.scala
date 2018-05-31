package interndiary.service

sealed trait Error

sealed trait UserError extends Error {
  override def toString(): String = this match {
    case UserNotFound => "Can not find a target user"
    case UserNotAuthorized => "Not authorized user"
  }
}
final case object UserNotFound extends UserError
final case object UserNotAuthorized extends UserError

sealed trait DiaryError extends Error {
  override def toString(): String = this match {
    case DiaryNotFound => "Can not find a target diary"
    case DiaryAlreadyExists => "Can not add diary which already exists"
  }
}
final case object DiaryNotFound extends DiaryError
final case object DiaryAlreadyExists extends DiaryError

sealed trait ArticleError extends Error {
  override def toString(): String = this match {
    case ArticleNotFound => "Can not find a target article"
    case ArticleAlreadyExists => "Can not add article which already exists"
  }
}
final case object ArticleNotFound extends ArticleError
final case object ArticleAlreadyExists extends ArticleError
