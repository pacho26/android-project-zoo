package hr.tvz.android.slovicprojekt.retrofitFresco

import hr.tvz.android.slovicprojekt.model.Animal
import retrofit2.Call
import retrofit2.http.GET

interface ServiceInterface {
    @GET("bfe0cb76fa1381c438a0")
    fun fetchData(): Call<MutableList<Animal>>
}