package com.stopsmoke.kekkek.presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostWriteBinding
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.PostWrite
import com.stopsmoke.kekkek.domain.model.PostWriteCategory
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.community.CommunityViewModel
import com.stopsmoke.kekkek.visible
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class PostWriteFragment : Fragment() {

    private var _binding: FragmentPostWriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostWriteViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by activityViewModels()

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
        includePostWriteAppBar.spinnerPostWrite.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun initListener() = with(binding) {
//        ivPostWriteBold.setOnClickListener {
//            val span = StyleSpan(Typeface.BOLD)
//            runTextEditor(span)
//        }
//        ivPostWriteItalic.setOnClickListener {
//            val span = StyleSpan(Typeface.ITALIC)
//            runTextEditor(span)
//        }
//        ivPostWriteUnderline.setOnClickListener {
//            val span = UnderlineSpan()
//            runTextEditor(span)
//        }
//        ivPostWriteLineThrough.setOnClickListener {
//            val span = StrikethroughSpan()
//            runTextEditor(span)
//        }
//        ivPostWriteTextColor.setOnClickListener {
//            val span = ForegroundColorSpan(requireContext().getColor(R.color.red))
//            runTextEditor(span)
//        }
//        ivPostWriteBackgroundColor.setOnClickListener {
//            val span = BackgroundColorSpan(requireContext().getColor(R.color.yellow))
//            runTextEditor(span)
//        }
//        ivPostWriteLink.setOnClickListener {
//            val span = URLSpan("https://github.com/Stop-smoke/KekKek")
//            runTextEditor(span)
//        }

        includePostWriteAppBar.tvPostWriteCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        includePostWriteAppBar.tvPostWriteRegister.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("게시물 등록")
            builder.setMessage("게시물을 등록하시겠습니까?")
            builder.setIcon(R.drawable.ic_post)

            builder.setPositiveButton("예", null)
            builder.setNegativeButton("아니요", null)

            val dialog = builder.create()
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if(etPostWriteTitle.text.isEmpty() || etPostWriteContent.text.isEmpty()) {
                    Snackbar.make(postWrite,"제목 또는 내용을 입력해주세요!",Snackbar.LENGTH_SHORT).show()
                } else {
                    val postWrite = PostWrite(
                        title = etPostWriteTitle.text.toString(),
                        text = etPostWriteContent.text.toString(),
                        dateTime = DateTime(LocalDateTime.now(), LocalDateTime.now()),
                        category = when (includePostWriteAppBar.tvPostWriteType.text) {
                            "자유 게시판" -> PostWriteCategory.GENERAL_DISCUSSION
                            "금연 성공 후기" -> PostWriteCategory.SUCCESS_STORIES
                            "금연 보조제 후기" -> PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS
                            "금연 실패 후기" -> PostWriteCategory.FAILURE_STORIES
                            "금연 다짐" -> PostWriteCategory.QUIT_SMOKING_WILLINGNESS
                            else -> throw IllegalStateException()
                        }
                    )
                    viewModel.addPost(postWrite)
                    dialog.dismiss()
                    findNavController().popBackStack()
                }
            }
        }

        includePostWriteAppBar.tvPostWriteType.setOnClickListener {
            includePostWriteAppBar.spinnerPostWrite.performClick()
        }
    }

//    private fun runTextEditor(span: Any?) {
//        val etPostWriteContent = binding.etPostWriteContent
//        val start = etPostWriteContent.selectionStart
//        val end = etPostWriteContent.selectionEnd
//        if (start != end) {
//            val spannableString = SpannableStringBuilder(etPostWriteContent.text)
//            spannableString.setSpan(
//                span,
//                start,
//                end,
//                Spannable.SPAN_INCLUSIVE_INCLUSIVE // 경계선 포함
//            )
//            etPostWriteContent.text = spannableString
//            etPostWriteContent.setSelection(start, end) // setSelection : 선택 영역 유지
//        }
//    }

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

