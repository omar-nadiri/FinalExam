package ge.physho.presentation.fragment.home.source

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.physho.databinding.LoadStateFooterBinding

class PetLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PetLoadStateAdapter.LoadStateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            LoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryBtn.setOnClickListener {
                retry.invoke()
            }
        }

        fun onBind(loadState: LoadState) {
            binding.apply {
                loadingProgress.isVisible = loadState is LoadState.Loading
                retryBtn.isVisible = loadState !is LoadState.Loading
                errorTv.isVisible = loadState !is LoadState.Loading
            }
        }
    }


}