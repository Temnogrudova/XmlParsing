package com.example.mondaytest.network


import com.example.mondaytest.models.Feed
import retrofit2.http.*

interface WebService{
    @GET("webservice/rss/rssfeeder.asmx/FeederNode")
    suspend fun getFeed(@Query("iID") iID: String): Feed
}

