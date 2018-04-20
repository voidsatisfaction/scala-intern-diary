package interndiary.model

import org.joda.time.LocalDateTime

case class User(
  userId: Long,
  name: String,
  created: LocalDateTime
)
