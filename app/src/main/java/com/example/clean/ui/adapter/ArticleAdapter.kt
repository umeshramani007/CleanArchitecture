package com.example.clean.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.data.model.Article
import com.example.clean.BR
import com.example.clean.databinding.ItemArticleBinding

class ArticleAdapter(
    context: Context,
    private val articleList: List<Article>,
    private val onItemClickListener: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>()  {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticleBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.mBinding.setVariable(BR.article, articleList[position])
        holder.mBinding.itemRoot.setOnClickListener {
            it.tag?.let { tag ->
                onItemClickListener.invoke(tag as Article)
            }
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    class ArticleViewHolder(binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mBinding = binding
    }
}