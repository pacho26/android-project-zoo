package hr.tvz.android.slovicprojekt.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import hr.tvz.android.slovicprojekt.R
import hr.tvz.android.slovicprojekt.database.DatabaseHelper
import hr.tvz.android.slovicprojekt.model.Animal

class ItemListFragment : ListFragment() {

    private val STATE_ACTIVATED_POSITION = "activated_position"
    var mActivatedPosition = ListView.INVALID_POSITION
    lateinit var animalsList: MutableList<Animal>

    interface Callbacks {
        fun onItemSelected(id: Int)
    }

    private val sDummyCallbacks: Callbacks =
        object : Callbacks {
            override fun onItemSelected(id: Int) {}
        }

    var mCallbacks: Callbacks = sDummyCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            val databaseHelper = DatabaseHelper(this.requireContext())
            animalsList = databaseHelper.getAnimals()

            listAdapter = ArrayAdapter(
                requireActivity(),
                R.layout.card_layout,
                R.id.text1,
                animalsList
            )
        }, 3000)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION))
        }
    }

    private fun setActivatedPosition(position: Int) {
        if (position == ListView.INVALID_POSITION) {
            listView.setItemChecked(mActivatedPosition, false)
        } else {
            listView.setItemChecked(position, true)
        }

        mActivatedPosition = position
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        check(context is Callbacks) { "Activity must implement fragment's callbacks." }

        mCallbacks = context
    }

    override fun onDetach() {
        super.onDetach()
        mCallbacks = sDummyCallbacks
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        mCallbacks.onItemSelected(animalsList.get(position).id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition)
        }
    }
}