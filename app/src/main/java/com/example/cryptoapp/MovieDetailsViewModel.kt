package com.example.cryptoapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MovieDetailsViewModel : ViewModel() {
    val movieId = MutableLiveData<String>()
}
