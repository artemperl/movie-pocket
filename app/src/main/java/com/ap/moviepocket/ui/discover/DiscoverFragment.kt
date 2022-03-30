package com.ap.moviepocket.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ap.moviepocket.databinding.FragmentDiscoverBinding
import com.ap.moviepocket.domain.UseCaseResult
import com.ap.moviepocket.domain.data
import com.ap.moviepocket.domain.succeeded
import com.ap.moviepocket.util.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.subscribe
import timber.log.Timber

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
        adapter.bottomSpace = 200
        (binding.recyclerViewDiscover.layoutManager as GridLayoutManager).apply {
            val columns = 3
            spanCount = columns
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (adapter.isHeading(position) || adapter.isButton(position)) {
                        columns
                    } else {
                        1
                    }
            }

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
        launchAndRepeatWithViewLifecycle {
            discoverViewModel.movieList.collect {
                when (it) {
                    is UseCaseResult.Loading -> binding.swipeRefreshDiscover.isRefreshing = true
                    is UseCaseResult.Success -> {
                        // TODO add dynamic categories
                        adapter.updateItems("Discover", it.data)
                        binding.swipeRefreshDiscover.isRefreshing = false
                    }
                    is UseCaseResult.Error -> {
                        // TODO add dynamic categories
                        //adapter.addItems("Discover", listOf())
                        binding.swipeRefreshDiscover.isRefreshing = false
                    }
                }
            }

            discoverViewModel.refreshing.collect {
                binding.swipeRefreshDiscover.isRefreshing = it
            }
        }
    }
}