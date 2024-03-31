import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.pexelsapp.databinding.FragmentBookmarkBinding
import com.example.pexelsapp.databinding.FragmentDetailsBinding
import com.example.pexelsapp.presentation.MainActivity
import com.example.pexelsapp.presentation.fragments.HomeFragmentDirections
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: PhotoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        binding.buttonBack.setOnClickListener{
            binding.root.findNavController().popBackStack()
        }

        viewModel.detailsPhoto.observe(this, Observer {
            Glide.with(this)
                .load(it.src.portrait)
                .centerCrop()
                .into(binding.detailsImage)
        })
        binding.author.text = viewModel.detailsPhoto.value?.photographer

        binding.buttonDownload.setOnClickListener{
            viewModel.saveImage(binding.detailsImage.drawable)
        }

        viewModel.viewState.observe(this,Observer{
            when(it.isToastDownload){
                true->Toast.makeText(this.context,"Picture saved",Toast.LENGTH_SHORT).show()
                false -> {}
            }
        })
    }
}