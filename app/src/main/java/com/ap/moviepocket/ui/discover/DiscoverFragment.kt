package com.ap.moviepocket.ui.discover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ap.moviepocket.databinding.FragmentDiscoverBinding
import com.ap.moviepocket.domain.UseCaseResult
import com.ap.moviepocket.domain.data
import com.ap.moviepocket.domain.succeeded
import com.ap.moviepocket.util.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.subscribe
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    private val discoverViewModel: DiscoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDiscoverBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = discoverViewModel
        }

        // Adapter
        val adapter = MovieListAdapter()
        binding.recyclerViewDiscover.adapter = adapter
        (binding.recyclerViewDiscover.layoutManager as GridLayoutManager).apply {
            val columnCount = 3
            spanCount = columnCount
            spanSizeLookup = SpanSizeLookup(adapter, columnCount)
        }
        adapter.onLoadMoreClickListener = { category, page ->
            discoverViewModel.loadMoreMovies(category, page)
        }

        binding.swipeRefreshDiscover.setOnRefreshListener {
            discoverViewModel.refreshMovies()
        }
        // add space to account for the status bar
        binding.swipeRefreshDiscover.setProgressViewEndTarget(true, 400)

        subscribeUi(adapter)
        discoverViewModel.refreshMovies()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeUi(adapter: MovieListAdapter) {
        // update the adapter with new items
        launchAndRepeatWithViewLifecycle {
            discoverViewModel.moviesMap.collect { moviesMap ->
                adapter.updateItems(moviesMap)
            }
        }

        // update the swipeRefreshView
        launchAndRepeatWithViewLifecycle {
            discoverViewModel.refreshing.collect {
                if (it != binding.swipeRefreshDiscover.isRefreshing) {
                    binding.swipeRefreshDiscover.isRefreshing = it
                }
            }
        }
    }
}