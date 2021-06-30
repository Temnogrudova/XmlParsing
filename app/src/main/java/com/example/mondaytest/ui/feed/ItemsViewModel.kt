package com.example.mondaytest.ui.feed


import android.app.Activity
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.example.mondaytest.models.Article
import com.example.mondaytest.repos.Repository
import com.example.mondaytest.repos.RequestState
import com.example.mondaytest.repos.Types
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ItemsViewModel  @Inject constructor(
    private val repository: Repository
): ViewModel(), LifecycleObserver
{
    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _feedList = MutableLiveData<ArrayList<Article>>()
    val feedListLiveData: LiveData<ArrayList<Article>> = _feedList

    private val _combinedList = MutableLiveData<ArrayList<Article>>()
    val combinedListLiveData: LiveData<ArrayList<Article>> = _combinedList

    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean> = _refresh

    private var feedList = ArrayList<Article>()
    private var combinedFeedList = ArrayList<Article>()

    private lateinit var mainHandler: Handler
    private val refreshTask = object : Runnable {
        override fun run() {
            _refresh.value = true
            mainHandler.postDelayed(this, TIMEOUT)
        }
    }

    fun fetchFeed(id: Types) {
        viewModelScope.launch {
            repository.fetchFeed(id = id)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun initView() {
        _refresh.value = true
        mainHandler = Handler(Looper.getMainLooper())
        observeRequestState()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun removeRefreshCallbacks() {
        mainHandler.removeCallbacks(refreshTask)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun subscribeOnRefreshCallbacks() {
        mainHandler.post(refreshTask)

    }

    private fun observeRequestState() {
        viewModelScope.launch {
            repository.requestState.collect { requestProgress ->
                when (requestProgress) {
                    is RequestState.Success -> {
                        when(requestProgress.feedByType.first)
                        {
                            Types.CARS_ID -> requestProgress.feedByType.second.articleList?.let { handleFeedItems(it) }
                            Types.SPORTS_ID -> requestProgress.feedByType.second.articleList?.let { handleSportFeedItems(it) }
                            Types.CULTURE_ID -> requestProgress.feedByType.second.articleList?.let { handleCultureFeedItems(it) }
                        }
                    }
                    is RequestState.Failed -> {
                        when(requestProgress.errorMessageByType.first)
                        {
                            Types.SPORTS_ID -> handleSportError()
                            else -> _showProgressBar.value = false
                        }
                    }
                    is RequestState.InProgress -> {
                        _showProgressBar.value = true
                    }
                    is RequestState.NotStarted -> {
                    }
                }
            }
        }
    }

    private fun handleSportError() {
        fetchFeed(Types.CULTURE_ID)
    }

    private fun handleFeedItems(items: List<Article>) {
        this.feedList.clear()
        this.feedList.addAll(items)
        _feedList.value = feedList
        _showProgressBar.value = false
    }

    private fun handleSportFeedItems(items: List<Article>) {
        this.combinedFeedList.clear()
        this.combinedFeedList.addAll(items)
        fetchFeed(Types.CULTURE_ID)
    }

    private fun handleCultureFeedItems(items: List<Article>) {
        this.combinedFeedList.addAll(items)
        _combinedList.value = combinedFeedList
        _showProgressBar.value = false
    }

    /*
    * If the user leaves the screen before getList(...) returns a value,
    *  the system calls onCleared and the ViewModel’s CoroutineScope will be canceled.
    *  This will make sure that the movieListData.value will not be modified,
    * and the code will not attempt to update a no longer existing screen.*/
    override fun onCleared() {
        viewModelScope.cancel()
    }

    fun onItemClick(context: Activity, url: String) {
        val intent = Intent(ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
    companion object {
        const val TIMEOUT: Long = 5000
    }
}