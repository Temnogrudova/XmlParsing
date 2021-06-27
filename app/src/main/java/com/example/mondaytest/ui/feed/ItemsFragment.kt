package com.example.mondaytest.ui.feed


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.mondaytest.models.Article
import com.example.mondaytest.repos.RepositoryImpl.Companion.CARS_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsFragment : BaseItemsFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserves()
    }

    private fun initObserves() {
        listViewModel.feedLiveData.observe(viewLifecycleOwner, feedLiveDataObserver)
        listViewModel.errorLiveData.observe(viewLifecycleOwner, errorLiveDataObserver)
    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }

    private fun removeObservers() {
        listViewModel.feedLiveData.removeObserver(feedLiveDataObserver)
        listViewModel.errorLiveData.removeObserver(errorLiveDataObserver)
    }

    override fun refresh() {
        super.refresh()
        listViewModel.fetchFeed(CARS_ID)
    }

    private val feedLiveDataObserver = Observer<List<Article>>{
        this.items.clear()
        this.items.addAll(it)
        binding.rvItems.adapter?.notifyDataSetChanged()
        binding.progressBar.visibility = View.GONE
    }

    private val errorLiveDataObserver = Observer<String>{
        binding.progressBar.visibility = View.GONE
    }
}