package com.dev.customerapp.activity;


import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.ActivityMainBinding
import com.dev.customerapp.fragments.AccountFragment
import com.dev.customerapp.fragments.AddCustomerFragment
import com.dev.customerapp.fragments.AddVendorFragment
import com.dev.customerapp.fragments.HomeFragment
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.changeActivity
import com.dev.customerapp.utils.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        setSupportActionBar(binding?.toolbar)

        drawerLayout = binding?.drawerLayout!!
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding?.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        headerView = binding?.navigationView!!.getHeaderView(0)


        supportActionBar?.setDisplayShowTitleEnabled(false)
        actionBarDrawerToggle.syncState()

        binding?.navigationView!!.setNavigationItemSelectedListener { item ->
            closeDrawer()
            when (item.itemId) {
                R.id.navigation_add_user -> {
                    changeActivity(CreateUserActivity::class.java, false)
                }

                R.id.navigation_add_customer -> {
                    changeFragment(AddCustomerFragment())
                }

                R.id.navigation_add_vendor -> {
                    changeFragment(AddVendorFragment())
                }
            }
            false
        }

        if (savedInstanceState == null) {
            changeFragment(HomeFragment())
        }

        bottomNavigationView = binding?.bottomNavigation!!
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
        changeFragment(HomeFragment())
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.bottom_home -> {
                selectedFragment = HomeFragment()
            }

            R.id.bottom_account -> {
                selectedFragment = AccountFragment()
            }


        }
        selectedFragment?.let { changeFragment(it) }
        true
    }

    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        val userData = Constant(this@MainActivity).getUserData()
        val headerButton: TextView = headerView.findViewById(R.id.tvSignIn)

        val menu = binding?.navigationView?.menu
        if (userData == null) {
            headerButton.setOnClickListener {
                changeActivity(LoginActivity::class.java, true)
            }
            menu?.findItem(R.id.navigation_add_user)?.isVisible = false
            menu?.findItem(R.id.navigation_add_vendor)?.isVisible = false
            menu?.findItem(R.id.navigation_add_customer)?.isVisible = false
        } else {
            headerButton.background = null
            headerButton.text = userData.userName
            val profileImage: CircleImageView = headerView.findViewById(R.id.profile_image)
            profileImage.loadImage(ApiClient.BASE_URL + userData.userPhoto)
            when (userData.userType) {
                1 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                }
                2 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                }
                3 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                }
                4 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                }
                5 -> {
                    menu?.findItem(R.id.navigation_add_vendor)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_customer)?.isVisible = true
                }
                else -> {

                    println("Unknown user type")
                }
            }

        }





    }
}
