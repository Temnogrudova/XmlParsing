package com.example.mondaytest.di

import com.example.mondaytest.BuildConfig
import com.example.mondaytest.network.WebService
import com.example.mondaytest.repos.Repository
import com.example.mondaytest.repos.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.OkHttpClient

@Module
@InstallIn(ActivityRetainedComponent::class)
interface  MainActivityRetainedModuleBinds {

    @Binds
    fun bindListRepository(
        listRepositoryImpl: RepositoryImpl
    ) : Repository

}

@Module
@InstallIn(ActivityRetainedComponent::class)
object  MainActivityRetainedModule {
    @Provides
    @ActivityRetainedScoped
    fun provideWebService(okHttpClient:OkHttpClient) : WebService = createWebService(okHttpClient = okHttpClient, url = BuildConfig.SERVER_URL)
}