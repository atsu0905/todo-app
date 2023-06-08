/**
  * This is a sample of Todo Application.
  * 
  */

package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

// ユーザーを表すモデル
//~~~~~~~~~~~~~~~~~~~~
import User._
import Category._
case class User(
  id:        Option[User.Id],
  category_id:        Long,
  title:      String,
  body:      String,
  state:     Int,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[User.Id]


// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object User {

  val  Id = the[Identity[Id]]
  type Id = Long @@ User
  type WithNoId = Entity.WithNoId [Id, User]
  type EmbeddedId = Entity.EmbeddedId[Id, User]

  // ステータス定義
  //~~~~~~~~~~~~~~~~~
  sealed abstract class Status(val code: Short, val name: String) extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object IS_INACTIVE extends Status(code = 0,   name = "無効")
    case object IS_ACTIVE   extends Status(code = 100, name = "有効")
  }

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(category_id: Long, title: String, body: String, state: Int): WithNoId = {
    new Entity.WithNoId(
      new User(
        id    = None,
        category_id = category_id,
        title  = title,
        body   = body,
        state = state
      )
    )
  }
}

case class Category(
                     id:        Option[Category.Id],
                     name:      String,
                     slug:      String,
                     color:     Int,
                     updatedAt: LocalDateTime = NOW,
                     createdAt: LocalDateTime = NOW
                   ) extends EntityModel[Category.Id]


object Category {

  val  Id = the[Identity[Id]]
  type Id = Long @@ Category
  type WithNoId = Entity.WithNoId [Id, Category]
  type EmbeddedId = Entity.EmbeddedId[Id, Category]

  // ステータス定義
  //~~~~~~~~~~~~~~~~~
  sealed abstract class Status(val code: Short, val name: String) extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object IS_INACTIVE extends Status(code = 0,   name = "無効")
    case object IS_ACTIVE   extends Status(code = 100, name = "有効")
  }

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(name: String, slug: String, color: Int): WithNoId = {
    new Entity.WithNoId(
      new Category(
        id    = None,
        name = name,
        slug  = slug,
        color   = color
      )
    )
  }
}

