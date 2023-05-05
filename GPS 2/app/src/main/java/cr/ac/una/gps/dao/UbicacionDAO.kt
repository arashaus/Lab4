package cr.ac.una.gps

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.gps.entity.Ubicacion
import java.util.*


@Dao
interface UbicacionDao {
    @Insert
    fun insert(entity: Ubicacion)

    @Query("SELECT * FROM ubicacion")
    fun getAll(): List<Ubicacion?>?

    @Delete
    fun delete(entity: Ubicacion)

    @Query("SELECT * FROM ubicacion WHERE (fecha BETWEEN :dateStart and :dateEnd)")
    fun get(dateStart : Date, dateEnd:Date): List<Ubicacion?>?
}