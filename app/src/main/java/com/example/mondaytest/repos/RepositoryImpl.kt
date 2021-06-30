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


    override suspend fun fetchFeed(id: Types) = withContext(Dispatchers.IO) {
        _requestState.value = RequestState.InProgress
        try {
            val feed = webService.getFeed(id.value.toString())
            _requestState.value = RequestState.Success(Pair(id, feed))
        } catch (cause: Throwable) {
            cause.printStackTrace()
            throw cause
            _requestState.value = RequestState.Failed(Pair(id, "Exception ${cause.message}"))
        }
    }
}

enum class Types(val value: Int) {
    CARS_ID(550),
    SPORTS_ID(3),
    CULTURE_ID(538)
}

sealed class RequestState {
    object NotStarted : RequestState()
    object InProgress: RequestState()
    data class Success(val feedByType: Pair<Types, Feed>) : RequestState()
    data class Failed(val errorMessageByType: Pair<Types, String>) : RequestState()
}


