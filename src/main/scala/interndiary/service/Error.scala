package interndiary.service

sealed trait Error {
  override def toString(): String = this match {
    case DiaryNotFoundError => "Can not find a target diary"
    case DiaryAlreadyExists => "Can not add diary which already exists"
    case ArticleNotFound => "Can not find a target article"
    case ArticleAlreadyExists => "Can not add article which already exists"
  }
}
final case object DiaryNotFoundError extends Error
final case object DiaryAlreadyExists extends Error
final case object ArticleNotFound extends Error
final case object ArticleAlreadyExists extends Error
