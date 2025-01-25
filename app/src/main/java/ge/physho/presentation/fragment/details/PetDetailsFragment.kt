package ge.physho.presentation.fragment.details

import android.util.Log.d
import android.view.Gravity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import ge.physho.R
import ge.physho.databinding.FragmentPetDetailsBinding
import ge.physho.extensions.*
import ge.physho.model.specificanimal.Animal
import ge.physho.model.specificanimal.Photo
import ge.physho.network.state.UiState
import ge.physho.presentation.fragment.BaseFragment
import ge.physho.presentation.fragment.details.adapters.PhotosViewPagerAdapter

class PetDetailsFragment :
    BaseFragment<FragmentPetDetailsBinding>(FragmentPetDetailsBinding::inflate) {

    private val viewModel: PetDetailsViewModel by viewModels()
    private val args: PetDetailsFragmentArgs by navArgs()
    private val imagesVpAdapter: PhotosViewPagerAdapter = PhotosViewPagerAdapter()

    override fun start() {
        viewModel.specificPet(args.petId)
    }

    override fun onClicks() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.specificPet(args.petId)
        }
        binding.vpPhotos.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tvCurrentPhoto.text = (position + 1).toString()
                super.onPageSelected(position)
            }
        })
    }

    override fun observers() {

        viewModel.uiState.observe(this, {
            if (!viewModel.uiState.value!!.isLoading) {
                setPetDetails(it.pet?.animal)

                it.pet?.animal?.photos?.let { it1 -> setImagesToAdapter(it1) }
            }
            setLayoutLoading(viewModel.uiState.value!!.isLoading)

            when (viewModel.uiState.value!!.error) {
                UiState.Error.NETWORK_ERROR -> {
                    setLayoutError(it.error!!.message)
                }
            }
        })
    }

    private fun setPetDetails(pet: Animal?) {
        pet?.let {
            binding.tvName.text = pet.name
            binding.tvEmail.text = pet.contact?.email
            binding.description.text = pet.description
            binding.genderText.text = pet.gender
            binding.sizeText.text = pet.size
            binding.ageText.text = pet.age
            binding.attributesChipGroup.removeAllViews()
        }

        pet?.tags?.forEach {
            binding.attributesChipGroup.addView(createAttributeChips(it))
        }

    }

    private fun setChipStyle(): ChipDrawable {
        return ChipDrawable.createFromAttributes(
            requireContext(),
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Action
        ).apply {
            chipBackgroundColor =
                AppCompatResources.getColorStateList(requireContext(), R.color.primary_dark)
        }
    }

    private fun createAttributeChips(attribute: String): Chip {
        return Chip(requireContext()).apply {
            text = attribute
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setChipDrawable(setChipStyle())
        }
    }

    private fun setImagesToAdapter(images: MutableList<Photo>) {
        if (images.size == 0)
            imagesVpAdapter.setData(mutableListOf(Photo(null, null, null, null)))
        else
            imagesVpAdapter.setData(images)

        binding.vpPhotos.adapter = imagesVpAdapter
        binding.tvAllPhotoSize.text =
            getString(R.string.all_view_pager_size, images.size.toString())


    }

    private fun setLayoutError(error: String) {
        binding.root.showSnack(error + "Please swipe down to refresh !", R.color.error)
    }

    private fun setLayoutLoading(isLoading: Boolean) {
        binding.swipeRefresh.isRefreshing = isLoading
        binding.mainDetailsLayout.isVisible = !isLoading
    }

}