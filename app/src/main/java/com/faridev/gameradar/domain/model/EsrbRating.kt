package com.faridev.gameradar.domain.model

import androidx.annotation.DrawableRes
import com.faridev.gameradar.R
import com.faridev.gameradar.domain.model.EsrbRating.entries

enum class EsrbRating(
    val slug: String,
    val title: String,
    @DrawableRes val iconResId: Int,
    val description: String
) {
    EVERYONE(
        slug = "everyone",
        title = "Everyone",
        iconResId = R.drawable.esrb_everyone,
        description = "Content is generally suitable for all ages. May contain minimal cartoon, fantasy or mild violence and/or infrequent use of mild language."
    ),
    EVERYONE_10_PLUS(
        slug = "everyone-10-plus",
        title = "Everyone 10+",
        iconResId = R.drawable.esrb_everyone_10_plus,
        description = "Content is generally suitable for ages 10 and up. May contain more cartoon, fantasy or mild violence, mild language and/or minimal suggestive themes."
    ),
    TEEN(
        slug = "teen",
        title = "Teen",
        iconResId = R.drawable.esrb_teen,
        description = "Content is generally suitable for ages 13 and up. May contain violence, suggestive themes, crude humor, minimal blood, simulated gambling and/or infrequent use of strong language."
    ),
    MATURE(
        slug = "mature",
        title = "Mature 17+",
        iconResId = R.drawable.esrb_mature,
        description = "Content is generally suitable for ages 17 and up. May contain intense violence, blood and gore, sexual content and/or strong language."
    ),
    ADULTS_ONLY(
        slug = "adults-only",
        title = "Adults Only 18+",
        iconResId = R.drawable.esrb_adults_only,
        description = "Content suitable only for adults ages 18 and up. May include prolonged scenes of intense violence, graphic sexual content and/or gambling with real currency."
    ),
    RATING_PENDING(
        slug = "rating-pending",
        title = "Rating Pending",
        iconResId = R.drawable.esrb_rating_pending,
        description = "Not yet assigned a final ESRB rating. Appears only in advertising, marketing and promotional materials related to a physical (e.g., boxed) video game that is expected to carry an ESRB rating, and should be replaced by a game's rating once it has been assigned."
    );

    companion object {
        fun getEsrbRatingBySlug(slug: String?) =
            entries.findLast { it.slug == slug } ?: RATING_PENDING
    }
}