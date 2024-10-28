package com.dev.customerapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.customerapp.R
import com.dev.customerapp.fragments.AccountFragment
import com.dev.customerapp.fragments.AddCustomerFragment
import com.dev.customerapp.fragments.AddVendorFragment
import com.dev.customerapp.fragments.CreateIDProofFragment

class ChangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)

        val fragmentType = intent.getStringExtra("fragment_type")

        when (fragmentType) {
            "idProof" -> changeFragment(CreateIDProofFragment())
            "customer" -> changeFragment(AddCustomerFragment())
            "vendor" -> changeFragment(AddVendorFragment())
            else -> changeFragment(AccountFragment())
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
