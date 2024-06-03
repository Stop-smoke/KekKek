package com.stopsmoke.kekkek

import android.os.Bundle
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
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
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

    @androidx.compose.runtime.Composable
    private fun initView() = with(binding) {
        val state = rememberRichTextState()
        RichTextEditor(
            state = state,
        )
    }

    private fun initListener() = with(binding) {
        ivPostWriteBold.setOnClickListener {
            state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
        }
        ivPostWriteItalic.setOnClickListener {
            state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
        }
        ivPostWriteUnderline.setOnClickListener {
            state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
        }
        ivPostWriteLineThrough.setOnClickListener {
            state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
        }
        ivPostWriteTextColor.setOnClickListener {
            state.toggleSpanStyle(SpanStyle(color = Color.Gray))
        }
        ivPostWriteBackgroundColor.setOnClickListener {
            state.toggleSpanStyle(SpanStyle(background = Color.Yellow))
        }
        ivPostWriteLink.setOnClickListener {
            state.addLink(
                text = "깃허브",
                url = "https://github.com/Stop-smoke/KekKek"
            )
        }
        tvPostWriteCancel.setOnClickListener {

        }
        tvPostWriteRegister.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}