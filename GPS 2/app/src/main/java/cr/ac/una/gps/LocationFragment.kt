package cr.ac.una.gps

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.core.content.PackageManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cr.ac.una.gps.entity.Ubicacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val ARG_PARAM2 = "param2"

@SuppressLint("StaticFieldLeak")
lateinit var filtro: EditText

@SuppressLint("StaticFieldLeak")
lateinit var listView: ListView

@SuppressLint("StaticFieldLeak")
lateinit var btnfiltrar: Button
lateinit var ubicaciones: List<Ubicacion>

class LocationFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var param2: String? = null

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
        val vista: View = inflater.inflate(R.layout.fragment_location, container, false)

        listView = vista.findViewById(R.id.listUbicaciones)
        val ubicacionDao = (activity as MainActivity).ubicacionDao

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ubicaciones = ubicacionDao.getAll() as List<Ubicacion>
                val adapter = UbicacionAdapter(appContext, ubicaciones)
                listView.adapter = adapter
            }
        }

        filtro = vista.findViewById(R.id.filtro)
        btnfiltrar = vista.findViewById(R.id.enviar_filtro)

        btnfiltrar.setOnClickListener {
            Log.d(PackageManagerCompat.LOG_TAG, "selecciono el boton")
            val buscar = filtro.text.toString()
            var l = (activity as MainActivity)

            if (buscar.isNotEmpty() ) {

                l.filtroUbicacion = buscar
                val dateEnd = Date(buscar)
                val dateStart = Date(buscar)
                dateEnd.hours = 23
                dateEnd.minutes = 59
                println("se puede buscar")

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        ubicaciones = ubicacionDao.get(dateStart, dateEnd) as List<Ubicacion>
                        activity?.runOnUiThread {
                            val adapter = UbicacionAdapter(appContext, ubicaciones)
                            listView.adapter = adapter
                        }
                    }
                }
            }
            else{
                l.filtroUbicacion = ""
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        ubicaciones = ubicacionDao.getAll() as List<Ubicacion>
                        activity?.runOnUiThread {
                            val adapter = UbicacionAdapter(appContext, ubicaciones)
                            listView.adapter = adapter
                        }
                    }
                }
            }
        }


        // Inflate the layout for this fragment
        return vista
    }

}