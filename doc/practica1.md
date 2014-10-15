#Play todo-list
##Documentación práctica 1
---
####Funcionamiento básico de la práctica

Play framework trabaja con un fichero de rutas como punto de entrada ala aplicación. Cuando se introduce una URL, el servidor busca en dicho fichero dicha URL y su recurso asignado. Cada linea del fichero "routes" está compuesto de tres elementos:

* Metodo
* URL de acceso
* Recurso asignado a dicha URL

Una vez se accede al recurso (controlador), se manejan los parámetros en caso de que los haya, se llama a la capa de modelo si hay que trabajar con base de datos, se construye la respuesta para el navegador y se envia.

```
A continuación se procederá a explicar las funcionalidades implementadas en cada feature. La forma de hacerlo será, nombrar cada ruta, explicar lo que hace el recurso al que se accede en el controlador y los metodos empleados de la capa de Modelo (En caso de hacerlo)
```

- #####Consulta de una tarea
	- **Ruta**: `GET /tasks/{id} controllers.Application.getTask(id:Long)`
	- **Recurso**: `getTask(id)` A partir de la llamada a la capa de modelo devuelve una respresentación JSon de la tarea, ésta será vacia en caso de no existir la tarea.
	- **Modelo**: `getTask(id)` Contiene la consulta SQL que devuelve la tarea con id pasado por parámetro.
	
- #####Creación de una nueva tarea
	- **Ruta**: `POST /tasks controllers.Application.newTask`
	- **Recurso**: `newTask` Comprueba que los parámetros son correctos y llama a la capa de modelo para añadir la tarea. Una vez añadida se crea un json con la tarea recien añadida. Para dicha operación se hace uso del metodo getLast de la capa de modelo para saber la ultima tarea insertada
	- **Modelo**: `create(label)` Añade la tarea a la base de datos, como no estamos usando login se insertará el usuario "Anonimin" por defecto. getLast Consulta el id de la ultima tarea añadida.

- #####Listado de tareas
	- **Ruta**: `GET /tasks controllers.Application.tasks`
	- **Recurso**: `tasks` Crea un JSon con todas las tareas de la base de datos cuyo usuario creador sea "Anonimin"
	- **Modelo**: `all()` Devuelve una Lista de tareas cuyo creador sea "Anonimin"

- #####Borrado de una tarea
	- Ruta: **DELETE /tasks/{id}** controllers.Application.deleteTask(id: Long)
	- Recurso: deleteTask(id) LLama a la capa de modelo para borrar dicha tarea. Primero se comprobara si la tarea existe empleando el metodo task(id). Si la tarea no existe se devolverá un 404 Not Found.
	- Modelo: delete(id) Borra la tarea con el id especificado de la base de datos. 
	
- #####Listado de tareas de un usuario
	- **Ruta**: `GET /{usuario}/tasks controllers.Application.getUTasks(usuario: String)`
	- **Recurso**: `getUTasks(usuario)` Primeramente comprueba que el usuario exista. Si no se devuelve un 404 Not Found. Despues llama a la capa de modelo para ver las tareas de dicho usuario y construir el JSON con dichas tareas.
	- **Modelo**: `getUTask(usuario)` Delvuelve una lista de tareas las cuales tienen como usuario el pasado por parametro.
	
- #####Creación de una nueva tarea con usuario
	- **Ruta**: `POST /{usuario}/tasks controllers.Application.newUTasks(usuario: String)`
	- **Recurso**: `newUTasks(usuario)` Al igual que con el GET, se comprueba que el usuario exista, devolviendo un 404 Not Found en caso de negativa. Despues se envia a la capa de modelo el label para crear la tarea.
	- **Modelo**: `createUTask(label)` Se añade una tarea a la base de datos referenciando al usuario pasado por parametro.

```
Nota: Todas las fechas son tratadas como fechas con formato americano. ej. YYYY-MM-DD
```
```
Los usuarios disponibles en la base de datos son: 
	"Anonimin" como usuario anonimo para las funciones del feature 1
	"Jorge" y "Payasin" como usuarios normales
``

- #####Creación de una nueva tarea con fecha limite
	-  **Ruta**: `POST /{usuario}/tasks/{fecha} controllers.Application.newUDateTask(usuario: String, fecha: String)` 
	- **Recurso**: `newDateTask(usuario, fecha)` Función que comprueba que el usuario exista y llama a la capa de modelo para insertar a la base de datos la tarea con la fecha.
	- **Modelo**: `createUDateTask(label, usuario, fecha)` Añade una tarea con los parametros pasados.
	
- #####Listado de las tareas que contengan una fecha límite
	- **Ruta**: `GET	/:usuario/tasksf		controllers.Application.tasksf(usuario: String)`
	- **Recurso**: `tasksf(usuario)` Comprueba que el usuario existe y llama al metodo de la capa de modelo que se encarga de realizar la consulta. Despues devuelve un JSON con las tareas.
	- **Modelo**: `getUDateTask(usuario)` Consulta en la base de datos todas las tareas con fecha de un usuario.
	
- #####Buscar tareas con una fecha concreta
	- **Ruta**: `GET	/:usuario/withDate/:fecha`	controllers.Application.withDate(usuario: String, fecha: String)
	- **Recurso**: `withDate(usuario, fecha)` Comprueba que el usuario existe y llama a la funcion de la capa de modelo encargada de hacer la consulta. Posteriormente devuelve un JSON con el resultado.
	- **Modelo**: `withDate(usuario, fecha)` Devuelve las tareas de un usuario con una fecha concreta.
	
- #####Lista las tareas con una fecha posterior a la dada
	- **Ruta**: `GET	/:usuario/fechadesp/:fecha	controllers.Application.fechadesp(usuario: String, fecha: String)`
	- **Recurso**: `fechadesp(usuario, fecha)` Comprueba que existe el usuario y llama al metodo de la capa de modelo encargada de la consulta. Posteriormente devuelve un JSon con el resultado.
	- **Modelo**: `fechadesp(usuario, fecha)` Devuelve las tareas del usuario dado y con fecha posterior a la dada.

