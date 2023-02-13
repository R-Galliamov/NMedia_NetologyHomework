package ru.netology.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.activity.NewPostFragment.Companion.textArg
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostViewHolder
import ru.netology.databinding.FragmentCardPostBinding
import ru.netology.dto.Post
import ru.netology.util.LongArg
import ru.netology.viewmodel.PostViewModel

class CardPostFragment : Fragment() {

    private var _binding: FragmentCardPostBinding? = null
    private val binding: FragmentCardPostBinding
        get() = _binding!!

    companion object {
        var Bundle.postId: Long? by LongArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private val interactionListener by lazy {
        object : OnInteractionListener {
            override fun onOpenVideo(post: Post) {
                viewModel.openVideo(post)
                val openVideoIntent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(post.video_url)
                }
                startActivity(openVideoIntent)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_cardPostFragment_toEditPostFragment,
                    Bundle().apply { textArg = post.content })
            }


            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
                viewModel.shareById(post.id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardPostBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val holder = PostViewHolder(binding, interactionListener)
        var postId = arguments?.postId

        viewModel.data.observe(viewLifecycleOwner) {
            postId?.let { it -> viewModel.getPostById(it)?.let { it -> holder.bind(it) } }
        }
    }
}
