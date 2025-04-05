package com.devtools.database_room

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.devtools.database_room.Model.ModelUser
import com.devtools.database_room.data.AppDatabase
import com.devtools.database_room.data.UserDao
import com.devtools.database_room.ui.theme.DataBase_RoomTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            DataBase_RoomTheme {

                UIView()
            }
        }
    }
}


@Composable
fun UIView() {

    lateinit var db: AppDatabase
    lateinit var userDao: UserDao


    //Room
    val currentContext = LocalContext.current
    db = AppDatabase.getDatabase(currentContext)
    userDao = db.userDao()
    //

    //Coroutines
    val corutineScope = rememberCoroutineScope()
    //


    var lista_users by remember { mutableStateOf(listOf<ModelUser>()) }
    var nombre by remember { mutableStateOf("") }
    var apelido by remember { mutableStateOf("") }
    var hayError_nombre by remember { mutableStateOf(false) }
    var mensajeError_nombre by remember { mutableStateOf("") }

    var hayError_apellido by remember { mutableStateOf(false) }
    var mensajeError_apellido by remember { mutableStateOf("") }



    val refreshUserList = remember {
        {
            corutineScope.launch(Dispatchers.IO) {

                val users = userDao.getAll()

                withContext(Dispatchers.Main) {
                    lista_users = users
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        refreshUserList()
    }




    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ejemplo de Corrutinas con Base de datos Room",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Este ejemplo muestra cómo usar corrutinas con una base de datos local")

                Spacer(modifier = Modifier.height(16.dp))




                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = nombre,
                    onValueChange = { nuevoTexto ->
                        nombre = nuevoTexto

                    },
                    label = { Text("Introduce Nombre") },
                    placeholder = { Text("Ej: Carlos") },
                    isError = hayError_nombre,

                    supportingText = {
                        if (hayError_nombre) {
                            Text(
                                text = mensajeError_nombre,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(text = "Introduce al menos 3 caracteres")
                        }


                    }
                )


                Spacer(modifier = Modifier.height(16.dp))


                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = apelido,

                    onValueChange = { nuevoTexto ->
                        apelido = nuevoTexto

                    },

                    label = { Text("Introduce Apellido") },
                    placeholder = { Text("Ej: Martinez") },
                    isError = hayError_apellido,

                    supportingText = {

                        if (hayError_apellido) {
                            Text(
                                text = mensajeError_apellido,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(text = "Introduce al menos 3 caracteres")
                        }


                    }
                )


                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Blue),
                    onClick = {

                        corutineScope.launch(Dispatchers.IO) {


                            if (nombre.isEmpty() || apelido.isEmpty()) {
                                hayError_nombre = true
                                mensajeError_nombre = "Este campo no puede estar vacío"
                                hayError_apellido = true
                                mensajeError_apellido = "Este campo no puede estar vacío"
                            } else if (nombre.length < 3 || apelido.length < 3) {
                                hayError_nombre = true
                                mensajeError_nombre = "El texto debe tener al menos 3 caracteres"
                                hayError_apellido = true
                                mensajeError_apellido = "El texto debe tener al menos 3 caracteres"
                            } else {

                                hayError_nombre = false
                                mensajeError_nombre = ""
                                hayError_apellido = false
                                mensajeError_apellido = ""


                                val User_ = ModelUser(firstName = nombre, lastName = apelido)

                                userDao.insertAll(User_)

                                withContext(Dispatchers.Main) {

                                    refreshUserList()
                                    Toast.makeText(currentContext, "Usuario agregado", Toast.LENGTH_SHORT).show()
                                }


                            }


                        }


                    }
                ) {

                    Text("Ingresar datos")

                }


                LazyColumn {


                    items(lista_users.size) { user ->

                        List(modelUser = lista_users[user], refreshList = {refreshUserList()})
                    }

                }

            }


        }


    }


}


@Composable
fun List(modelUser: ModelUser, refreshList: () -> Unit) {

    lateinit var db: AppDatabase
    lateinit var userDao: UserDao


    //Room
    val currentContext = LocalContext.current
    db = AppDatabase.getDatabase(currentContext)
    userDao = db.userDao()

    val corutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Text(text = "ID: ${modelUser.id}")
        Text(text = "Nombre: ${modelUser.firstName}")
        Text(text = "Apellido: ${modelUser.lastName}")

       IconButton(onClick = {


           // background thread [backend]
           corutineScope.launch (Dispatchers.IO){

              userDao.deleteById(modelUser.id ?: 0)


               // Main thread [Design]
               withContext(Dispatchers.Main) {


                   refreshList()
                   Toast.makeText(currentContext, "User Delete", Toast.LENGTH_SHORT).show()


               }


           }


       }) {

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }


    }

}



