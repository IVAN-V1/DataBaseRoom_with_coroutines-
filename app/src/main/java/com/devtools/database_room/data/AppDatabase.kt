package com.devtools.database_room.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devtools.database_room.Model.ModelUser

@Database(entities = [ModelUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao


    /***
     * companion object: Implementa un patrón Singleton para asegurar que solo haya una instancia de la base de datos en toda la aplicación. Esto es una práctica común para las bases de datos Room

     */

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /***
         *
         * getDatabase(context: android.content.Context):
         * Un método estático para obtener la instancia de la base de datos. Utiliza Room.databaseBuilder para crear la base de datos si aún no existe. El nombre del archivo de la base de datos será "app_database".
         *
         *
         */


        fun getDatabase(context: android.content.Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }


    }

}