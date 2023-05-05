package cr.ac.una.gps.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Ubicacion(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val latitud: Double,
    val longitud: Double,
    val fecha: Date,
    val inPoligono: Boolean
)

// agregar enpoligono --> es para ver si la ubicacion esta dentro del poligono
// bool


// lo que se puede ir haciendo es que se haga un mantenimiento para que el polygonoOptions
// sea sacado de la base de datos

//  necesitamos un listView

// lista de puntos de poligonos en base de datos y con scroll en la app
// una vista que liste todas las ubicacciones del poligono, un boton incluir que lleve a una
// vista que reciba latitud y longitd y pueda guardarla en la base de datos