package com.dev.customerapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.dev.customerapp.R
import com.dev.customerapp.databinding.ActivityEmployeeMainBinding
import com.dev.customerapp.fragments.AccountFragment
import com.dev.customerapp.fragments.AddLocationFragment
import com.dev.customerapp.fragments.CategoriesFragment
import com.dev.customerapp.fragments.HomeFragment
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.changeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog

class EmployeeMainActivity : BaseActivity() {
    private lateinit var binding: ActivityEmployeeMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var header: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmployeeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        actionBarDrawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { item ->
            closeDrawer()
            when (item.itemId) {
                R.id.navigation_add_user -> {
                    changeActivity(CreateUserActivity::class.java, false)
                }

                R.id.navigation_add_customer -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "add_customer")
                    startActivity(intent)

                }

                R.id.navigation_employee_customer_list -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "employeeCustomerList")
                    startActivity(intent)
                }

                R.id.navigation_add_vendor -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "add_vendor")
                    startActivity(intent)
                }

                R.id.navigation_employee_vendor_list -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "employeeVendorList")
                    startActivity(intent)
                }

                R.id.navigation_addLocation -> {
                    changeFragment(AddLocationFragment())

                }


                R.id.navigationLogOut -> {
                    bottomSheet.show()
                }
            }
            false
        }
        header = binding.navigationView.getHeaderView(0)

        val userNameTxt: AppCompatTextView = header.findViewById(R.id.tvName)

        userNameTxt.text = Constant(this).getEmployeeData().employeeName

        if (savedInstanceState == null) {
            changeFragment(HomeFragment())
        }

        bottomNavigationView = binding.bottomNavigation
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
                val userData = Constant(this@EmployeeMainActivity).getUserData()
                if (userData == null) {
                    changeActivity(LoginActivity::class.java, true)

                } else {
                    selectedFragment = AccountFragment()
                }

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

    private val bottomSheet: BottomSheetDialog by lazy {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view: View =
            LayoutInflater.from(this).inflate(R.layout.dialog_logout, null)
        val logoutButton = view.findViewById<AppCompatButton>(R.id.logoutButton)
        val cancelButton = view.findViewById<AppCompatButton>(R.id.cancelButton)


        cancelButton.setOnClickListener {

            bottomSheetDialog.dismiss()
        }


        logoutButton.setOnClickListener {
            Constant(this).clearUserData()
            bottomSheetDialog.dismiss()
            changeActivity(LoginActivity::class.java, true)
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog
    }

}