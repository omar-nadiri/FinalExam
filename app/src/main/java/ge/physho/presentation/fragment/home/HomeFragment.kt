package ge.physho.presentation.fragment.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuInflater
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import ge.physho.R
import ge.physho.databinding.FragmentHomeBinding
import ge.physho.network.state.UiState
import ge.physho.presentation.fragment.BaseFragment
import ge.physho.presentation.fragment.home.source.PetAdapter
import ge.physho.presentation.fragment.home.source.PetLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import com.google.android.material.chip.ChipGroup
import ge.physho.extensions.*
import ge.physho.model.enums.Backdrop
import ge.physho.model.enums.Gender
import ge.physho.model.types.SpecificType


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val adapter = PetAdapter()
    private val viewModel: HomeViewModel by viewModels()
    private var backdropShown = false
    private val animatorSet = AnimatorSet()

    override fun start() {
        viewModel.newsItemList.forEachIndexed { index, pet ->
            binding.petsChipGroup.addView(addAttributesToChip(pet, index))
        }

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.appBar)
        initRecycler()
    }

    override fun observers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.pets.collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
        viewModel.uiState.observe(this, {
            if (!viewModel.uiState.value!!.isLoading) {
                setUpFilter(it.animalTypes)
            }
            setLayoutLoading(viewModel.uiState.value!!.isLoading)

            when (viewModel.uiState.value!!.error) {
                UiState.Error.NETWORK_ERROR -> {
                    setLayoutError()
                }
            }
        })
    }

    private fun setUpFilter(petType: SpecificType.Type?) {
        petType?.coats?.forEachIndexed { index, coat ->
            binding.coatsChipGroup.addView(addAttributesToChip(coat, index))
        }
        petType?.colors?.forEachIndexed { index, colors ->
            binding.colorChipGroup.addView(addAttributesToChip(colors, index))
        }
        playAnimation(Backdrop.FULLY_OPENED_PORTRAIT)
        binding.petsChipGroup.setChildrenEnabled(true)
    }

    override fun onClicks() = with(binding) {
        adapter.addLoadStateListener { loadStates ->
            chooseUiState(loadStates)
        }

        appBar.setNavigationOnClickListener {
            showBackDrop()
        }

        swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        petsChipGroup.setOnCheckedChangeListener { group, checkedId ->
            petCheckedListener(group, checkedId)
        }

        coatsChipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.defineIsCheckedChip()
            if (checkedId >= 0) {
                val coatChip: Chip = group.findViewById(checkedId)
                viewModel.coat = coatChip.text.toString()
            }
            viewModel.coat = null

        }

        colorChipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.defineIsCheckedChip()
            if (checkedId >= 0) {
                val colorChip: Chip = group.findViewById(checkedId)
                viewModel.color = colorChip.text.toString()
            }
            viewModel.color = null

        }

        genderSlider.addOnChangeListener { _, value, _ ->
            defineGender(value.toInt())
        }


        filterBtn.setOnClickListener {
            showBackDrop()
            adapter.refresh()
        }

    }

    private fun defineGender(value: Int) {
        when (value) {
            Gender.MALE.ordinal -> {
                binding.maleGenderIcon.imageTintList =
                    getColorStateList(requireContext(), R.color.white)
                binding.femaleGenderIcon.imageTintList =
                    getColorStateList(requireContext(), R.color.primary_light)
                viewModel.gender = Gender.MALE.value
            }
            Gender.NONE.ordinal -> {
                binding.maleGenderIcon.imageTintList =
                    getColorStateList(requireContext(), R.color.primary_light)
                binding.femaleGenderIcon.imageTintList =
                    getColorStateList(requireContext(), R.color.primary_light)
                viewModel.gender = Gender.NONE.value
            }
            Gender.FEMALE.ordinal -> {
                binding.maleGenderIcon.imageTintList =
                    getColorStateList(requireContext(), R.color.primary_light)
                binding.femaleGenderIcon.imageTintList =
                    getColorStateList(requireContext(), R.color.white)
                viewModel.gender = Gender.FEMALE.value
            }
        }
    }


    private fun petCheckedListener(group: ChipGroup, checkedId: Int) {
        group.defineIsCheckedChip()
        binding.coatsChipGroup.removeAllViews()
        binding.colorChipGroup.removeAllViews()
        if (checkedId >= 0) {
            val checkedChip: Chip = group.findViewById(checkedId)
            viewModel.pet = checkedChip.text.toString()
            viewModel.getPetTypes(checkedChip.text.toString())
        } else {
            viewModel.pet = null
            viewModel.color = null
            viewModel.coat = null
            playAnimation(Backdrop.PARTIALLY_OPENED_PORTRAIT)
        }
    }

    private fun setLayoutError() {
        binding.filterRefreshBtn.visible()
    }

    private fun setLayoutLoading(isLoading: Boolean) {
        binding.filterProgress.isVisible = isLoading
        binding.petsChipGroup.setChildrenEnabled(!isLoading)
        binding.filterRefreshBtn.gone()

    }

    private fun chooseUiState(loadStates: CombinedLoadStates) {
        binding.apply {
            swipeRefresh.isRefreshing = loadStates.source.refresh is LoadState.Loading
            recyclerView.isVisible = loadStates.source.refresh is LoadState.NotLoading
            tvError.isVisible = loadStates.source.refresh is LoadState.Error
            tvError.text = getString(R.string.error_text)
            if (loadStates.source.refresh is LoadState.NotLoading &&
                loadStates.append.endOfPaginationReached && adapter.itemCount < 1
            ) {
                recyclerView.isVisible = false
                tvError.isVisible = true
                tvError.text = getString(R.string.empty_text)

            } else {
                tvError.isVisible = false
            }
        }
    }

    private fun initRecycler() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    GridLayoutManager(requireContext(), 2)
                else
                    GridLayoutManager(requireContext(), 4)
        }
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.adapter = adapter.withLoadStateFooter(
                footer = PetLoadStateAdapter { adapter.retry() },
            )
        }

        adapter.petItemOnClick = {
            openPetDetailScreen(it)
        }
    }

    private fun openPetDetailScreen(petId: Int?) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToPetDetailsFragment(
                petId ?: 0
            )
        )
    }

    private fun getScreenHeight(): Int {
        val metrics: DisplayMetrics = requireContext().resources.displayMetrics
        return metrics.heightPixels
    }

    private fun removeAnimationListeners() {
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()
    }

    private fun createAnimator(openType: Backdrop): ObjectAnimator {

        val height: Int = when (openType) {
            Backdrop.PARTIALLY_OPENED_PORTRAIT -> {
                if (viewModel.pet != null)
                    R.dimen.FULL_OPENED_PORTRAIT
                else
                    R.dimen.PARTIALLY_OPENED_PORTRAIT
            }
            Backdrop.FULLY_OPENED_PORTRAIT -> {
                R.dimen.FULL_OPENED_PORTRAIT
            }
        }

        val translateY =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                getScreenHeight() - requireContext().resources.getDimensionPixelSize(height)
            else
                getScreenHeight()


        return ObjectAnimator.ofFloat(
            binding.productGrid,
            getString(R.string.animation_name),
            (if (backdropShown) translateY else 0).toFloat()
        )

    }

    private fun playAnimation(openType: Backdrop) {
        val animator = createAnimator(openType)
        animator.duration = 500
        animator.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.play(animator)
        animator.start()
    }

    private fun showBackDrop() {
        backdropShown = !backdropShown
        removeAnimationListeners()
        binding.appBar.updateIcon(backdropShown)
        playAnimation(Backdrop.PARTIALLY_OPENED_PORTRAIT)
    }


    private fun setChipStyle(): ChipDrawable {
        return ChipDrawable.createFromAttributes(
            requireContext(),
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Choice
        ).withChipStyle(getColorStateList(requireContext(), R.color.primary_dark))
    }

    private fun addAttributesToChip(petName: String?, id: Int): Chip {
        return Chip(requireContext()).apply {
            text = petName
            this.id = id
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            this.setChipDrawable(setChipStyle())
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.shr_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_switchLayout -> {
//                isLinearLayoutManager = !isLinearLayoutManager
////                chooseLayout()
////                setIcon(item)
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}