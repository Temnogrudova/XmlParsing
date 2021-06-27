package com.example.mondaytest.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.mondaytest.models.Article
import com.example.mondaytest.models.Feed
import com.example.mondaytest.network.WebService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

@ActivityRetainedScoped
class RepositoryImpl  @Inject constructor(
    private val webService: WebService
): Repository {
    override val feedLiveData: MutableLiveData<Feed> = MutableLiveData()
    override val sportLiveData: MutableLiveData<Feed> = MutableLiveData()
    override val cultureLiveData: MutableLiveData<Feed> = MutableLiveData()


    override suspend fun fetchFeed(iId: String) {
        val feed = try {
            webService.getFeed(iId)
        } catch (cause: Throwable) {
            cause.printStackTrace()
            throw cause
        }
        when (iId) {
            CARS_ID -> {
                return   feedLiveData.postValue(feed)
            }
            SPORTS_ID -> {
                return   sportLiveData.postValue(feed)
            }
            CULTURE_ID -> {
                return   cultureLiveData.postValue(feed)
            }
        }

    }



    companion object {
        val CARS_ID = "3220"
        val SPORTS_ID = "2605"
        val CULTURE_ID = "3317"
    }
}
