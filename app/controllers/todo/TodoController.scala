package controllers.todo

import ixias.model.Entity
import lib.model.Todo
import lib.model.Category
import lib.persistence.onMySQL

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import play.api.mvc.BaseController
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @SingletonでPlayFrameworkの管理下でSingletonオブジェクトとして本クラスを扱う指定をする
 * @Injectでconstructorの引数をDIする
 *   BaseControllerにはprotected の controllerComponentsが存在するため、そこに代入される。
 */

// controllersクラスの外に記載
case class TodoFormData(title: String, category_id: Int, body: String)
case class Test(title: String)

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport{

  // BaseControllerにActionメソッドが定義されているため、Actionがコールできる
  //   このActionにcontrollerComponentsが利用されているためInject部分でDIされている
  def list() = Action async{ implicit request: Request[AnyContent] =>
    for {
      results <- onMySQL.TodoRepository.get_all()
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
      result <- onMySQL.TodoRepository.get(Todo.Id(id))
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

  val form = Form(
    // html formのnameがcontentのものを140文字以下の必須文字列に設定する
    mapping(
      "title"       -> nonEmptyText(maxLength = 140),
      "category"    -> number,
      "body"        -> nonEmptyText(maxLength = 140)
    )(TodoFormData.apply)(TodoFormData.unapply)
  )
  val test = Form(
    // html formのnameがcontentのものを140文字以下の必須文字列に設定する
    mapping(
      "title" -> nonEmptyText(maxLength = 140)
    )(Test.apply)(Test.unapply)
  )
  def register() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.todo.store(form))
  }


  def store() = Action { implicit request: Request[AnyContent] =>
    // foldでデータ受け取りの成功、失敗を分岐しつつ処理が行える
    form.bindFromRequest().fold(
      // 処理が失敗した場合に呼び出される関数
      // 処理失敗の例: バリデーションエラー
      (formWithErrors: Form[TodoFormData]) => {
        BadRequest(views.html.todo.store(formWithErrors))
      },

      // 処理が成功した場合に呼び出される関数
      (todoFormData: TodoFormData) => {
        // 登録処理としてSeqに画面から受け取ったコンテンツを持つTweetを追加
        onMySQL.TodoRepository.add(Todo(category_id = todoFormData.category_id, title = todoFormData.title, body = todoFormData.body, state = 0))
        // 登録が完了したら一覧画面へリダイレクトする
        Redirect("/todo/list")
        // 以下のような書き方も可能です。基本的にはtwirl側と同じです
        // 自分自身がcontrollers.tweetパッケージに属しているのでcontrollers.tweetの部分が省略されています。
        // Redirect(routes.TweetController.list())
      }
    )
  }
}