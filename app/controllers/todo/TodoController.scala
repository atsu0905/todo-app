package controllers.todo

import models.Todo

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import play.api.mvc.BaseController
import play.api.mvc.Request
import play.api.mvc.AnyContent

/**
 * @SingletonでPlayFrameworkの管理下でSingletonオブジェクトとして本クラスを扱う指定をする
 * @Injectでconstructorの引数をDIする
 *   BaseControllerにはprotected の controllerComponentsが存在するため、そこに代入される。
 */
@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  // BaseControllerにActionメソッドが定義されているため、Actionがコールできる
  //   このActionにcontrollerComponentsが利用されているためInject部分でDIされている
  def list() = Action { implicit request: Request[AnyContent] =>
    // 1から10までのTweetクラスのインタンスを作成しています。
    // 1 to 10だとIntになってしまうので1L to 10LでLongにしています。
    val todos: Seq[Todo] = (1L to 10L).map(i => Todo(Some(i), s"test todo${i.toString}"))

    // viewの引数としてtweetsを渡します。
    Ok(views.html.todo.list(todos))
  }
}