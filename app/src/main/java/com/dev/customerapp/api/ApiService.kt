package com.dev.customerapp.api;

import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.CreateUserModel
import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.response.CreateUserData
import com.dev.customerapp.utils.ResponseHandler
import com.dev.customerapp.response.PhotoResponse
import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body;
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST;
import retrofit2.http.Part

interface ApiService {
    /*@POST("createUser")
    fun createUser(@Body userRequest: UserDataModel): Call<CommonResponse<UserDataModel>>*/


    @POST("login")
    @FormUrlEncoded
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<CommonResponse<UserDataModel>>

    @POST("createCustomer")
    fun addCustomer(@Body customerModel: CustomerModel): Call<ResponseHandler<List<CustomerModel>>>

    @POST("createEmployee")
    fun addEmployee(@Body employeeModel: EmployeeModel) : Call<ResponseHandler<List<EmployeeModel>>>

    @POST("createVendor")
    fun addVendor(@Body vendorModel: VendorModel): Call<ResponseHandler<List<VendorModel>>>

    @POST("getStateList")
    fun getStateList(): Call<CommonResponse<List<StatePostingDataModel>>>


    @POST("getDivisionList")
    @FormUrlEncoded
    fun getDivisionList(@Field("stateId") stateId: Int): Call<CommonResponse<List<DivisionalPostingDataModel>>>


    @POST("getDistrictList")
    @FormUrlEncoded
    fun getDistrictList(@Field("divisionId") divisionId: Int): Call<CommonResponse<List<DistrictPostingDataModel>>>

    @POST("getBlockList")
    @FormUrlEncoded
    fun getBlockList(@Field("districtId") districtId: Int): Call<CommonResponse<List<BlockPostingDataModel>>>

    @Multipart
    @POST("uploadCreateImages")
    fun uploadDocuments(
        @Part donorPhoto: MultipartBody.Part,
        @Part idProof: MultipartBody.Part,
        @Part addressProof: MultipartBody.Part,
        @Part bankPass: MultipartBody.Part
    ): Call<CommonResponse<PhotoResponse>>


    @POST("createUser")
    fun createUser(
        @Body userInfo: CreateUserModel
    ): Call<CommonResponse<String>>

    @POST("createUserData")
    @FormUrlEncoded
    fun createUserData(
        @Field("stateId") stateId: Int
    ): Call<CommonResponse<CreateUserData>>

    @POST("getUserListForLocation")
    @FormUrlEncoded
    fun getUserOfLocation(
        @Field("locationId") locationId: Int,
    @Field("locationType") locationType: Int,
    ): Call<CommonResponse<List<UserDataModel>>>

    @POST("addLocation")
    @FormUrlEncoded
    fun addLocation(
        @Field("stateId") stateId: Int?,
        @Field("divisionId") divisionId: Int?,
        @Field("districtId") districtId: Int?,
        @Field("locationType") locationType: Int,
        @Field("locationName") locationName: String,
    ): Call<CommonResponse<String>>
}
