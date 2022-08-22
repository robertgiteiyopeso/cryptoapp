package com.example.cryptoapp

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.login.CredentialsModel
import com.example.cryptoapp.login.SessionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val mdbRepo: MDBRepositoryRetrofit,
    private val sharedPrefSession: SharedPreferences
) : ViewModel() {

    private var job: Job = Job()

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState>
        get() = _state

    fun doLogin() {

        val usernameValue = username.value
        val passwordValue = password.value

        if (usernameValue.isNullOrBlank()) {
            _state.value = LoginState.Error("Username blank;")
            return
        }
        if (passwordValue.isNullOrBlank()) {
            _state.value = LoginState.Error("Password blank;")
            return
        }

        //Cancel any previous login attempt
        job.cancel()

        //Try to login
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.postValue(LoginState.InProgress)

                val session = getSession(usernameValue, passwordValue)
                saveNewSessionId(session.sessionId)

                _state.postValue(LoginState.Success)

            } catch (e: HttpException) {
                _state.postValue(LoginState.Error("Incorrect username or password"))
                Log.e("LoginViewModel: ", e.message.toString())
            }
        }
    }

    private fun saveNewSessionId(sessionId: String) {
        with(sharedPrefSession.edit()) {
            putString("session_id", sessionId)
            apply()
        }
        mdbRepo.sessionId = sessionId
    }

    private suspend fun getSession(usernameValue: String, passwordValue: String): SessionModel {

        //Get new token
        val token = mdbRepo.getNewTokenParsed()

        //Login
        val login = mdbRepo.login(
            CredentialsModel(
                usernameValue,
                passwordValue,
                token.requestToken
            )
        )

        //Save new session ID
        return mdbRepo.createSession(login)
    }

    fun checkOldLogin() {
        val sessionId = sharedPrefSession.getString("session_id", "")
        if (!sessionId.isNullOrBlank()) {
            mdbRepo.sessionId = sessionId
            _state.postValue(LoginState.Success)
        }
    }
}

class LoginViewModelFactory(private val application: MovieApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(
            application.appContainer.mdbRepo,
            application.sharedPrefSession
        ) as T
    }
}