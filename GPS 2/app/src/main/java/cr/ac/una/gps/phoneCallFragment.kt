package cr.ac.una.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [phoneCallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class phoneCallFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var param2: String? = null
    lateinit var vista: View
    lateinit var btnllamar: Button
    lateinit var phoneNumber: String


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
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_phone_call, container, false)
        btnllamar = vista.findViewById(R.id.call_button)
        phoneNumber = vista.findViewById<EditText>(R.id.phone_number).text.toString()

        /*btnllamar.setOnClickListener {
            Log.d(PackageManagerCompat.LOG_TAG, "selecciono el boton")
            makePhoneCall2(vista)
        }*/

        return vista
    }

    fun makePhoneCall2(view: View) {
        println("genere la llamada 1")
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        } else {
            println("genere la llamada")
            makePhoneCall2()
        }
    }


    fun makePhoneCall2() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (1) {
            requestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall2()
                } else {
                    // El usuario no concedió el permiso
                }
            }
        }
    }


}