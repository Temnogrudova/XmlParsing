package com.example.mondaytest.ui.feed

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.mondaytest.models.Article
import com.example.mondaytest.repos.RepositoryImpl.Companion.SPORTS_ID
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CombinedItemsItemsFragment: BaseItemsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserves()
    }

    private fun initObserves() {
        listViewModel.combinedListLiveData.observe(viewLifecycleOwner, combinedObserver)
    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }

    private fun removeObservers() {
        listViewModel.combinedListLiveData.removeObserver(combinedObserver)
    }

    override fun refresh() {
        listViewModel.fetchFeed(SPORTS_ID)
    }

    private val combinedObserver = Observer<List<Article>>{
        (binding.rvItems.adapter as ItemsListAdapter).setData(it)
    }
}