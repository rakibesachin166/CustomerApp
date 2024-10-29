package com.dev.customerapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.customerapp.R
import com.dev.customerapp.fragments.AccountFragment
import com.dev.customerapp.fragments.AddCustomerFragment
import com.dev.customerapp.fragments.AddEmployeeFragment
import com.dev.customerapp.fragments.AddVendorFragment
import com.dev.customerapp.fragments.CategoriesFragment
import com.dev.customerapp.fragments.CreateIDProofFragment
import com.dev.customerapp.fragments.UserListFragment

class ChangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)

        val fragmentType = intent.getStringExtra("fragment_type")

        when (fragmentType) {
            "idProof" -> changeFragment(CreateIDProofFragment())
            "customer" -> changeFragment(AddCustomerFragment())
            "vendor" -> changeFragment(AddVendorFragment())
            "employee" -> changeFragment(AddEmployeeFragment())
            "userList" -> changeFragment(UserListFragment())
            else -> {}
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val fragments = fragmentManager.backStackEntryCount

        if (fragments == 1) {
            finish()
        } else if (fragments > 1) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
