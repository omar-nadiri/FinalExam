package ge.physho.presentation.fragment.home.source

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ge.physho.R
import ge.physho.databinding.PetShortItemBinding
import ge.physho.extensions.setNetworkImage
import ge.physho.model.specificanimal.Animal

typealias PetItemOnClick = (id: Int?) -> Unit

class PetAdapter : PagingDataAdapter<Animal, PetAdapter.ShortItemViewHolder>(PET_COMPARATOR) {

    companion object {
        private val PET_COMPARATOR = object : DiffUtil.ItemCallback<Animal>() {
            override fun areItemsTheSame(oldItem: Animal, newItem: Animal) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Animal, newItem: Animal) =
                oldItem == newItem
        }
    }

    var petItemOnClick: PetItemOnClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ShortItemViewHolder(
            PetShortItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ShortItemViewHolder, position: Int) {
        val currentPet = getItem(position)
        holder.onBind(currentPet)
    }

    inner class ShortItemViewHolder(private val binding: PetShortItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun onBind(pet: Animal?) {
            binding.apply {
                tvPetShortTitle.text = pet?.name
                tvPetShortBreed.text = pet?.breeds?.primary
//                ivPetSortImage.setNetworkImage(pet?.primaryPhotoCropped?.small)
                choosePetGender(pet?.gender)
                setListeners()
            }
        }

        private fun setListeners() {
            binding.root.setOnClickListener(this)
        }

        private fun choosePetGender(gender: String?) {
            if (gender.equals("Male"))
                binding.genderIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.genderIcon.context,
                        R.drawable.ic_gender_male
                    )
                )
            else
                binding.genderIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.genderIcon.context,
                        R.drawable.ic_gender_female
                    )
                )
        }

        override fun onClick(p0: View?) {
            val model = getItem(absoluteAdapterPosition)
            petItemOnClick?.invoke(model?.id)
        }
    }


}