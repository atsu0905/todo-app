package models

// case classについての説明は省略
// 参考: https://docs.scala-lang.org/ja/tour/case-classes.html
case class Todo2(
                 id:           Long,
                 category_id:  Long,
                 title:        String,
                 body:         String,
                 state:        Int
               )