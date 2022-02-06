package com.example.clean.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.data.model.Article
import com.example.clean.BR
import com.example.clean.R
import com.example.clean.databinding.FragmentArticleDetailBinding
import com.example.clean.util.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {

    private lateinit var mBinding: FragmentArticleDetailBinding
    private val args: ArticleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_article_detail, container, false)
        mBinding.setVariable(BR.article, args.article)
        return mBinding.root
    }

}