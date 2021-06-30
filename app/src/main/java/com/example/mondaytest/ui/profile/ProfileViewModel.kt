package com.example.mondaytest.ui.profile


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel  @Inject constructor(): ViewModel()
{
    private val _lastSeenFeedTitle = MutableLiveData<String>()
    val lastSeenFeedTitle: LiveData<String> = _lastSeenFeedTitle

    fun setLastSeenFeedTitle(title: String) {
        _lastSeenFeedTitle.value = title
    }
}