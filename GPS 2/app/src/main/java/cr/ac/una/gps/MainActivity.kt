package cr.ac.una.gps

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import cr.ac.una.gps.entity.Ubicacion
import cr.ac.una.gps.entity.UbicacionPoligono
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    lateinit var etiqueta: String
    lateinit var filtroUbicacion: String
    lateinit var ubicacionDao: UbicacionDao
    lateinit var ubicaciones: List<Ubicacion>

    lateinit var ubicacionPoligonoDao: UbicacionPoligonoDao
    lateinit var ubicacionesPoligono: List<UbicacionPoligono>

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ubicacionDao = AppDatabase.getInstance(this).ubicacionDao()
        ubicacionPoligonoDao = AppDatabasePoligono.getInstance(this).ubicacionUbicacionPoligonoDao()

        etiqueta = "ubicacion actual"
        filtroUbicacion = ""
        ubicaciones = emptyList()
        ubicacionesPoligono = emptyList()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        // para vaciar la base de datos
        /*lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ubicaciones = ubicacionDao.getAll() as List<Ubicacion>
                for (i in ubicaciones.indices) {
                    ubicacionDao.delete(ubicaciones[i])
                    println("********************************************borrando***********************************************")
                }
            }
        }*/

        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)


    }//fin del onCreate

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { // cuando alguien selecciono un item del menu
        val title: Int
        lateinit var fragment: Fragment

        when (item.getItemId()) {

            R.id.nav_acercaDe -> {
                title = R.string.menu_AcercaDe // si el selecciona home, haga
                fragment = HomeFragment.newInstance(getString(title)) // cargamos el fragmento
            }

            R.id.nav_maps -> {
                title = R.string.menu_maps  // si el selecciona maps, haga
                fragment = MapsFragment()   // cargamos el fragmento con el contenido de maps
            }

            R.id.nav_tools -> {
                title = R.string.menu_tools// si el selecciona maps, haga
                fragment = configFragment()               // cargamos el fragmento
            }

            R.id.nav_locations -> {
                title = R.string.menu_locations
                fragment = LocationFragment()
            }

            R.id.nav_poligono -> {
                title = R.string.menu_poligono
                fragment = restrictedAreaFragment()
            }

            R.id.nav_phone_call->{
                title = R.string.menu_phone_call
                fragment = phoneCallFragment()
            }


            else -> throw IllegalArgumentException("menu option not implemented!!")
        }

        supportFragmentManager.beginTransaction().replace(R.id.home_content, fragment).commit()
        setTitle(getString(title))
        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    fun insertEntityPoligono(entity: UbicacionPoligono) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ubicacionPoligonoDao.insert(entity)
            }
        }
    }

    fun insertEntity(entity: Ubicacion) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ubicacionDao.insert(entity)
            }
        }
    }
}