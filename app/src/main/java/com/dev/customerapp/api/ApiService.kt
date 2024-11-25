package com.dev.customerapp.api;

import com.dev.customerapp.models.BlockPostingDataModel
import com.dev.customerapp.models.Child1CategoryModel
import com.dev.customerapp.models.CreateUserModel
import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.models.DistrictPostingDataModel
import com.dev.customerapp.models.DivisionalPostingDataModel
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.models.StatePostingDataModel
import com.dev.customerapp.models.TopCategoryModel
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.response.AgreementResponse
import com.dev.customerapp.response.CommonResponse
import com.dev.customerapp.response.CreateEmployeeData
import com.dev.customerapp.response.CreateUserData
import com.dev.customerapp.response.LoginResponse
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
    ): Call<CommonResponse<LoginResponse>>

    @POST("getUserHomeProfile")
    @FormUrlEncoded
    fun getUserHomeProfile(
        @Field("userId") userId: Int
    ): Call<CommonResponse<UserDataModel>>

    @POST("createCustomer")
    fun addCustomer(@Body customerModel: CustomerModel): Call<ResponseHandler<List<CustomerModel>>>

    @POST("createEmployee")
    fun addEmployee(@Body employeeModel: EmployeeModel) : Call<ResponseHandler<List<EmployeeModel>>>

    @POST("createVendor")
    fun addVendor(@Body vendorModel: VendorModel): Call<ResponseHandler<List<VendorModel>>>

    @POST("getStateList")
    fun getStateList(): Call<CommonResponse<List<StatePostingDataModel>>>

    @POST("getEmployeeWithPendingStatus")
    fun getEmployeeWithPendingStatus(): Call<CommonResponse<List<EmployeeModel>>>

    @POST("setEmployeeStatus")
    fun setEmployeeStatus(
        @Field("employeeId") employeeId: Int,
        @Field("employeeStatus") employeeStatus: Int
    ): Call<CommonResponse<String>>


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
        @Part idProof2: MultipartBody.Part?,
        @Part addressProof: MultipartBody.Part,
        @Part addressProof2: MultipartBody.Part?,
        @Part bankPass: MultipartBody.Part,
        @Part signaturePhoto: MultipartBody.Part,
    ): Call<CommonResponse<PhotoResponse>>


    @POST("createUser")
    fun createUser(
        @Body userInfo: CreateUserModel
    ): Call<CommonResponse<String>>

    @POST("createUserData")
    @FormUrlEncoded
    fun createUserData(
        @Field("stateId") stateId: Int,
        @Field("divisionId") divisionalId: Int?,
        @Field("districtId") districtId: Int?,
        @Field("blockId") blockId: Int?,
    ): Call<CommonResponse<CreateUserData>>


    @POST("createEmployeeData")
    @FormUrlEncoded
    fun createEmployeeData(
        @Field("districtId") districtId: Int,
        @Field("blockId") blockId: Int,
    ): Call<CommonResponse<CreateEmployeeData>>

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

    @POST("getAgreement")
    @FormUrlEncoded
    fun getAgreementData(
        @Field("userId") stateId: Int?): Call<CommonResponse<AgreementResponse>>
    @POST("acceptAgreement")
    @FormUrlEncoded
    fun acceptAgreement(
        @Field("userId") userId: Int,
        @Field("agreement") agreement: String,
    ): Call<CommonResponse<String>>


    @POST("getUserProfile")
    @FormUrlEncoded
    fun getUserProfile(
        @Field("userId") userId: Int,
    ): Call<CommonResponse<UserDataModel>>



    @POST("getTopCategoryList")
    fun getTopCategoryList(): Call<CommonResponse<List<TopCategoryModel>>>


    @POST("getChild1CategoryList")
    @FormUrlEncoded
    fun getChild1CategoryList(@Field("topCategoryId") topCategoryId: Int): Call<CommonResponse<List<Child1CategoryModel>>>




    @POST("addCategory")
    @FormUrlEncoded
    fun addCategory(
        @Field("categoryName") categoryName: String,
        @Field("topCategoryId") topCategoryId: Int?,
        @Field("categoryPhoto") categoryPhoto: String?,
        @Field("child1CategoryId") child1CategoryId: Int?,
        @Field("categoryType") categoryType: Int,
        @Field("stateProfit") stateProfit: Int,
        @Field("divisionProfit") divisionProfit: Int,
        @Field("districtProfit") districtProfit: Int,
        @Field("blockProfit") blockProfit: Int,

    ): Call<CommonResponse<String>>

    @Multipart
    @POST("uploadCategoryImage")
    fun uploadCategoryImage(
        @Part topCategoryImage: MultipartBody.Part
    ): Call<CommonResponse<String>>

    @POST("getNestedCategories")
    fun getNestedCategories(): Call<CommonResponse<List<TopCategoryModel>>>

}
