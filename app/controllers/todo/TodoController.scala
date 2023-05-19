package controllers.todo

import ixias.model.Entity
import models.Todo
import lib.model.User
import lib.persistence.onMySQL

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import play.api.mvc.BaseController
import play.api.mvc.Request
import play.api.mvc.AnyContent

import scala.concurrent.Await
import scala.concurrent.duration.Duration
//import scala.concurrent.ExecutionContext
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
    /*
    val todos: Seq[Todo] = (1L to 10L).map(i => Todo(Some(i), s"test todo${i.toString}"))
    val userWithNoId: User#WithNoId =User.apply(name = "hogehoge", slug = "slug", color = 1)
    val userId = onMySQL.UserRepository.add(userWithNoId)
    val id = Await.result(userId, Duration.Inf)
    val user2 = Await.result(onMySQL.UserRepository.get(id), Duration.Inf).get
    val name2 = user2.v.name
    val id2 = user2.v.id

    */
    //val db_todo_list = Await.result(onMySQL.UserRepository.get_all(), Duration.Inf)
    //val db_todo_list = onMySQL.UserRepository.get_all().map(todo => Todo(id = (todo.v.id),content = todo.v.name)

    for {
      results <- onMySQL.UserRepository.get_all()
    } yield {
      //Ok(views.html.todo.list(results.map(todo => Todo(id = (todo.v.id),content = todo.v.title))))
      Ok(views.html.todo.list(results))
    }
  }
  def show(id :Long) = Action async { implicit request: Request[AnyContent] =>
    //println(Await.result(onMySQL.UserRepository.get(User.Id(100)), Duration.Inf))
    //println("!!!!!!")
    //Ok(views.html.todo.show(Todo(Some(1), s"test todo${1.toString}")))

    for {
      result <- onMySQL.UserRepository.get(User.Id(id))
    } yield {
      result match {
        case Some(todo) => Ok(views.html.todo.show(Todo(id = todo.v.id, content = todo.v.title)))
        //case Some(todo) => Ok(views.html.todo.show(result)))
        case None => NotFound(views.html.common.page404())
      }
    }


  }

  def list_category() = Action async { implicit request: Request[AnyContent] =>
    /*
    val todos: Seq[Todo] = (1L to 10L).map(i => Todo(Some(i), s"test todo${i.toString}"))
    val userWithNoId: User#WithNoId =User.apply(name = "hogehoge", slug = "slug", color = 1)
    val userId = onMySQL.UserRepository.add(userWithNoId)
    val id = Await.result(userId, Duration.Inf)
    val user2 = Await.result(onMySQL.UserRepository.get(id), Duration.Inf).get
    val name2 = user2.v.name
    val id2 = user2.v.id

    */
    //val db_todo_list = Await.result(onMySQL.UserRepository.get_all(), Duration.Inf)
    //val db_todo_list = onMySQL.UserRepository.get_all().map(todo => Todo(id = (todo.v.id),content = todo.v.name)

    for {
      results <- onMySQL.CategoryRepository.get_all()
    } yield {
      println(results)
      //Ok(views.html.todo.list_category(results))
      Ok(views.html.todo.list_category(Seq(1,2,3)))
    }
  }
}