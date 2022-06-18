package hr.tvz.android.slovicprojekt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.drawee.backends.pipeline.Fresco
import hr.tvz.android.slovicprojekt.databinding.FragmentItemDetailBinding
import hr.tvz.android.slovicprojekt.R
import hr.tvz.android.slovicprojekt.model.Animal

class ItemDetailFragment : Fragment(R.layout.fragment_item_detail) {
    val ARG_ANIMAL = null;
    val ARG_ITEM_ID = "item_id"

    var animal: Animal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(context)

        animal = requireArguments().getParcelable(ARG_ANIMAL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_item_detail, container, false)
        val binding = FragmentItemDetailBinding.bind(rootView)
        if (animal != null) {
            binding.nameContent.text = animal!!.name
            binding.familyContent.text = animal!!.family
            binding.lifetimeContent.text = animal!!.lifetime
            binding.weightContent.text = animal!!.weight
//            binding.nationalityContent.text = animal!!.country
//            binding.currentClubContent.text = animal!!.club

//            val playerName = animal!!.name

//            val imgUri = Uri.parse(animal!!.img)
//            val draweeView = binding.playerImage as SimpleDraweeView
//            draweeView.setImageURI(imgUri)

//            binding.buttonLink.setOnClickListener {
//                val url = "https://www.google.com/search?q=${animal!!.name}"
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(url)
//                startActivity(intent)
//            }
        }

        return rootView
    }
}