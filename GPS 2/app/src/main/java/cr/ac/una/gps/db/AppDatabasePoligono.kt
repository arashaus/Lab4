package cr.ac.una.gps

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cr.ac.una.gps.entity.UbicacionPoligono

@Database(entities = [UbicacionPoligono::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabasePoligono : RoomDatabase() {
    abstract fun ubicacionUbicacionPoligonoDao(): UbicacionPoligonoDao

    companion object {
        private var instance: AppDatabasePoligono? = null

        fun getInstance(context: Context): AppDatabasePoligono {
            if (instance == null) {
                synchronized(AppDatabasePoligono::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabasePoligono::class.java,
                        "ubicaciones-poligono-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}