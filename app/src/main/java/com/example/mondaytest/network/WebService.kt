package com.example.mondaytest.network


import com.example.mondaytest.models.Feed
import retrofit2.http.*

interface WebService{
    @GET("/Integration/StoryRss{id}.xml")
    suspend fun getFeed(@Path("id") id: String): Feed
}

