package de.filtik.gronkhtv

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import de.filtik.gronkhtv.objects.Movie

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(
        viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
        item: Any
    ) {
        val movie = item as Movie

        viewHolder.title.text = movie.title
        viewHolder.subtitle.text = movie.description
        viewHolder.body.text = movie.description
    }
}