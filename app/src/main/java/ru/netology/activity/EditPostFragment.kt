package ru.netology.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.databinding.FragmentEditPostBinding
import ru.netology.util.AndroidUtils
import ru.netology.util.StringArg
import ru.netology.viewmodel.PostViewModel

class EditPostFragment : Fragment() {

    private var _binding: FragmentEditPostBinding? = null
    private val binding: FragmentEditPostBinding
    get() = _binding!!

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.edit) {
            requestFocus()
            setText(arguments?.textArg)
        }

        binding.cancelButton.setOnClickListener{
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        binding.ok.setOnClickListener {
            if (binding.edit.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_empty_content),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.changeContent(binding.edit.text.toString())
                viewModel.save()
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
