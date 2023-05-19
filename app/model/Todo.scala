package models

// case classについての説明は省略
// 参考: https://docs.scala-lang.org/ja/tour/case-classes.html
case class Todo(
                  id:      Option[Long],
                  content: String
                )

