package com.example.mondaytest.repos

import androidx.lifecycle.MutableLiveData
import com.example.mondaytest.models.Feed
import com.example.mondaytest.network.WebService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RepositoryImpl  @Inject constructor(
    private val webService: WebService
): Repository {
    override val feedLiveData: MutableLiveData<Feed> = MutableLiveData()
    override val sportLiveData: MutableLiveData<Feed> = MutableLiveData()
    override val cultureLiveData: MutableLiveData<Feed> = MutableLiveData()


    override suspend fun fetchFeed(id: String) {
        val feed = try {
            webService.getFeed(id)
        } catch (cause: Throwable) {
            cause.printStackTrace()
            throw cause
        }
        when (id) {
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
        const val CARS_ID = "550"
        const val SPORTS_ID = "3"
        const val CULTURE_ID = "538"
    }
}
