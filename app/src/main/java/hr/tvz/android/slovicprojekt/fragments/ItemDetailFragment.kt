package hr.tvz.android.slovicprojekt.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import hr.tvz.android.slovicprojekt.ZooDetailsActivity
import hr.tvz.android.slovicprojekt.databinding.FragmentItemDetailBinding
import hr.tvz.android.slovicprojekt.R
import hr.tvz.android.slovicprojekt.database.DatabaseHelper
import hr.tvz.android.slovicprojekt.model.Animal

class ItemDetailFragment : Fragment(R.layout.fragment_item_detail) {
    val ARG_ANIMAL = null
    val ARG_ITEM_ID = "item_id"

    var animal: Animal? = null
    var clickedOnRandom = false

    lateinit var mediaPlayer: MediaPlayer

    private lateinit var binding: FragmentItemDetailBinding

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
        binding = FragmentItemDetailBinding.bind(rootView)
        if (animal != null) {
            binding.nameContent.text = animal!!.name
            binding.familyContent.text = animal!!.family
            binding.lifetimeContent.text = animal!!.lifetime
            binding.weightContent.text = animal!!.weight

            binding.randomButton.setOnClickListener {
                if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }

                clickedOnRandom = true

                val databaseHelper = DatabaseHelper(requireContext())
                while (true) {
                    val randomId =
                        Math.floor(Math.random() * databaseHelper.getAnimalCount()).toInt() + 1
                    if (randomId != animal!!.id) {
                        animal = databaseHelper.getAnimalById(randomId)
                        break
                    }
                }
                binding.nameContent.text = animal!!.name
                binding.familyContent.text = animal!!.family
                binding.lifetimeContent.text = animal!!.lifetime
                binding.weightContent.text = animal!!.weight

                val animalSound = requireContext().resources.getIdentifier(
                    animal!!.name.toLowerCase().replace(" ", "_"),
                    "raw",
                    requireContext().packageName
                )

                if (animalSound != 0) {
                    binding.animalImage.setOnClickListener {
                        mediaPlayer = MediaPlayer.create(
                            requireContext(), animalSound
                        )
                        mediaPlayer.start()
                    }
                }


                val imgUri = Uri.parse(animal!!.img)
                val draweeView = binding.animalImage as SimpleDraweeView
                draweeView.setImageURI(imgUri)
            }

            val imgUri = Uri.parse(animal!!.img)
            val draweeView = binding.animalImage as SimpleDraweeView
            draweeView.setImageURI(imgUri)

            val animalSound = requireContext().resources.getIdentifier(
                animal!!.name.toLowerCase().replace(" ", "_"),
                "raw",
                requireContext().packageName
            )

            if (animalSound != 0 && !clickedOnRandom) {
                // Play animal sound on image click
                binding.animalImage.setOnClickListener {
                    mediaPlayer = MediaPlayer.create(
                        requireContext(), animalSound
                    )
                    mediaPlayer.start()
                }
            }

            binding.buttonLink.setOnClickListener {
                val url = "https://en.wikipedia.org/wiki/${animal!!.name.replace(" ", "_")}"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

            binding.notificationButton.setOnClickListener {
                val intent = Intent(requireContext(), ZooDetailsActivity::class.java)
                startActivity(intent)
                if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
            }
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }
}