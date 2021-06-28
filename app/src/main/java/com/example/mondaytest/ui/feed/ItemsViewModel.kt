package com.example.mondaytest.ui.feed


import android.app.Activity
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.lifecycle.*
import com.example.mondaytest.models.Article
import com.example.mondaytest.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ItemsViewModel  @Inject constructor(
    private val repository: Repository
): ViewModel()
{
    /*
    Map is 1 to 1 mapping which is easy to understand.
    SwitchMap on the other hand only mapping the most recent value at a time to reduce unnecessary compute.
     */

    val feedLiveData: LiveData<List<Article>> =  Transformations.map(repository.feedLiveData) { items ->
        items?.articleList
    }
    val sportLiveData: LiveData<List<Article>> =  Transformations.map(repository.sportLiveData) { items ->
        items?.articleList
    }
    val cultureLiveData: LiveData<List<Article>> =  Transformations.map(repository.cultureLiveData) { items ->
        items?.articleList
    }

    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun fetchFeed(id: String) {
        viewModelScope.launch {
            try {
                repository.fetchFeed(id = id)
            } catch (error: Throwable) {
                errorLiveData.value = id
            }
        }
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