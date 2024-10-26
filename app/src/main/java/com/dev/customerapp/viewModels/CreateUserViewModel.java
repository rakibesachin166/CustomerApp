package com.dev.customerapp.viewModels;

import androidx.lifecycle.ViewModel;

import com.dev.customerapp.models.CreateUserDataModel;
import com.dev.customerapp.models.UserPostingModel;
import com.dev.customerapp.models.UserTypes;

public class CreateUserViewModel extends ViewModel {
    UserTypes userType;
    UserPostingModel userPostingModel;
    CreateUserDataModel createUserDataModel;

}
