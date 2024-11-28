package com.dev.customerapp.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.api.ApiService
import com.dev.customerapp.databinding.FragmentAddEmployeeBinding
import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.response.CreateEmployeeData
import com.dev.customerapp.response.CreateUserData
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class AddEmployeeFragment : Fragment() {

    private lateinit var binding: FragmentAddEmployeeBinding
    private lateinit var apiService: ApiService
    private var progressDialog: Dialog? = null
    private lateinit var createEmployeeData: CreateEmployeeData
    private val userData by lazy {
        Constant(requireContext()).getUserData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEmployeeBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiClient.getRetrofitInstance()

        binding.employeeEditTextDOB.setOnClickListener()
        {
            datePickerDialog.show()
        }

        binding.employeeSubmitButton.setOnClickListener {
            val name = binding.employeeNameEditText.text.toString().trim()
            val dob = binding.employeeEditTextDOB.text.toString().trim()
            val mobile = binding.employeeEditTextMobile.text.toString().trim()
            val email = binding.employeeEditTextEmail.text.toString().trim()
            val address = binding.employeeEditTextAddress.text.toString().trim()
            val houseNo = binding.employeeEditTextHouseNo.text.toString().trim()
            val locality = binding.employeeEditTextLocality.text.toString().trim()
            val block = binding.employeeEditTextBlock.text.toString().trim()
            val district = binding.employeeEditTextDistrict.text.toString().trim()
            val pinCode = binding.employeeEditTextPinCode.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (name.isEmpty()) {
                binding.employeeNameEditText.text.toString().trim()
                binding.employeeNameEditText.error = "Enter Name."
                return@setOnClickListener
            }

            if (dob.isEmpty()) {
                binding.employeeEditTextDOB.requestFocus()
                binding.employeeEditTextDOB.error = "Enter DOB."
                return@setOnClickListener
            }

            if (mobile.isEmpty() || !mobile.matches(Regex("\\d{10}"))) {
                binding.employeeEditTextMobile.requestFocus()
                binding.employeeEditTextMobile.error = "Enter a valid 10-digit mobile number."
                return@setOnClickListener
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                binding.employeeEditTextEmail.requestFocus()
                binding.employeeEditTextEmail.error = "Enter a valid email address."
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                binding.employeeEditTextAddress.requestFocus()
                binding.employeeEditTextAddress.error = "Enter Address."
                return@setOnClickListener
            }

            if (houseNo.isEmpty()) {
                binding.employeeEditTextHouseNo.requestFocus()
                binding.employeeEditTextHouseNo.error = "Enter House Number."
                return@setOnClickListener
            }

            if (locality.isEmpty()) {
                binding.employeeEditTextLocality.requestFocus()
                binding.employeeEditTextLocality.error = "Enter Locality."
                return@setOnClickListener
            }

            if (block.isEmpty()) {
                binding.employeeEditTextBlock.requestFocus()
                binding.employeeEditTextBlock.error = "Enter Block."
                return@setOnClickListener
            }

            if (district.isEmpty()) {
                binding.employeeEditTextDistrict.requestFocus()
                binding.employeeEditTextDistrict.error = "Enter District."
                return@setOnClickListener
            }

            if (pinCode.isEmpty() || !pinCode.matches(Regex("\\d{6}"))) {
                binding.employeeEditTextPinCode.requestFocus()
                binding.employeeEditTextPinCode.error = "Enter a valid 6-digit PinCode."
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6) {
                binding.passwordEditText.requestFocus()
                binding.passwordEditText.error = "Enter a Password With 6 Digits"
                return@setOnClickListener
            }
            showProgressDialog(true)
            val employee = EmployeeModel(
                0,
                name,
                dob,
                mobile,
                email,
                address,
                houseNo,
                locality,
                userData!!.blockId!!,
                userData!!.districtId!!,
                pinCode.toInt(),
                password,
                Constant(requireContext()).getUserData()!!.userId
            )

            val call: Call<CommonResponse<List<EmployeeModel>>> = apiService.addEmployee(employee)
            call.enqueue(object : Callback<CommonResponse<List<EmployeeModel>>> {
                override fun onResponse(
                    call: Call<CommonResponse<List<EmployeeModel>>>,
                    response: Response<CommonResponse<List<EmployeeModel>>>
                ) {
                    showProgressDialog(false)
                    if (response.isSuccessful && response.body() != null) {
                        val responseHandler = response.body()
                        val code = responseHandler?.code
                        val message = responseHandler?.message
                        if (code == 200) {
                            requireContext().showSuccessToast(message.toString())
                            requireActivity().onBackPressed()
                        } else {
                            requireContext().showErrorToast(message.toString())
                        }
                    } else {
                        requireContext().showErrorToast("Error While Creating Employee")
                    }
                }

                override fun onFailure(
                    call: Call<CommonResponse<List<EmployeeModel>>>,
                    t: Throwable
                ) {
                    showProgressDialog(false)
                    requireContext().showErrorToast(t.message.toString())

                }
            })

        }

        getCreateFromData()

    }

    private fun showProgressDialog(show: Boolean) {
        if (show) {
            if (progressDialog == null) {
                progressDialog = Dialog(requireContext())
                progressDialog!!.setContentView(R.layout.dialog_progress_bar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setCanceledOnTouchOutside(false)
            }
            if (!isFinishing()) {
                progressDialog!!.show()
            }
        } else {
            if (progressDialog != null && progressDialog!!.isShowing()) {
                if (!isFinishing()) {
                    progressDialog!!.dismiss()
                }
            }
        }
    }

    private fun isFinishing(): Boolean {
        return activity?.isFinishing == true
    }


    private val datePickerDialog by lazy {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = selectedMonth + 1
                val date = makeDateString(selectedDay, formattedMonth, selectedYear)
                binding.employeeEditTextDOB.setText(date)
            },
            year,
            month,
            day
        )
    }

    private fun getCreateFromData() {

        ApiClient.getRetrofitInstance().createEmployeeData(
            userData!!.districtId!!,
            userData!!.blockId!!
        ).enqueue(object : Callback<CommonResponse<CreateEmployeeData>> {


            override fun onResponse(
                call: Call<CommonResponse<CreateEmployeeData>>,
                response: Response<CommonResponse<CreateEmployeeData>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.code == 200) {

                        createEmployeeData = responseBody.data

                        binding.employeeEditTextBlock.setText(createEmployeeData.blockName.toString())
                        binding.employeeEditTextDistrict.setText(createEmployeeData.districtName.toString())

                    } else {
                        requireContext().showErrorToast(responseBody!!.message)
                    }
                } else {
                    requireContext().showErrorToast("Error While Getting State List ")
                }
            }

            override fun onFailure(call: Call<CommonResponse<CreateEmployeeData>>, t: Throwable) {
                requireContext().showErrorToast(t.message.toString())
            }

        })

    }


    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "$year-$month-$day"
    }


}