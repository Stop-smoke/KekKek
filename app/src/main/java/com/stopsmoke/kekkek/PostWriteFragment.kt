package com.stopsmoke.kekkek

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TextAppearanceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.stopsmoke.kekkek.databinding.FragmentPostViewBinding
import com.stopsmoke.kekkek.databinding.FragmentPostWriteBinding


class PostWriteFragment : Fragment() {

    private var _binding: FragmentPostWriteBinding ?= null
    private val binding get() = _binding!!

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

        }
        tvPostWriteRegister.setOnClickListener {

        }
    }

    private fun runTextEditor(span: Any?) {
        val etPostWriteContent = binding.etPostWriteContent
        val start = etPostWriteContent.selectionStart
        val end = etPostWriteContent.selectionEnd
        if(start!=end) {
            val spannableString = SpannableStringBuilder(etPostWriteContent.text)
            spannableString.setSpan(
                span,
                start,
                end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE // 경계선 포함
            )
            etPostWriteContent.text = spannableString
            etPostWriteContent.setSelection(start,end) // setSelection : 선택 영역 유지
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}