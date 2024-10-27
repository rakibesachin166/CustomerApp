package com.dev.customerapp.viewModels;

import androidx.lifecycle.ViewModel;

import com.dev.customerapp.models.CreateUserDataModel;
import com.dev.customerapp.models.UserPostingModel;
import com.dev.customerapp.models.UserTypes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CreateUserViewModel extends ViewModel {

    private final MutableLiveData<UserTypes> userType = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(0);
    private final MutableLiveData<UserPostingModel> userPostingModel = new MutableLiveData<>();
    private final MutableLiveData<CreateUserDataModel> createUserDataModel = new MutableLiveData<>();

    // Getter for userType
    public LiveData<UserTypes> getUserType() {
        return userType;
    }

    // Setter for userType
    public void setUserType(UserTypes userType) {
        this.userType.setValue(userType);
    }

    // Getter for currentPage
    public LiveData<Integer> getCurrentPage() {
        return currentPage;
    }

    // Setter for currentPage
    public void setCurrentPage(int page) {
        this.currentPage.setValue(page);
    }

    // Getter for userPostingModel
    public LiveData<UserPostingModel> getUserPostingModel() {
        return userPostingModel;
    }

    // Setter for userPostingModel
    public void setUserPostingModel(UserPostingModel model) {
        this.userPostingModel.setValue(model);
    }

    // Getter for createUserDataModel
    public LiveData<CreateUserDataModel> getCreateUserDataModel() {
        return createUserDataModel;
    }

    // Setter for createUserDataModel
    public void setCreateUserDataModel(CreateUserDataModel model) {
        this.createUserDataModel.setValue(model);
    }
}
