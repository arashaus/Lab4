package cr.ac.una.gps

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.core.content.PackageManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cr.ac.una.gps.entity.UbicacionPoligono
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM2 = "param2"

class restrictedAreaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param2: String? = null
    lateinit var entity: UbicacionPoligono

    lateinit var TXT_latitud: EditText
    lateinit var TXT_longitud: EditText
    lateinit var listView: ListView
    lateinit var btnagregar: Button
    lateinit var ubicacionesPoligono: List<UbicacionPoligono>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val appContext = requireContext().applicationContext
        val vista: View = inflater.inflate(R.layout.fragment_restricted_area, container, false)

        listView = vista.findViewById(R.id.listaUbicacionesPoligono)
        btnagregar = vista.findViewById(R.id.agregar)
        TXT_latitud = vista.findViewById(R.id.latitud_Poligono)
        TXT_longitud = vista.findViewById(R.id.longitud_Poligono)

        val ubicacionPoligonoDao = (activity as MainActivity).ubicacionPoligonoDao
        cargarVista(appContext)


        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val id: UbicacionPoligono? = listView.getItemAtPosition(position) as UbicacionPoligono?
            println(id)

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Importante")
            builder.setMessage("Va a eliminar la " + id)
            builder.setPositiveButton(android.R.string.yes) { _, _ ->

                Thread {
                    ubicacionPoligonoDao.delete(id!!)//Do your database´s operations here
                }.start()
                println("Dato eliminado")
                cargarVista(appContext)
            }
            builder.setNegativeButton(android.R.string.no) { _, _ -> }
            builder.show()
        }

        btnagregar.setOnClickListener {
            Log.d(PackageManagerCompat.LOG_TAG, "selecciono el boton")

            val latitud_ = TXT_latitud.text.toString()
            val latitud: Double = latitud_.toDouble()
            val longitud_ = TXT_longitud.text.toString()
            val longitud: Double = longitud_.toDouble()

            if (latitud_.isNotEmpty() and longitud_.isNotEmpty()) {

                val l = (activity as MainActivity)
                entity = UbicacionPoligono(
                    id = null,
                    latitudPoligono = latitud,
                    longitudPoligono = longitud,
                )
                l.insertEntityPoligono(entity)
                cargarVista(appContext)
            }
        }

        // Inflate the layout for this fragment
        return vista
    }

    fun cargarVista(appContext: Context){
        val ubicacionPoligonoDao = (activity as MainActivity).ubicacionPoligonoDao
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ubicacionesPoligono = ubicacionPoligonoDao.getAll() as List<UbicacionPoligono>
                if (ubicacionesPoligono.isNotEmpty()) {
                    activity?.runOnUiThread {
                        val adapter = UbicacionPoligonoAdapter(appContext, ubicacionesPoligono)
                        listView.adapter = adapter
                    }
                }
            }
        }
    }


}