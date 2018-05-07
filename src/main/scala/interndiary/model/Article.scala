package interndiary.model

import org.joda.time.LocalDateTime

case class Article(
  articleId: Long,
  title: String,
  body: String,
  diaryId: Long,
  created: LocalDateTime,
  updated: LocalDateTime
)
