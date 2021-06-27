package com.example.mondaytest.ui.feed

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mondaytest.databinding.FragmentItemsBinding
import com.example.mondaytest.models.Article
import com.example.mondaytest.ui.profile.ProfileViewModel

abstract class BaseItemsFragment: Fragment() {
    protected val listViewModel: ItemsViewModel by viewModels()
    protected val profileViewModel: ProfileViewModel by activityViewModels()
    protected lateinit var binding: FragmentItemsBinding

    private lateinit var mainHandler: Handler
    private val refreshTask = object : Runnable {
        override fun run() {
            refresh()
            mainHandler.postDelayed(this, TIMEOUT)
        }
    }

    var items: ArrayList<Article?> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                    DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                    )
            )
            adapter = ItemsListAdapter(
                    items,
                    adapterInteractionCallbackImpl
            )
        }
    }

    private val adapterInteractionCallbackImpl = object:
            ItemsListAdapter.InteractionListener {
        override fun onItemClick(v: View, feed: Article?) {
            feed?.let { it ->
                it.title?.let {title -> profileViewModel.setLastSeenFeedTitle(title) }
                it.link?.let { link -> activity?.let { activity -> listViewModel.onItemClick(activity, link) } }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(refreshTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(refreshTask)
    }

    open fun refresh()
    {
        if (this::binding.isInitialized) {
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    companion object {
        const val TIMEOUT: Long = 5000
    }
}