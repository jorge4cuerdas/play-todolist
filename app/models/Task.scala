package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

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
         SQL("insert into task (label, usuario) values ({label}, 'Anonimin')").on(
            'label -> label
         ).executeUpdate()
      }
   }

   def createUTask(label: String, usuario: String){
      DB.withConnection { implicit c =>
         SQL("insert into task (label, usuario) values ({label}, {usuario})").on(
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