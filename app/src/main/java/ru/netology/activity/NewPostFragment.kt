package ru.netology.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.databinding.FragmentNewPostBinding
import ru.netology.util.AndroidUtils
import ru.netology.util.StringArg
import ru.netology.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private var _binding: FragmentNewPostBinding? = null
    private val binding: FragmentNewPostBinding
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
        _binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                var draft = binding.edit.text.toString()
                if (!draft.isNullOrBlank()) {
                    viewModel.saveDraft(draft)
                }
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        setDraft()
        with(binding) {
            //edit.setText(arguments?.textArg)
            edit.requestFocus()

            cancelButton.setOnClickListener {
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }

            ok.setOnClickListener {
                if (edit.text.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    viewModel.changeContent(edit.text.toString())
                    viewModel.save()
                    AndroidUtils.hideKeyboard(requireView())
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDraft(){
        var draft = viewModel.getDraft()
        if (!draft.isNullOrBlank()) {
            binding.edit.setText(draft)
        }
    }
}
