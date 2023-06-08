package controllers.todo

import ixias.model.Entity
import models.Todo
import lib.model.User
import lib.model.Category
import lib.persistence.onMySQL

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import play.api.mvc.BaseController
import play.api.mvc.Request
import play.api.mvc.AnyContent

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @SingletonでPlayFrameworkの管理下でSingletonオブジェクトとして本クラスを扱う指定をする
 * @Injectでconstructorの引数をDIする
 *   BaseControllerにはprotected の controllerComponentsが存在するため、そこに代入される。
 */
@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  // BaseControllerにActionメソッドが定義されているため、Actionがコールできる
  //   このActionにcontrollerComponentsが利用されているためInject部分でDIされている
  def list() = Action async{ implicit request: Request[AnyContent] =>
    for {
      results <- onMySQL.UserRepository.get_all()
    } yield {
      Ok(views.html.todo.list(results))
    }
  }

  def list_category() = Action async { implicit request: Request[AnyContent] =>
    for {
      results <- onMySQL.CategoryRepository.get_all()
    } yield {
      Ok(views.html.todo.list_category(results))
    }
  }

  def show(id :Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      result <- onMySQL.UserRepository.get(User.Id(id))
    } yield {
      result match {
        case Some(todo) => Ok(views.html.todo.show(todo))
        case None => NotFound(views.html.common.page404())
      }
    }
  }

  def show_category(id: Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      result <- onMySQL.CategoryRepository.get(Category.Id(id))
    } yield {
      result match {
        case Some(category) => Ok(views.html.todo.show_category(category))
        case None => NotFound(views.html.common.page404())
      }
    }
  }
}