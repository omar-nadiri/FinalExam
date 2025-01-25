package ge.physho.extensions

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import ge.physho.R

fun View.showSnack(message: String, color: Int) {
    val snackBar: Snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(color)
    snackBar.show()
}

fun View.visible(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}


fun ImageView.setNetworkImage(
    cover: String?,
    placeHolder: Int = R.mipmap.pet,
    errorImage: Int = R.mipmap.not_provided_picture
) {

    if (cover != null) {
        Glide.with(context)
            .load(cover)
            .error(errorImage)
            .placeholder(placeHolder)
            .into(this)
    } else {
        this.setImageResource(R.mipmap.not_provided_picture)
    }

}

fun ChipGroup.setChildrenEnabled(enable: Boolean) {
    children.forEach { it.isEnabled = enable }
}

fun Chip.setCheckedChipLayout() {
    if (this.isChecked) {
        this.setChipStrokeWidthResource(R.dimen.chip_stroke_width_bold)
        this.setChipStrokeColorResource(R.color.white)
    } else {
        this.setChipStrokeWidthResource(R.dimen.chip_stroke_width)
        this.setChipStrokeColorResource(R.color.primary_light)
    }
}

fun ChipGroup.defineIsCheckedChip() {
    this.children.forEach {
        val chip = it as Chip
        chip.setCheckedChipLayout()
    }
}

fun Toolbar.updateIcon(backdropShown: Boolean) {
    navigationIcon = if (backdropShown) {
        ContextCompat.getDrawable(
            context,
            R.drawable.ic_close
        )
    } else {
        ContextCompat.getDrawable(
            context,
            R.drawable.ic_paw
        )
    }
}

fun ChipDrawable.withChipStyle(colorStateList: ColorStateList): ChipDrawable {
    return apply {
        chipBackgroundColor = colorStateList
        setChipMinHeightResource(R.dimen.chip_width)
        setChipStrokeColorResource(R.color.primary_light)
        setChipStrokeWidthResource(R.dimen.chip_stroke_width)
        chipCornerRadius = 20F
    }
}
