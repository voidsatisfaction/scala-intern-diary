package interndiary.model

import org.joda.time.LocalDateTime

case class Diary(
  diaryId: Long,
  title: String,
  userId: Long,
  created: LocalDateTime
)
