package hr.tvz.android.slovicprojekt.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal(
    var id: Int,
    var name: String,
    var family: String
) : Parcelable {

    override fun toString(): String {
        return name
    }
}