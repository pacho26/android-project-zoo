package hr.tvz.android.slovicprojekt.retrofitFresco

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccessToken(
    @SerializedName("access_token")
    var accessToken: String? = null,
    @SerializedName("token_type")
    val tokenType: String? = null
): Parcelable