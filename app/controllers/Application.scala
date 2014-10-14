package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.Date
import java.text.SimpleDateFormat

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

  def getUTasks(usuario: String) = Action {
  	if(Task.userExists(usuario) == 1){
  		var json = Json.toJson(Task.getUTasks(usuario))
  		Ok(json)
  	} else NotFound("No existe el usuario")
 }

 def tasksf(usuario: String) = Action {
 	if(Task.userExists(usuario) == 1){
 		var json = Json.toJson(Task.getUDateTask(usuario))
 		Ok(json)
 	} else NotFound("No existe usuario")
 }

 def withDate(usuario: String, fecha: String) = Action{
 	if(Task.userExists(usuario) == 1){
 			var json = Json.toJson(Task.withDate(usuario, fecha))
 			Ok(json)
 		} else NotFound("No existe el usuario")
 }

 def fechadesp(usuario: String, fecha: String) = Action{
 	if(Task.userExists(usuario) == 1){
 		var formato = new SimpleDateFormat("yyyy-MM-dd")
  			var f = formato.parse(fecha)
 		var json = Json.toJson(Task.fechadesp(usuario, f))
 		Ok(json)
 	} else NotFound("No existe el usuario")
 }

  def newUDateTask(usuario: String, fecha: String) = Action {implicit request =>
  	if(Task.userExists(usuario) != 1) NotFound("No se ha encontrado el usuario") else
  	taskForm.bindFromRequest.fold(
  		errors => BadRequest(views.html.index(Task.all(), errors)),
  		label => {
  			var formato = new SimpleDateFormat("yyyy-MM-dd")
  			var f = formato.parse(fecha)
  			Task.createUDateTask(label, usuario, f)
 			var json = Json.toJson(Task.getTask(Task.getLast()))
  			Created(json)
  			})
  }

  def newUTasks(usuario: String) = Action { implicit request =>
  	if(Task.userExists(usuario) != 1) NotFound("No se ha encontrado el usuario") else
  	taskForm.bindFromRequest.fold(
  		errors => BadRequest(views.html.index(Task.all(), errors)),
  		label => {
  			Task.createUTask(label, usuario)
  			var json = Json.toJson(Task.getTask(Task.getLast()))
  			Created(json)
  			})
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
   /*Task.delete(id)
   Redirect(routes.Application.tasks)
   */
   var cons = Task.getTask(id)
   if(cons == Nil)
      NotFound("No se ha encontrado la tarea")
    else{
      Task.delete(id)
      Ok("Tarea borrada correctamente")
    }
  }

  def getTask(id: Long) = Action {
  	//Ok(views.html.index(Task.getTask(id), taskForm))
  	var json = Json.toJson(Task.getTask(id))
  	Ok(json)
  }
}