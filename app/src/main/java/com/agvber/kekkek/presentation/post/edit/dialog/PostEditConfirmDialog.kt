package com.agvber.kekkek.presentation.post.edit.dialog

// 콜백을 생성자에 넣으면 편한데, 설계상 생성자에 넣으면 안 됨
// 나중에 수정해서 반영할 것
//
//
//class PostEditConfirmDialog(
//    private val onConfirm: () -> Unit
//) : DialogFragment() {
//
//    private var _binding: FragmentCommonDialogBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return super.onCreateDialog(savedInstanceState).apply {
//            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentCommonDialogBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        bind()
//        initListener()
//    }
//
//    private fun bind() = with(binding) {
//        tvDialogTitle.text = "게시글 등록"
//        tvDialogContent.text = "게시글을 등록하시겠습니까?"
//        btnDialogCancel.text = "취소"
//        btnDialogFinish.text = "등록"
//    }
//
//    private fun initListener() = with(binding) {
//        btnDialogFinish.setOnClickListener {
//            onConfirm()
//            dismiss()
//        }
//
//        btnDialogCancel.setOnClickListener {
//            dismiss()
//        }
//    }
//}
