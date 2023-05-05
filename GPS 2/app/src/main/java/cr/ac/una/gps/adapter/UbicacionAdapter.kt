package cr.ac.una.gps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cr.ac.una.gps.entity.Ubicacion
import java.text.SimpleDateFormat

class UbicacionAdapter(context: Context, ubicaciones: List<Ubicacion>) :
    ArrayAdapter<Ubicacion>(context, 0, ubicaciones) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_ubicacion, parent, false)
        }
        val ubicacion = getItem(position)

        val fechaTextView = view!!.findViewById<TextView>(R.id.fecha)
        val latitudTextView = view.findViewById<TextView>(R.id.latitud)
        val longitudTextView = view.findViewById<TextView>(R.id.longitud)
        val inPoligonoTextView = view.findViewById<TextView>(R.id.inPoligono)

        fechaTextView.text = "Fecha: "+ubicacion!!.fecha.toString()
        latitudTextView.text = "Latitud: "+ubicacion.latitud.toString()
        longitudTextView.text = "Longitud: "+ubicacion.longitud.toString()
        inPoligonoTextView.text = "Dentro del poligono: "+ ubicacion.inPoligono.toString()

        return view
    }
}