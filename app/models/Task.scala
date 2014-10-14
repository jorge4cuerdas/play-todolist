package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

import java.util.Date

case class Task(id: Long, label: String)

object Task {

   val task = {
      get[Long]("id") ~
      get[String]("label") map {
         case id~label => Task(id, label)
      }
   }

   def all(): List[Task] = DB.withConnection {implicit c =>
      SQL("select * from task where usuario = 'Anonimin'").as(task *)
   }

   def create(label: String){
      DB.withConnection { implicit c =>
         SQL("insert into task (label, usuario, fecha) values ({label}, 'Anonimin', NULL)").on(
            'label -> label
         ).executeUpdate()
      }
   }

   def getUDateTask(usuario: String): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario = {usuario} and fecha is not null").on(
         'usuario -> usuario
      ).as(task *)
   }

   def withDate(usuario: String, fecha: String): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario = {usuario} and fecha = {fecha}").on(
         'usuario -> usuario,
         'fecha -> fecha
         ).as(task *)
   }

   def userExists(nombre: String): Long = DB.withConnection { implicit c =>
         SQL("select count(*) from usr where usr.nombre = {nombre}").on(
            'nombre -> nombre
         ).as(scalar[Long].single)
   }

   def createUDateTask(label: String, usuario: String, fecha: Date){
   	DB.withConnection{ implicit c =>
   		SQL("INSERT INTO task (label, usuario, fecha) values ({label}, {usuario}, {fecha})").on(
   			'label -> label,
   			'usuario -> usuario,
   			'fecha -> fecha
   			).executeUpdate()
   		}
   }

   def createUTask(label: String, usuario: String){
      DB.withConnection { implicit c =>
         SQL("insert into task (label, usuario, fecha) values ({label}, {usuario}, NULL)").on(
            'label -> label,
            'usuario -> usuario
         ).executeUpdate()
      }
   }

   def delete(id: Long) {
      DB.withConnection { implicit c =>
         SQL("delete from task where id = {id}").on(
            'id -> id
         ).executeUpdate()
      }
   }

   def getLast() : Long={
      DB.withConnection {implicit c =>
         SQL("select max(id) from task").as(scalar[Long].single)
      }
   }

   def getTask(id: Long): List[Task]={
      DB.withConnection { implicit c =>
         SQL("select * from task where id = {id}").on(
            'id -> id
         ).as(task *)
      }
   }

   def getUTasks(usuario: String): List[Task]={
      DB.withConnection {implicit c =>
         SQL("select * from task where usuario = {usuario}").on(
            'usuario -> usuario
         ).as(task *)
      }
   }
}