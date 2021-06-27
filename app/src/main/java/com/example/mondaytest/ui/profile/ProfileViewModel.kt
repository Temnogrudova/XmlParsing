package com.example.mondaytest.ui.profile


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel  @Inject constructor(): ViewModel()
{
    var lastSeenFeedTitle: MutableLiveData<String> = MutableLiveData()

    fun setLastSeenFeedTitle(title: String) {
        lastSeenFeedTitle.value =title
    }
}