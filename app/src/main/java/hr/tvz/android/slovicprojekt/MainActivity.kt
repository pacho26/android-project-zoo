package hr.tvz.android.slovicprojekt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import hr.tvz.android.listaslovic.ShowableActivity
import hr.tvz.android.slovicprojekt.R
import hr.tvz.android.slovicprojekt.database.DatabaseHelper
import hr.tvz.android.slovicprojekt.fragments.ItemDetailFragment
import hr.tvz.android.slovicprojekt.fragments.ItemListFragment
import hr.tvz.android.slovicprojekt.model.Animal
import hr.tvz.android.slovicprojekt.retrofitFresco.ServiceGenerator
import hr.tvz.android.slovicprojekt.retrofitFresco.ServiceInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ItemListFragment.Callbacks, ShowableActivity {

    val API_URL = "https://api.npoint.io/"
    var mTwoPane = false
    lateinit var animalsList: MutableList<Animal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (findViewById<FrameLayout>(R.id.item_detail_container) != null) {
            mTwoPane = true
        } else {
            mTwoPane = false
            var itemListFragment = ItemListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.item_list_container, itemListFragment)
                .commit()
        }

        val client: ServiceInterface = ServiceGenerator().createService(
            ServiceInterface::class.java,
            API_URL
        )

        val databaseHelper = DatabaseHelper(this)
        databaseHelper.onUpgrade(databaseHelper.writableDatabase, 1, 2)

        val animals: Call<MutableList<Animal>> = client.fetchData()
        animals.enqueue(object : Callback<MutableList<Animal>> {
            override fun onResponse(
                call: Call<MutableList<Animal>>,
                response: Response<MutableList<Animal>>
            ) {
                if (response.isSuccessful) {
                    animalsList = response.body()!!
                    for (animal in animalsList) {
                        databaseHelper.createAnimal(animal)
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<Animal>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemSelected(id: Int) {
        var arguments = Bundle()
        arguments.putString(ItemDetailFragment().ARG_ITEM_ID, id.toString())
        arguments.putParcelable(
            ItemDetailFragment().ARG_ANIMAL,
            animalsList.find { it.id == id })

        var detailFragment = ItemDetailFragment()
        detailFragment.arguments = arguments
        var fragmentTransaction = supportFragmentManager.beginTransaction()

        if (mTwoPane) {
            fragmentTransaction
                .replace(R.id.item_detail_container, detailFragment)
                .commit()
        } else {
            fragmentTransaction
                .addToBackStack(null)
                .replace(R.id.item_list_container, detailFragment)
                .commit()
        }
    }

    override val displayName: String?
        get() = "Završni projekt"
    override val description: String?
        get() = "Zoo Encylclopedia"
}