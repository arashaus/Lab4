package cr.ac.una.gps

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.gps.entity.UbicacionPoligono


@Dao
interface UbicacionPoligonoDao {
    @Insert
    fun insert(entity: UbicacionPoligono)

    @Query("SELECT * FROM ubicacionPoligono")
    fun getAll(): List<UbicacionPoligono?>?

    @Delete
    fun delete(entity: UbicacionPoligono)

    @Query("SELECT * FROM ubicacionPoligono WHERE id LIKE :dato")
    fun get(dato: Long): UbicacionPoligono

}