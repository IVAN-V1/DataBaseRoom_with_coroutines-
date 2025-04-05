package com.devtools.database_room.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devtools.database_room.Model.ModelUser


@Dao
interface UserDao {


    @Query("SELECT * FROM users")
    fun getAll(): List<ModelUser>

    @Insert
    fun insertAll(vararg users: ModelUser)

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<ModelUser>

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): ModelUser

    @Query("DELETE FROM users")
    fun deleteAll()

}