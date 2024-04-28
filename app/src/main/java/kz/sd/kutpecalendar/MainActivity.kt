package kz.sd.kutpecalendar

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import kz.sd.kutpecalendar.databinding.ActivityMainBinding
import kz.sd.kutpecalendar.utils.BottomNavigationViewListener


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationViewListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController


        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

    }

    override fun showBottomNavigationView(show: Boolean) {
        if (show) {
            binding.bottomNavigation.visibility = View.VISIBLE
        } else {
            binding.bottomNavigation.visibility = View.GONE
        }
    }
}