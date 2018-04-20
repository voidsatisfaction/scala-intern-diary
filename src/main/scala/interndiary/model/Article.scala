package interndiary.model

import org.joda.time.LocalDateTime

case class Article(
  articleId: Long,
  title: String,
  body: String,
  created: LocalDateTime,
  updated: LocalDateTime
)
