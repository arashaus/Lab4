package cr.ac.una.gps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cr.ac.una.gps.entity.UbicacionPoligono

class UbicacionPoligonoAdapter(context: Context, ubicaciones: List<UbicacionPoligono>) :
    ArrayAdapter<UbicacionPoligono>(context, 0, ubicaciones) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_poligono, parent, false)
        }

        val ubicacion = getItem(position)

        val latitudTextView = view?.findViewById<TextView>(R.id.latitudPoligono)
        val longitudTextView = view?.findViewById<TextView>(R.id.longitudPoligono)

        if (latitudTextView != null) {
            if (ubicacion != null) {
                latitudTextView.text = "Latitud: "+ ubicacion.latitudPoligono.toString()
            }
        }
        if (longitudTextView != null) {
            if (ubicacion != null) {
                longitudTextView.text = "Longitud: " +  ubicacion.longitudPoligono.toString()
            }
        }

        return view!!
    }
}