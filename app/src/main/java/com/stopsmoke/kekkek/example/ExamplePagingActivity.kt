package com.stopsmoke.kekkek.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ActivityExamplePagingBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewExamplePagingBinding
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExamplePagingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExamplePagingBinding

    private val viewModel: ExamplePagingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExamplePagingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = ExamplePagingAdapter()

//        binding.rvTest.adapter = adapter
//        binding.rvTest.layoutManager = LinearLayoutManager(this)

//        lifecycleScope.launch {
//            viewModel.data.collect { paging ->
//                adapter.submitData(paging)
//            }
//        }

    }
}

private class ExamplePagingAdapter :
    PagingDataAdapter<CommentEntity, ExamplePagingViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamplePagingViewHolder {
        val view =
            RecyclerviewExamplePagingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ExamplePagingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamplePagingViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<CommentEntity>() {
            override fun areItemsTheSame(oldItem: CommentEntity, newItem: CommentEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CommentEntity,
                newItem: CommentEntity,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}

private class ExamplePagingViewHolder(
    private val binding: RecyclerviewExamplePagingBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(commentEntity: CommentEntity) {

//        binding.tvTitle.text = commentEntity.title

    }

}