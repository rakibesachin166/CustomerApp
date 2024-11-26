package com.dev.customerapp.activity;


import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.ActivityMainBinding
import com.dev.customerapp.fragments.AccountFragment
import com.dev.customerapp.fragments.AddLocationFragment
import com.dev.customerapp.fragments.CategoriesFragment
import com.dev.customerapp.fragments.HomeFragment
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.response.AgreementResponse
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.changeActivity
import com.dev.customerapp.utils.loadImage
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : BaseActivity() {

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
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "customer")
                    startActivity(intent)

                }

                R.id.navigation_add_vendor -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "vendor")
                    startActivity(intent)
                }

                R.id.navigation_agreement -> {
                    val userDataModel = Constant(this@MainActivity).getUserData()

                    if (userDataModel != null) {
                        agreementDialog()
                    }

                }

                R.id.navigation_addLocation -> {
                    changeFragment(AddLocationFragment())

                }

                R.id.navigation_add_product_category -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "add_product_category")
                    startActivity(intent)

                }

                R.id.navigation_add_employee -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "employee")
                    startActivity(intent)
                }

                R.id.navigation_manage_employee_status -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "manage_employee_status")
                    startActivity(intent)
                }

                R.id.navigation_user_list -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "userList")
                    startActivity(intent)
                }

                R.id.navigation_contact_us -> {
                    val intent = Intent(this, FragmentActivity::class.java)
                    intent.putExtra("fragment_type", "contact-us")
                    startActivity(intent)
                }

                R.id.navigationLogOut -> {
                    bottomSheet.show()
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
                val userData = Constant(this@MainActivity).getUserData()
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

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
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
            recreate()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog
    }

    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setUpHomePage(userData: UserDataModel?) {

        val headerButton: TextView = headerView.findViewById(R.id.tvSignIn)

        val menu = binding?.navigationView?.menu
        if (userData == null) {
            headerButton.setOnClickListener {
                changeActivity(LoginActivity::class.java, true)
            }
            headerView.setOnClickListener(null)

            menu?.findItem(R.id.navigation_add_user)?.isVisible = false
            menu?.findItem(R.id.navigation_add_vendor)?.isVisible = false
            menu?.findItem(R.id.navigation_add_customer)?.isVisible = false
            menu?.findItem(R.id.navigation_agreement)?.isVisible = false
            menu?.findItem(R.id.navigation_addLocation)?.isVisible = false
            menu?.findItem(R.id.navigationLogOut)?.isVisible = false
            menu?.findItem(R.id.navigation_user_list)?.isVisible = false
            menu?.findItem(R.id.navigation_add_product_category)?.isVisible = false
            menu?.findItem(R.id.navigation_add_employee)?.isVisible = false
            menu?.findItem(R.id.navigation_manage_employee_status)?.isVisible = false

        }
        else {
            headerView.setOnClickListener {
                val intent = Intent(this@MainActivity, UserDetailsActivity::class.java)
                intent.putExtra("userId", userData.userId)
                startActivity(intent)
            }
            menu?.findItem(R.id.navigationLogOut)?.isVisible = true
            headerButton.background = null
            headerButton.text = userData.userName
            val profileImage: CircleImageView = headerView.findViewById(R.id.profile_image)
            profileImage.loadImage(ApiClient.BASE_URL + userData.userPhoto)
            when (userData.userType) {
                //Admin
                1 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_vendor)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = false
                    menu?.findItem(R.id.navigation_add_product_category)?.isVisible = true
                    menu?.findItem(R.id.navigation_addLocation)?.isVisible = true
                    menu?.findItem(R.id.navigation_user_list)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_employee)?.isVisible = false
                    menu?.findItem(R.id.navigation_manage_employee_status)?.isVisible = true
                }
                //State User
                2 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = false
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = true
                    menu?.findItem(R.id.navigation_user_list)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_product_category)?.isVisible = false
                }
                //Division User
                3 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = false
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = true
                    menu?.findItem(R.id.navigation_user_list)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_product_category)?.isVisible = false
                }
                //District User
                4 -> {
                    menu?.findItem(R.id.navigation_add_user)?.isVisible = false
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = true
                    menu?.findItem(R.id.navigation_user_list)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_product_category)?.isVisible = false
                }
                //Block User
                5 -> {
                    menu?.findItem(R.id.navigation_add_employee)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = true
                    menu?.findItem(R.id.navigation_user_list)?.isVisible = false
                    menu?.findItem(R.id.navigation_add_product_category)?.isVisible = false
                }
                //Employee User
                6 -> {
                    menu?.findItem(R.id.navigation_add_vendor)?.isVisible = true
                    menu?.findItem(R.id.navigation_add_customer)?.isVisible = true
                    menu?.findItem(R.id.navigation_agreement)?.isVisible = false
                    menu?.findItem(R.id.navigation_user_list)?.isVisible = false
                    menu?.findItem(R.id.navigation_add_product_category)?.isVisible = false
                }

                else -> {
                    println("Unknown user type")
                }
            }

        }


    }

    override fun onResume() {
        super.onResume()

        when (Constant(this@MainActivity).getLoginType()) {
            1 -> {
                val userData = Constant(this@MainActivity).getUserData()
                setUpHomePage(userData)
                getUserProfile()
            }

            2 -> {
                // $type = 2;  // Employee
                Constant(this@MainActivity).getEmployeeData()
                changeActivity(EmployeeMainActivity::class.java, false)
            }

            3 -> {
                //   $type = 3;  // Vendor
                Constant(this@MainActivity).getVendorData()
                changeActivity(VendorMainActivity::class.java, false)
            }

            4 -> {
                // $type = 4;  // Customer
                Constant(this@MainActivity).getCustomerData()
                changeActivity(CustomerMainActivity::class.java, false)
            }
            else -> {
                val userData = Constant(this@MainActivity).getUserData()
                setUpHomePage(userData)
                getUserProfile()
            }
        }

    }

    private fun getUserProfile() {

        val userData = Constant(this@MainActivity).getUserData()

        if (userData != null) {
            ApiClient.getRetrofitInstance().getUserHomeProfile(userData.userId)
                .enqueue(object : retrofit2.Callback<CommonResponse<UserDataModel>> {
                    override fun onResponse(
                        call: Call<CommonResponse<UserDataModel>>,
                        response: Response<CommonResponse<UserDataModel>>
                    ) {

                        progressDialog.dismiss()
                        val responseBody = response.body()
                        if (responseBody != null) {

                            if (responseBody.code == 200) {
                                Constant(this@MainActivity).saveUserData(responseBody.data)
                                setUpHomePage(responseBody.data)
                            } else if (responseBody.code == 201) {
                                Constant(this@MainActivity).clearUserData()
                                recreate()
                            }

                        } else {
                            showErrorToast("Failed To Get Agreement . Please Try Again.")
                        }

                    }

                    override fun onFailure(
                        call: Call<CommonResponse<UserDataModel>>,
                        t: Throwable
                    ) {
                        showErrorToast("Failed To Get Agreement ." + t.message.toString())
                    }

                })
        }

    }

    private val progressDialog: Dialog by lazy {
        ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
    }

    private fun agreementDialog() {


        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

        val dialogView = layoutInflater.inflate(R.layout.dialog_agreement, null)
        dialog.setContentView(dialogView)

        val webView: WebView = dialogView.findViewById(R.id.dialogWebView)
        webView.settings.setJavaScriptEnabled(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        val agreeButton: AppCompatButton =
            dialogView.findViewById(com.dev.customerapp.R.id.agreeButton)
        webView.webViewClient = WebViewClient()


//        val htmlContent =
//            assets.open("AASTHAGROUPSAGREEMENT.html").bufferedReader().use { it.readText() }
//        val modifiedContent = htmlContent
//            .replace("[User Creation Date]", date)
//            .replace("[User Name]", name)
//            .replace("[User Role]", role)
//            .replace("[User Complete Address]", address)
        val userData = Constant(this@MainActivity).getUserData() ?: return
        progressDialog.show()
        ApiClient.getRetrofitInstance().getAgreementData(userData.userId)
            .enqueue(object : retrofit2.Callback<CommonResponse<AgreementResponse>> {
                override fun onResponse(
                    call: Call<CommonResponse<AgreementResponse>>,
                    response: Response<CommonResponse<AgreementResponse>>
                ) {

                    progressDialog.dismiss()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        agreeButton.setOnClickListener {
                            dialog.dismiss()
                        }

                        progressDialog.dismiss()

                        if (responseBody.code == 200) {
                            var agreement = " "

                            if (responseBody.data.isAccepted == 0) {
                                agreement = responseBody.data.agreement!!
                                //Not accepted
                                webView.loadData(agreement, "text/html; charset=utf-8", "UTF-8")
                                agreeButton.visibility = View.VISIBLE

                            } else {
                                agreement = responseBody.data.agreementData!!.agreement
                                //Already Accepted
                                webView.loadData(agreement, "text/html; charset=utf-8", "UTF-8")
                                agreeButton.visibility = View.GONE
                            }
                            agreeButton.setOnClickListener {

                                progressDialog.show()

                                ApiClient.getRetrofitInstance()
                                    .acceptAgreement(userData.userId, agreement)
                                    .enqueue(object : retrofit2.Callback<CommonResponse<String>> {

                                        override fun onResponse(
                                            call: Call<CommonResponse<String>>,
                                            agreeResponse: Response<CommonResponse<String>>
                                        ) {
                                            progressDialog.dismiss()
                                            val agreeBody = agreeResponse.body()
                                            if (agreeBody?.code == 200) {
                                                showSuccessToast(agreeBody.message)
                                                dialog.dismiss()
                                            } else {
                                                showErrorToast(agreeBody?.message.toString())
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<CommonResponse<String>>,
                                            t: Throwable
                                        ) {
                                            progressDialog.dismiss()

                                            showErrorToast("Failed To Accept Agreement . Please Try Again.")
                                        }

                                    })


                            }
                        }
                    } else {
                        showErrorToast("Failed To Get Agreement . Please Try Again.")
                    }

                }

                override fun onFailure(
                    call: Call<CommonResponse<AgreementResponse>>,
                    t: Throwable
                ) {
                    dialog.dismiss()
                    progressDialog.dismiss()
                    showErrorToast("Failed To Get Agreement ." + t.message.toString())

                }

            })

        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@MainActivity,
                android.R.color.black
            )
        )
        dialog.show()

    }
}
