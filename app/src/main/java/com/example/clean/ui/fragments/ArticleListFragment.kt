package com.example.clean.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clean.databinding.FragmentArticleListBinding
import androidx.navigation.findNavController
import com.example.clean.ui.adapter.ArticleAdapter
import com.example.clean.viewmodel.ArticleListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    private val mViewModel: ArticleListViewModel by viewModels()
    lateinit var mBinding: FragmentArticleListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentArticleListBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObserver()
        setListeners()
        fetchArticles()
    }

    private fun addObserver() {

        mViewModel.error.observe(this, {
            showError(it)
        })

        mViewModel.articleList.observe(this, {
            mBinding.recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ArticleAdapter(requireContext(), it, onItemClickListener = { article ->
                    findNavController().navigate(ArticleListFragmentDirections.actionArticleListFragmentToArticleDetailFragment(article))
                })
            }
            showProgress(false)
        })
    }

    private fun fetchArticles() {
        showProgress(true)
        mViewModel.fetchArticle()
    }

    private fun setListeners() {
        mBinding.btnReload.setOnClickListener {
            fetchArticles()
        }
    }

    private fun showProgress(show: Boolean) {
        if (mBinding.groupError.isVisible)
            mBinding.groupError.visibility = View.GONE

        if (show) {
            mBinding.progressBar.visibility = View.VISIBLE
            mBinding.recyclerView.visibility = View.GONE
        } else {
            mBinding.progressBar.visibility = View.GONE
            mBinding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        mBinding.groupError.visibility = View.VISIBLE
        mBinding.recyclerView.visibility = View.GONE
        mBinding.progressBar.visibility = View.GONE
        mBinding.txtMessage.text = message
    }
}