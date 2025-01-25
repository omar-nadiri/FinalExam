package ge.physho.presentation.fragment.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.physho.databinding.PetLongItemBinding
import ge.physho.model.specificanimal.Animal
import ge.physho.extensions.setNetworkImage
import ge.physho.model.specificanimal.Photo

class PhotosViewPagerAdapter :
    RecyclerView.Adapter<PhotosViewPagerAdapter.ViewPagerViewHolder>() {

    private var photosList: MutableList<Photo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = PetLongItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currentItem = photosList[position]
        holder.onBind(currentItem)
    }

    override fun getItemCount(): Int = photosList.size

    inner class ViewPagerViewHolder(var binding: PetLongItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Photo) {
            binding.ivPetLargeImage.setNetworkImage(item.medium)
        }

    }

    fun setData(infoData: MutableList<Photo>) {
        photosList.clear()
        photosList.addAll(infoData)
        notifyDataSetChanged()
    }
}