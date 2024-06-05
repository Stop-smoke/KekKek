package com.stopsmoke.kekkek.presentation.post

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostWriteBinding
import com.stopsmoke.kekkek.presentation.community.CommunityFragment


class PostWriteFragment : Fragment() {

    private var _binding: FragmentPostWriteBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        findNavController().previousBackStackEntry?.savedStateHandle?.set("NEW_POST", null)
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
        initListener()
    }

    private fun initView() = with(binding) {
        initSpinner()
    }

    private fun initSpinner() = with(binding) {
        val category = resources.getStringArray(R.array.post_category)
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, category)
        spinnerPostWrite.adapter = adapter
        spinnerPostWrite.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        tvPostWriteType.text = category[0]
                    }

                    1 -> {
                        tvPostWriteType.text = category[1]
                    }

                    2 -> {
                        tvPostWriteType.text = category[2]
                    }

                    3 -> {
                        tvPostWriteType.text = category[3]
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun initListener() = with(binding) {
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
            val span = URLSpan("https://github.com/Stop-smoke/KekKek")
            runTextEditor(span)
        }

        tvPostWriteCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        tvPostWriteRegister.setOnClickListener {
            val newPost = PostWriteItem(
                postType = tvPostWriteType.text.toString(),
                title = etPostWriteTitle.text.toString(),
                content = etPostWriteContent.text.toString()
            )
            findNavController().previousBackStackEntry?.savedStateHandle?.set("NEW_POST", newPost)
            findNavController().popBackStack()
        }
    }

    private fun runTextEditor(span: Any?) {
        val etPostWriteContent = binding.etPostWriteContent
        val start = etPostWriteContent.selectionStart
        val end = etPostWriteContent.selectionEnd
        if (start != end) {
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
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

