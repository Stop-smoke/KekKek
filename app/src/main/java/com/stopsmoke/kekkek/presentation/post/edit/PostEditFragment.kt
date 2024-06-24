package com.stopsmoke.kekkek.presentation.post.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
 import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentPostEditBinding
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.PostEdit
import com.stopsmoke.kekkek.domain.model.PostWriteCategory
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.presentation.post.PostWriteItem
import com.stopsmoke.kekkek.visible
import java.time.LocalDateTime

class PostEditFragment : Fragment() {


    private var _binding: FragmentPostEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostEditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 키보드가 나와있을 때 스낵바 출력을 위한 인풋모드 설정
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initView()
        initListener()

        // 기존의 내용을 가져오는 것이 필요
        // 수정 완료 시 업데이트 필요
    }

    private fun initView() {
        binding.includePostEditAppBar.spinnerEditWrite.apply {
            val category = resources.getStringArray(R.array.post_category)
            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    category
                )
            this.adapter = adapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.includePostEditAppBar.tvPostEditType.text = category[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            val postWriteItem = arguments?.getParcelable<PostWriteItem>("postWriteItem")
            postWriteItem?.let {
                binding.etPostEditTitle.setText(it.title)
                binding.etPostWriteContent.setText(it.content)
            }
        }
    }

    private fun initListener() {
        binding.includePostEditAppBar.tvPostEditCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.includePostEditAppBar.tvPostEditRegister.setOnClickListener {
            showEditPostDialog()
        }

        binding.includePostEditAppBar.tvPostEditType.setOnClickListener {
            binding.includePostEditAppBar.spinnerEditWrite.performClick()
        }    }

    private fun showEditPostDialog() {
        if (binding.etPostEditTitle.text.isEmpty() || binding.etPostWriteContent.text.isEmpty()) {
            Snackbar.make(binding.root, "제목 또는 내용을 입력해주세요!", Snackbar.LENGTH_SHORT).show()
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("게시물 수정")
            builder.setMessage("게시물을 수정하시겠습니까??")
            builder.setIcon(R.drawable.ic_post)

            builder.setPositiveButton("예", null)
            builder.setNegativeButton("아니요", null)

            val dialog = builder.create()
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val postEdit = PostEdit(
                    title = binding.etPostEditTitle.text.toString(),
                    text = binding.etPostWriteContent.text.toString(),
                    dateTime = DateTime(LocalDateTime.now(), LocalDateTime.now()),
                    category = when (binding.includePostEditAppBar.tvPostEditType.text) {
                        "자유 게시판" -> PostWriteCategory.GENERAL_DISCUSSION
                        "금연 성공 후기" -> PostWriteCategory.SUCCESS_STORIES
                        "금연 보조제 후기" -> PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS
                        else -> throw IllegalStateException()
                    }
                )
//                viewModel.editPost(postEdit)
                dialog.dismiss()
                findNavController().popBackStack()
            }
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