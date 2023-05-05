package cr.ac.una.gps

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 * Use the [configFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class configFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var btnEnviarEtiqueta: Button
    lateinit var etiqueta: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista: View = inflater.inflate(R.layout.fragment_config, container, false)
        etiqueta = vista.findViewById(R.id.etiqueta)
        btnEnviarEtiqueta = vista.findViewById(R.id.Colocar_etiqueta)

        this.btnEnviarEtiqueta.setOnClickListener {
            Log.d(LOG_TAG, etiqueta.text.toString())
            Log.d(LOG_TAG, "seleccione el boton")

            var l = (activity as MainActivity)
            l.etiqueta = etiqueta.text.toString()

            Log.d(LOG_TAG, (activity as MainActivity).etiqueta)


        }

        // Inflate the layout for this fragment
        return vista
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment configFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            configFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}