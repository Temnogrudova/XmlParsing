package com.example.mondaytest.repos

import androidx.lifecycle.LiveData
import com.example.mondaytest.models.Feed

interface Repository {
    val feedLiveData: LiveData<Feed>
    val sportLiveData: LiveData<Feed>
    val cultureLiveData: LiveData<Feed>

    suspend fun fetchFeed(iId: String)
}
