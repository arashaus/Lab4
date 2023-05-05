package cr.ac.una.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.PackageManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import cr.ac.una.gps.entity.Ubicacion
import cr.ac.una.gps.entity.UbicacionPoligono
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationReceiver: BroadcastReceiver
    lateinit var ubicaciones: List<Ubicacion>
    lateinit var entity: Ubicacion
    private var locations = mutableListOf<LatLng>()
    lateinit var ubicacionesPoligono: List<UbicacionPoligono>


    private lateinit var polygon: Polygon

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        createPolygon()

        val filtroUbicacion = (activity as MainActivity).filtroUbicacion
        filtrarMarcadores(filtroUbicacion)
        getLocation()

    }


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        iniciaServicio()
        locationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val latitud = intent?.getDoubleExtra("latitud", 0.0) ?: 0.0
                val longitud = intent?.getDoubleExtra("longitud", 0.0) ?: 0.0
                getLocation()
                println(latitud.toString() + "    " + longitud)
            }
        }
        context?.registerReceiver(locationReceiver, IntentFilter("ubicacionActualizada"))

    }

    override fun onResume() {
        super.onResume()
        // Registrar el receptor para recibir actualizaciones de ubicación
        context?.registerReceiver(locationReceiver, IntentFilter("ubicacionActualizada"))
    }

    override fun onPause() {
        super.onPause()
        // Desregistrar el receptor al pausar el fragmento
        context?.unregisterReceiver(locationReceiver)
    }


    @SuppressLint("RestrictedApi")
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->


                // Ubicación obtenida con éxito
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val etiqueta = (activity as MainActivity).etiqueta

                    crearMarker(currentLatLng, etiqueta)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 3f))
                    map.uiSettings.isZoomControlsEnabled = true

                    val l = (activity as MainActivity)
                    entity = Ubicacion(
                        id = null,
                        latitud = currentLatLng.latitude,
                        longitud = currentLatLng.longitude,
                        fecha = Date(),
                        inPoligono = isLocationInsidePolygon(currentLatLng)
                    )
                    l.insertEntity(entity)

                    Log.d(PackageManagerCompat.LOG_TAG, "La ubicacion que va a la base de datos es")
                    Log.d(PackageManagerCompat.LOG_TAG, entity.toString())


                }//final del if dentro del else
            }//final del listener
        }// final de else
    }//final de getlocation

    fun crearMarker(currentLatLng: LatLng, etiqueta: String) {
        val inPoligono = isLocationInsidePolygon(currentLatLng)
        if (inPoligono) {
            map.addMarker(
                MarkerOptions().position(currentLatLng).title(etiqueta)
                    .icon(BitmapDescriptorFactory.defaultMarker(210.0F))
            )
                ?.showInfoWindow()
        } else {
            map.addMarker(MarkerOptions().position(currentLatLng).title(etiqueta))
                ?.showInfoWindow()
        }
    }

    fun filtrarMarcadores(buscar: String) {
        val ubicacionDao = (activity as MainActivity).ubicacionDao

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (buscar != "") {
                    val dateEnd = Date(buscar)
                    val dateStart = Date(buscar)
                    dateEnd.hours = 23
                    dateEnd.minutes = 59
                    println("se puede buscar")
                    ubicaciones = ubicacionDao.get(dateStart, dateEnd) as List<Ubicacion>
                } else {
                    ubicaciones = ubicacionDao.getAll() as List<Ubicacion>
                }

                for (i in ubicaciones.indices) {
                    val currentLatLng = LatLng(ubicaciones[i].latitud, ubicaciones[i].longitud)
                    locations.add(currentLatLng)
                }
                locations = locations.distinct().toList().toMutableList()

                activity?.runOnUiThread {
                    if (locations.size > 0) {
                        for (i in locations.indices) {
                            println("***************************************** Creando marcadores **************************************************")
                            val currentLatLng =
                                LatLng(locations[i].latitude, locations[i].longitude)
                            crearMarker(currentLatLng, "")
                        }
                    }
                }
            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    iniciaServicio()
                    // getLocation()
                }
            } else {
                // Permiso denegado, maneja la situación de acuerdo a tus necesidades
            }
        }
    }

    private fun iniciaServicio() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            val intent = Intent(context, LocationService::class.java)
            context?.startService(intent)
        }
    } // fin del inicia servicio


    // estos son los puntos que se sacaron de
    // https://www.google.com/maps/d/edit?hl=es&mid=1Qbw3vQWcb2k8GwN7Ibg8eAuaCOfyLCE&ll=9.618114789051308%2C-84.2363965&z=8
    private fun createPolygon() {
       val polygonOptions = PolygonOptions()
        val ubicacionPoligonoDao = (activity as MainActivity).ubicacionPoligonoDao
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ubicacionesPoligono = ubicacionPoligonoDao.getAll() as List<UbicacionPoligono>
                if (ubicacionesPoligono.isNotEmpty()) {
                    for (i in ubicacionesPoligono.indices) {
                        println("--------------------------------------------------------")
                        polygonOptions.add(
                            LatLng(
                                ubicacionesPoligono[i].latitudPoligono,
                                ubicacionesPoligono[i].longitudPoligono
                            )
                        )
                    }
                }
                activity?.runOnUiThread {
                    setearPolygon(map.addPolygon(polygonOptions))
                }
            }
        }
    }

    fun setearPolygon(poli : Polygon){
        polygon = poli
    }





    private fun isLocationInsidePolygon(location: LatLng): Boolean {
        return polygon != null && PolyUtil.containsLocation(location, polygon?.points, true)
    }




    //*******************************************************





}//end