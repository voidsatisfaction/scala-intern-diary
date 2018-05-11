package interndiary.service

sealed trait Error {
  override def toString(): String = this match {
    case DiaryNotFoundError => "Can not find a target diary"
    case DiaryAlreadyExistsError => "Can not add diary which already exists"
    case ArticleNotFoundError => "Can not find a target article"
    case ArticleAlreadyExistsError => "Can not add article which already exists"
  }
}
final case object DiaryNotFoundError extends Error
final case object DiaryAlreadyExistsError extends Error
final case object ArticleNotFoundError extends Error
final case object ArticleAlreadyExistsError extends Error
