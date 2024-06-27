package com.stopsmoke.kekkek.presentation.post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
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
import android.media.ExifInterface

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

    private var isBold = false
    private var isItalic = false
    private var isUnderline = false
    private var isStrikethrough = false
    private var currentTextColor: Int? = null
    private var currentBackgroundColor: Int? = null

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
            builder.setMessage("게시물을 수정하시겠습니까?")
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
                        dateTime = DateTime(
                            created = viewModel.post.value?.dateTime?.created
                                ?: LocalDateTime.now(), modified = LocalDateTime.now()
                        ),
                        category = binding.includePostWriteAppBar.tvPostWriteType.text.toString()
                            .trim()
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

    private fun insertImage(url: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(url)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val orientation = getOrientation(url)
        val rotatedBitmap = rotateBitmap(bitmap, orientation)

        val width = resources.getDimensionPixelSize(R.dimen.post_image_width)
        val height = resources.getDimensionPixelSize(R.dimen.post_image_height)
        val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, width, height, true)

        val roundedBitmap = getRoundedCornerBitmap(scaledBitmap, 20f)

        binding.ivPostWriteImage.setImageBitmap(roundedBitmap)
        binding.ivPostWriteImage.visibility = View.VISIBLE
    }

    private fun getOrientation(uri: Uri):Int {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        inputStream?.use { stream ->
            val exifInterface = ExifInterface(stream)
            return exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
        }
        return ExifInterface.ORIENTATION_UNDEFINED
    }

    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun getRoundedCornerBitmap(bitmap: Bitmap, radius: Float): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = Color.BLACK
        canvas.drawRoundRect(rectF, radius, radius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun initTextEditor() = with(binding) {

        etPostWriteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editText: Editable?) {
                editText?.let {
                    Log.d("editText length", it.length.toString())
                    Log.d("edittext", editText.toString())
                    applyActiveStyles(it, 0, it.length)
                }
            }
        })

        ivPostWriteBold.setOnClickListener {
            isBold = !isBold
            ivPostWriteBold.isSelected = isBold
            applyCurrentStylesToSelection()
        }
        ivPostWriteItalic.setOnClickListener {
            isItalic = !isItalic
            ivPostWriteItalic.isSelected = isItalic
            applyCurrentStylesToSelection()
        }
        ivPostWriteUnderline.setOnClickListener {
            isUnderline = !isUnderline
            ivPostWriteUnderline.isSelected = isUnderline
            applyCurrentStylesToSelection()
        }
        ivPostWriteLineThrough.setOnClickListener {
            isStrikethrough = !isStrikethrough
            ivPostWriteLineThrough.isSelected = isStrikethrough
            applyCurrentStylesToSelection()
        }
        ivPostWriteTextColor.setOnClickListener {
            currentTextColor = if (currentTextColor == null) {
                requireContext().getColor(R.color.red)
            } else null
            ivPostWriteTextColor.isSelected = currentTextColor != null
            applyCurrentStylesToSelection()
        }
        ivPostWriteBackgroundColor.setOnClickListener {
            currentBackgroundColor = if (currentBackgroundColor == null) {
                requireContext().getColor(R.color.yellow)
            } else null
            ivPostWriteBackgroundColor.isSelected = currentBackgroundColor != null
            applyCurrentStylesToSelection()
        }
        ivPostWriteLink.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun applyCurrentStylesToSelection() {
        val editable = binding.etPostWriteContent.text
        Log.d("editable", editable.toString())
        val start = binding.etPostWriteContent.selectionStart
        Log.d("start", start.toString())
        val end = binding.etPostWriteContent.selectionEnd
        Log.d("end",end.toString())
        applyActiveStyles(editable, start, end)
    }

    private fun applyActiveStyles(spannableString: Editable, start: Int, end: Int) {
        if (isBold) {
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableString.getSpans(start, end, StyleSpan::class.java).forEach {
                if(it.style == Typeface.BOLD) spannableString.removeSpan(it)
            }
        }
        if (isItalic) {
            spannableString.setSpan(
                StyleSpan(Typeface.ITALIC),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableString.getSpans(start, end, StyleSpan::class.java).forEach {
                if(it.style == Typeface.ITALIC) spannableString.removeSpan(it)
            }
        }
        if (isUnderline) {
            spannableString.setSpan(UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            spannableString.getSpans(start, end, UnderlineSpan::class.java).forEach {
                spannableString.removeSpan(it)
            }
        }
        if (isStrikethrough) {
            spannableString.setSpan(
                StrikethroughSpan(),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableString.getSpans(start, end, StrikethroughSpan::class.java).forEach {
                spannableString.removeSpan(it)
            }
        }
        currentTextColor?.let {
            spannableString.setSpan(
                ForegroundColorSpan(it),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } ?: run {
            spannableString.getSpans(start, end, ForegroundColorSpan::class.java).forEach {
                spannableString.removeSpan(it)
            }
        }
        currentBackgroundColor?.let {
            spannableString.setSpan(
                BackgroundColorSpan(it),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } ?: run {
            spannableString.getSpans(start, end, BackgroundColorSpan::class.java).forEach {
                spannableString.removeSpan(it)
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

