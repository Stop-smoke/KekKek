package com.stopsmoke.kekkek.presentation.post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostWriteBinding
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostEdit
import com.stopsmoke.kekkek.domain.model.toPostWriteCategory
import com.stopsmoke.kekkek.domain.model.toStringKR
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.community.CommunityViewModel
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class PostWriteFragment : Fragment() {
    private var _binding: FragmentPostWriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostWriteViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by activityViewModels()
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { url ->
        url?.let {
            insertImage(it)
        }
    }

    private val builder by lazy {
        AlertDialog.Builder(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString("post_id")?.let {
            viewModel.updatePostId(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        initListener()
    }

    private fun initView() = with(binding) {
        initSpinner()
    }

    private fun initSpinner() = with(binding) {
        val category = resources.getStringArray(R.array.post_category)
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, category)
        includePostWriteAppBar.spinnerPostWrite.adapter = adapter
        includePostWriteAppBar.spinnerPostWrite.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    includePostWriteAppBar.tvPostWriteType.text = category[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
    }

    private fun createDialogBuilder() = with(builder) {
        if (viewModel.post.value == null) {
            builder.setTitle("게시물 등록")
            builder.setMessage("게시물을 등록하시겠습니까?")
            builder.setIcon(R.drawable.ic_post)
        } else {
            builder.setTitle("게시물 수정")
            builder.setMessage("게시물을 수정하시겠습니까??")
            builder.setIcon(R.drawable.ic_post)
        }

        builder.setPositiveButton("예", null)
        builder.setNegativeButton("아니요", null)
    }

    private fun initListener() = with(binding) {
        initTextEditor()

        includePostWriteAppBar.tvPostWriteCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        includePostWriteAppBar.tvPostWriteRegister.setOnClickListener {
            if (binding.etPostWriteTitle.text.isEmpty() || binding.etPostWriteContent.text.isEmpty()) {
                Snackbar.make(binding.root, "제목 또는 내용을 입력해주세요!", Snackbar.LENGTH_SHORT).show()
            } else {
                val dialog = builder.create()
                dialog.show()

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val post = PostEdit(
                        title = etPostWriteTitle.text.toString(),
                        text = etPostWriteContent.text.toString(),
                        dateTime = DateTime(LocalDateTime.now(), LocalDateTime.now()),
                        category = binding.includePostWriteAppBar.tvPostWriteType.text.toString()
                            .toPostWriteCategory()
                    )
                    if (viewModel.post.value == null) viewModel.addPost(post)
                    else viewModel.editPost(post)
                    dialog.dismiss()
                    findNavController().popBackStack()
                }
            }
        }

        includePostWriteAppBar.tvPostWriteType.setOnClickListener {
            includePostWriteAppBar.spinnerPostWrite.performClick()
        }
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            post.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {
                    it?.let { onBind(it) }
                    createDialogBuilder()
                }
        }
    }

    private fun onBind(post: Post) = with(binding) {
        etPostWriteContent.setText(post.text)
        etPostWriteTitle.setText(post.title)

        includePostWriteAppBar.tvPostWriteType.text = post.category.toStringKR()

        includePostWriteAppBar.tvPostWriteRegister.text = "수정"
    }

    private fun runTextEditor(span: Any?) {
        val etPostWriteContent = binding.etPostWriteContent
        val start = etPostWriteContent.selectionStart
        val end = etPostWriteContent.selectionEnd

        val spannableString = SpannableStringBuilder(etPostWriteContent.text)
        spannableString.setSpan(
            span,
            start,
            end,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE // 경계선 포함
        )
        etPostWriteContent.text = spannableString
        etPostWriteContent.setSelection(start, end) // setSelection : 선택 영역 유지
    }

    private fun insertImage(url: Uri) {
        val etPostWriteContent = binding.etPostWriteContent
        val start = etPostWriteContent.selectionStart
//        val end = etPostWriteContent.selectionEnd

        val inputStream = requireContext().contentResolver.openInputStream(url)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val width = resources.getDimensionPixelSize(R.dimen.post_image_width)
        val height = resources.getDimensionPixelSize(R.dimen.post_image_height)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

        val drawable = BitmapDrawable(resources, scaledBitmap)
        drawable.setBounds(0, 0, width, height)

        val imageSpan = ImageSpan(drawable)
        val spannableString = SpannableStringBuilder(etPostWriteContent.text)
        spannableString.insert(start, " ")
        spannableString.setSpan(
            imageSpan,
            start,
            start + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        etPostWriteContent.text = spannableString
        etPostWriteContent.setSelection(start + 1)
    }

    private fun initTextEditor() = with(binding) {
        ivPostWriteBold.setOnClickListener {
            val span = StyleSpan(Typeface.BOLD)
            runTextEditor(span)
        }
        ivPostWriteItalic.setOnClickListener {
            val span = StyleSpan(Typeface.ITALIC)
            runTextEditor(span)
        }
        ivPostWriteUnderline.setOnClickListener {
            val span = UnderlineSpan()
            runTextEditor(span)
        }
        ivPostWriteLineThrough.setOnClickListener {
            val span = StrikethroughSpan()
            runTextEditor(span)
        }
        ivPostWriteTextColor.setOnClickListener {
            val span = ForegroundColorSpan(requireContext().getColor(R.color.red))
            runTextEditor(span)
        }
        ivPostWriteBackgroundColor.setOnClickListener {
            val span = BackgroundColorSpan(requireContext().getColor(R.color.yellow))
            runTextEditor(span)
        }
        ivPostWriteLink.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.visible()
        _binding = null
    }

}

