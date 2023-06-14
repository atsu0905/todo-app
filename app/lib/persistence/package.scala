/**
  * This is a sample of Todo Application.
  * 
  */

package lib

package object persistence {

  val default = onMySQL
  
  object onMySQL {
    implicit lazy val driver = slick.profile.MyDBProfile
    object TodoRepository extends TodoRepository
    object CategoryRepository extends CategoryRepository
  }
}
