package com.example.mondaytest.repos


import com.example.mondaytest.models.Feed
import com.example.mondaytest.network.WebService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class RepositoryImpl  @Inject constructor(
    private val webService: WebService
): Repository {
    private val _requestState = MutableStateFlow<RequestState>(RequestState.NotStarted)
    override val requestState: StateFlow<RequestState>
        get() = _requestState


    override suspend fun fetchFeed(id: String) = withContext(Dispatchers.IO) {
        _requestState.value = RequestState.InProgress
        try {
            val feed = webService.getFeed(id)
            when (id) {
                CARS_ID -> {
                    _requestState.value = RequestState.FeedSuccess(feed)
                }
                SPORTS_ID -> {
                    _requestState.value = RequestState.SportSuccess(feed)
                }
                CULTURE_ID -> {
                    _requestState.value = RequestState.CultureSuccess(feed)

                }
            }
        } catch (cause: Throwable) {
            cause.printStackTrace()
            throw cause
            _requestState.value = RequestState.Failed("Exception ${cause.message}")
        }
    }

    companion object {
        const val CARS_ID = "550"
        const val SPORTS_ID = "3"
        const val CULTURE_ID = "538"
    }
}

sealed class RequestState {
    object NotStarted : RequestState()
    object InProgress: RequestState()
    data class FeedSuccess(val feed: Feed) : RequestState()
    data class SportSuccess(val sport: Feed) : RequestState()
    data class CultureSuccess(val culture: Feed) : RequestState()
    data class Failed(val errorMessage: String) : RequestState()
}


