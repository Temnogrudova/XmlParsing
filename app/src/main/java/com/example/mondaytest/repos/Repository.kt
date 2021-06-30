package com.example.mondaytest.repos


import kotlinx.coroutines.flow.StateFlow

interface Repository {

    val requestState: StateFlow<RequestState>

    suspend fun fetchFeed(id: String)
}
