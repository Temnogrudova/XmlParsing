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
import com.example.mondaytest.repos.RepositoryImpl
import com.example.mondaytest.repos.RepositoryImpl.Companion.CULTURE_ID
import com.example.mondaytest.repos.RequestState
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
    /*
    Map is 1 to 1 mapping which is easy to understand.
    SwitchMap on the other hand only mapping the most recent value at a time to reduce unnecessary compute.
     */
    private var feedList = ArrayList<Article>()
    private var combinedFeedList = ArrayList<Article>()

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _feedList = MutableLiveData<ArrayList<Article>>()
    val feedListLiveData: LiveData<ArrayList<Article>> = _feedList

    private val _combinedList = MutableLiveData<ArrayList<Article>>()
    val combinedListLiveData: LiveData<ArrayList<Article>> = _combinedList

    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean> = _refresh

    private lateinit var mainHandler: Handler
    private val refreshTask = object : Runnable {
        override fun run() {
            _refresh.value = true
            mainHandler.postDelayed(this, BaseItemsFragment.TIMEOUT)
        }
    }

    fun fetchFeed(id: String) {
        viewModelScope.launch {
            repository.fetchFeed(id = id)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun initView() {
        mainHandler = Handler(Looper.getMainLooper())
        observeSearchState()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun removeRefreshCallbacks() {
        mainHandler.removeCallbacks(refreshTask)

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun subscribeOnRefreshCallbacks() {
        mainHandler.post(refreshTask)

    }
    private fun observeSearchState() {
        viewModelScope.launch {
            repository.requestState.collect { requestProgress ->
                when (requestProgress) {
                    is RequestState.FeedSuccess -> {
                        requestProgress.feed.articleList?.let { handleFeedItems(it) }
                    }
                    is RequestState.SportSuccess -> {
                        requestProgress.sport.articleList?.let { handleSportItems(it)}
                    }
                    is RequestState.CultureSuccess -> {
                        requestProgress.culture.articleList?.let { handleCultureFeedItems(it) }
                    }
                    is RequestState.Failed -> {
                        _showProgressBar.value = false
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

    private fun handleFeedItems(items: List<Article>) {
        this.feedList.clear()
        this.feedList.addAll(items)
        _feedList.value = feedList
        _showProgressBar.value = false
    }

    private fun handleSportItems(items: List<Article>) {
        this.combinedFeedList.clear()
        this.combinedFeedList.addAll(items)
        fetchFeed(CULTURE_ID)

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
}