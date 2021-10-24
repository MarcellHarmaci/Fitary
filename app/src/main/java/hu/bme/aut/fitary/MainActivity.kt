package hu.bme.aut.fitary

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.fitary.ui.editWorkout.EditWorkoutFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.nav_edit_or_create_workout -> {
                    if (arguments?.getString("workout_id") != null)
                        destination.label = "Edit workout"
                    else
                        destination.label = "Create workout"
                }
            }
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_social_workouts,
                R.id.nav_user_workouts,
                R.id.nav_charts,
                R.id.nav_user_profile
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            setFloatingActionButtonVisible(false)
            val bundle = Bundle().apply {
                putInt("purpose", EditWorkoutFragment.Purpose.CREATE_WORKOUT)
            }
            navController.navigate(R.id.nav_edit_or_create_workout, bundle)
        }
    }

    fun setFloatingActionButtonVisible(isVisible: Boolean) {
        when (isVisible) {
            true -> fab.visibility = View.VISIBLE
            false -> fab.visibility = View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}