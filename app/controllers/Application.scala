package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Application extends Controller {

 implicit val taskWrites: Writes[Task] = (
  	(JsPath \ "id").write[Long] and
  	(JsPath \ "String").write[String] 
 )(unlift(Task.unapply))

  val taskForm = Form(
     "label" -> nonEmptyText
  )

  def index = Action {
  	Ok(views.html.index(Task.all(), taskForm))
  }

  //Aqui iria la lista d tareas en JSonk
  def tasks = Action {
  	var json = Json.toJson(Task.all())
  	Ok(json)
  }

  def newTask = Action { implicit request =>
   taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
         Task.create(label)
         var json = Json.toJson(Task.getTask(Task.getLast()))
         Created(json)
      }
   )
}

  def deleteTask(id: Long) = Action {
   Task.delete(id)
   Redirect(routes.Application.tasks)
  }

  def getTask(id: Long) = Action {
  	//Ok(views.html.index(Task.getTask(id), taskForm))
  	var json = Json.toJson(Task.getTask(id))
  	Ok(json)
  }
}