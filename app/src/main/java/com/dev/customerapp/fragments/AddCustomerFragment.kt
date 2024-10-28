package com.dev.customerapp.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.dev.customerapp.R
import com.dev.customerapp.activity.MainActivity
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.api.ApiService
import com.dev.customerapp.databinding.FragmentAddCustomerBinding
import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.utils.Constant
import com.dev.customerapp.utils.ResponseHandler
import com.dev.customerapp.utils.showErrorToast
import com.dev.customerapp.utils.showSuccessToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class AddCustomerFragment : Fragment() {
    private lateinit var binding: FragmentAddCustomerBinding
    private lateinit var apiService: ApiService
    private var progressDialog: Dialog? = null
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCustomerBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        apiService = ApiClient.getRetrofitInstance()
        initDatePicker()
        binding.editTextDOB.setOnClickListener {
            openDatePicker(it)
        }

        binding.customerSubmitButton.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val dob = binding.editTextDOB.text.toString().trim()
            val mobile = binding.editTextMobile.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val address = binding.editTextAddress.text.toString().trim()
            val houseNo = binding.editTextHouseNo.text.toString().trim()
            val locality = binding.editTextLocality.text.toString().trim()
            val block = binding.editTextBlock.text.toString().trim()
            val district = binding.editTextDistrict.text.toString().trim()
            val pinCode = binding.editTextPinCode.text.toString().trim()


            if (name.isEmpty()) {
                binding.editTextName.requestFocus()
                binding.editTextName.error = "Enter Name."
                return@setOnClickListener
            }

            if (dob.isEmpty()) {
                binding.editTextDOB.requestFocus()
                binding.editTextDOB.error = "Enter DOB."
                return@setOnClickListener
            }

            if (mobile.isEmpty() || !mobile.matches(Regex("\\d{10}"))) {
                binding.editTextMobile.requestFocus()
                binding.editTextMobile.error = "Enter a valid 10-digit mobile number."
                return@setOnClickListener
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                binding.editTextEmail.requestFocus()
                binding.editTextEmail.error = "Enter a valid email address."
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                binding.editTextAddress.requestFocus()
                binding.editTextAddress.error = "Enter Address."
                return@setOnClickListener
            }

            if (houseNo.isEmpty()) {
                binding.editTextHouseNo.requestFocus()
                binding.editTextHouseNo.error = "Enter House Number."
                return@setOnClickListener
            }

            if (locality.isEmpty()) {
                binding.editTextLocality.requestFocus()
                binding.editTextLocality.error = "Enter Locality."
                return@setOnClickListener
            }

            if (block.isEmpty()) {
                binding.editTextBlock.requestFocus()
                binding.editTextBlock.error = "Enter Block."
                return@setOnClickListener
            }

            if (district.isEmpty()) {
                binding.editTextDistrict.requestFocus()
                binding.editTextDistrict.error = "Enter District."
                return@setOnClickListener
            }

            if (pinCode.isEmpty() || !pinCode.matches(Regex("\\d{6}"))) {
                binding.editTextPinCode.requestFocus()
                binding.editTextPinCode.error = "Enter a valid 6-digit PinCode."
                return@setOnClickListener
            }

            val customer = CustomerModel(
                name,
                dob,
                mobile,
                email,
                address,
                houseNo,
                locality,
                block,
                district,
                pinCode.toInt(),
                Constant(requireContext()).getUserData()?.userId.toString(),
                ""
            )
            showProgressDialog(true)
            val call: Call<ResponseHandler<List<CustomerModel>>> = apiService.addCustomer(customer)
            call.enqueue(object : Callback<ResponseHandler<List<CustomerModel>>> {
                override fun onResponse(
                    call: Call<ResponseHandler<List<CustomerModel>>>,
                    response: Response<ResponseHandler<List<CustomerModel>>>
                ) {
                    showProgressDialog(false)
                    if (response.isSuccessful && response.body() != null) {
                        val responseHandler = response.body()
                        val code = responseHandler?.code
                        val message = responseHandler?.message
                        if (code == 200) {

                            requireContext().showSuccessToast(message.toString())
                            requireActivity().onBackPressed()
                        }
                        if (code == 201) {
                            requireContext().showErrorToast(message.toString())
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ResponseHandler<List<CustomerModel>>>,
                    t: Throwable
                ) {
                    showProgressDialog(false)
                    requireContext().showErrorToast(t.message.toString())
                }

            })

        }
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

    private fun openDatePicker(view: View) {
        datePickerDialog.show()
    }

    private fun initDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = selectedMonth + 1
                val date = makeDateString(selectedDay, formattedMonth, selectedYear)
                binding.editTextDOB.setText(date)
            }, year, month, day
        )
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "$year-$month-$day"
    }


}