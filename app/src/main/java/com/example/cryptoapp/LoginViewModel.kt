package com.example.cryptoapp

import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.login.CredentialsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private val mdbRepo = MDBRepositoryRetrofit
    private var job: Job = Job()

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState>
        get() = _state

    fun doLogin() {

        val usernameValue = username.value
        val passwordValue = password.value

        if(usernameValue.isNullOrBlank()){
            _state.value = LoginState.Error("Username blank;")
            return
        }
        if(passwordValue.isNullOrBlank()){
            _state.value = LoginState.Error("Password blank;")
            return
        }

        //Cancel any previous login attempt
        job.cancel()

        //Try to login
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.postValue(LoginState.InProgress)

                //Get new token
                val token = mdbRepo.getNewTokenParsed()

                //Login
                 mdbRepo.login(
                    CredentialsModel(
                        usernameValue,
                        passwordValue,
                        token.requestToken
                    )
                )

                _state.postValue(LoginState.Success)

            } catch (e: HttpException) {
                _state.postValue(LoginState.Error("Incorrect username or password"))
                Log.e("LoginViewModel: ", e.message.toString())
            }
        }
    }
}