package com.example.mondaytest.ui.feed


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.mondaytest.models.Article
import com.example.mondaytest.repos.RepositoryImpl.Companion.CARS_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsFragment : BaseItemsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserves()
    }

    private fun initObserves() {
        listViewModel.feedListLiveData.observe(viewLifecycleOwner, feedLiveDataObserver)
    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }

    private fun removeObservers() {
        listViewModel.feedListLiveData.removeObserver(feedLiveDataObserver)
    }

    override fun refresh() {
        listViewModel.fetchFeed(CARS_ID)
    }

    private val feedLiveDataObserver = Observer<List<Article>>{
        (binding.rvItems.adapter as ItemsListAdapter).setData(it)
    }
}