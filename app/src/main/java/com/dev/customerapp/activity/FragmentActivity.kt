package com.dev.customerapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.customerapp.R
import com.dev.customerapp.fragments.AddCustomerFragment
import com.dev.customerapp.fragments.AddEmployeeFragment
import com.dev.customerapp.fragments.AddProductCategoryFragment
import com.dev.customerapp.fragments.AddVendorFragment
import com.dev.customerapp.fragments.CreateIDProofFragment
import com.dev.customerapp.fragments.CustomerDetailsFragment
import com.dev.customerapp.fragments.CustomerListFragment
import com.dev.customerapp.fragments.EmployeeDetailsFragment
import com.dev.customerapp.fragments.EmployeeListFragment
import com.dev.customerapp.fragments.EmployeeStatusFragment
import com.dev.customerapp.fragments.UserListFragment
import com.dev.customerapp.fragments.VendorDetailsFragment
import com.dev.customerapp.fragments.VendorListFragment
import com.dev.customerapp.fragments.WebViewFragment

class FragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)

        val fragmentType = intent.getStringExtra("fragment_type")

        when (fragmentType) {
            "idProof" -> changeFragment(CreateIDProofFragment())
            "customer" -> changeFragment(AddCustomerFragment())
            "vendor" -> changeFragment(AddVendorFragment())
            "employee" -> changeFragment(AddEmployeeFragment())
            "manage_employee_status" -> changeFragment(EmployeeStatusFragment())
            "userList" -> changeFragment(UserListFragment())
            "employeeCustomerList" -> changeFragment(CustomerListFragment())
            "employeeVendorList" -> changeFragment(VendorListFragment())
            "add_product_category" -> changeFragment(AddProductCategoryFragment())
            "customerDetails" -> {
                val customerId = intent.getIntExtra("customerId",0)
                changeFragment(CustomerDetailsFragment(customerId))
            }
            "vendorDetails" -> {
                val vendorId = intent.getIntExtra("vendorId",0)
                changeFragment(VendorDetailsFragment(vendorId))
            }
            "employeeDetails" -> {
                val employeeId = intent.getIntExtra("employeeId",0)
                changeFragment(EmployeeDetailsFragment(employeeId))
            }
            "contact-us" -> changeFragment(WebViewFragment("https://www.aasthagroups.com/contact-us/"))
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
