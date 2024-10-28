package com.dev.customerapp.activity;


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.ActivityMainBinding
import com.dev.customerapp.fragments.AccountFragment
import com.dev.customerapp.fragments.CategoriesFragment
import com.dev.customerapp.fragments.AddLocationFragment
import com.dev.customerapp.fragments.HomeFragment
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.FunctionsConstant
import com.dev.customerapp.utils.changeActivity
import com.dev.customerapp.utils.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

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
                    val intent = Intent(this, ChangeActivity::class.java)
                    intent.putExtra("fragment_type", "customer")
                    startActivity(intent)
                }

                R.id.navigation_add_vendor -> {
                    val intent = Intent(this, ChangeActivity::class.java)
                    intent.putExtra("fragment_type", "vendor")
                    startActivity(intent)
                }

                R.id.navigation_agreement -> {
                    val userDataModel = Constant(this@MainActivity).getUserData()

                    if (userDataModel != null) {
                        fullScreenDialog(
                            "2024-10-28",
                            userDataModel.userName,
                            FunctionsConstant.getUserRoleName(userDataModel.userType)
                        )
                    }

                }
                R.id.navigation_addLocation -> {
                    changeFragment(AddLocationFragment())

                }

                R.id.navigation_add_employee -> {
                    val intent = Intent(this, ChangeActivity::class.java)
                    intent.putExtra("fragment_type", "employee")
                    startActivity(intent)
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

            R.id.bottom_categories -> {
                selectedFragment = CategoriesFragment()
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
            menu?.findItem(R.id.navigation_agreement)?.isVisible = false
            menu?.findItem(R.id.navigation_addLocation)?.isVisible = false
        } else {
            headerButton.background = null
            headerButton.text = userData.userName
            val profileImage: CircleImageView = headerView.findViewById(R.id.profile_image)
            profileImage.loadImage(ApiClient.BASE_URL + userData.userPhoto)

            when (userData.userType) {
                1 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_vendor)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = false
                    menu?.findItem(R.id.navigation_addLocation)?.isVisible = true
                }

                2 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = true
                }

                3 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = true
                }

                4 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = true
                }

                5 -> {

                    menu?.findItem(R.id.navigation_add_employee)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = true
                }

                6 -> {

                    menu?.findItem(R.id.navigation_add_vendor)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_customer)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = false
                }

                else -> {

                    println("Unknown user type")
                }
            }

        }


    }

    private fun fullScreenDialog(date: String, name: String, role: String) {


        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(com.dev.customerapp.R.layout.dialog_agreement, null)
        dialog.setContentView(dialogView)

        // Set up the WebView
        val webView: WebView = dialogView.findViewById(com.dev.customerapp.R.id.dialogWebView)
        val agreeButton: AppCompatButton =
            dialogView.findViewById(com.dev.customerapp.R.id.agreeButton)
        webView.webViewClient = WebViewClient()


        val htmlContent =
            assets.open("AASTHAGROUPSAGREEMENT.html").bufferedReader().use { it.readText() }
        val modifiedContent = htmlContent
            .replace("[User Creation Date]", date)
            .replace("[User Name]", name)
            .replace("[User Role]", role)

        webView.loadDataWithBaseURL(
            "file:///android_asset/",
            modifiedContent,
            "text/html",
            "UTF-8",
            null
        )
        agreeButton.setOnClickListener {
            dialog.dismiss()
            changeActivity(MainActivity::class.java, false)
        }

        dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.black))

        dialog.show()
    }
}
