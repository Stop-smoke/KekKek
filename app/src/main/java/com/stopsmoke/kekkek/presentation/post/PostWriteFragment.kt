package com.stopsmoke.kekkek.presentation.post

import android.content.Intent
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
import android.text.Html
import android.text.SpannedString
import androidx.core.text.HtmlCompat
import androidx.core.text.set
import com.stopsmoke.kekkek.presentation.extensions.GlideImageLoader
import com.stopsmoke.kekkek.presentation.extensions.GlideVideoThumbnailLoader
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.AztecText
import org.wordpress.aztec.ITextFormat
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener
import org.wordpress.aztec.toolbar.ToolbarAction
import org.wordpress.aztec.toolbar.ToolbarItems

@AndroidEntryPoint
class PostWriteFragment : Fragment(), IAztecToolbarClickListener {

    private var _binding: FragmentPostWriteBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val BOLD = "<b>Bold</b><br>"
        private const val ITALIC = "<i>Italic</i><br>"
        private const val UNDERLINE = "<u>Underline</u><br>"
        private const val STRIKETHROUGH =
            "<s class=\"test\">Strikethrough</s><br>" // <s> or <strike> or <del>
        private const val BACKGROUND =
            "<span style=\"background-color:#005082\">BACK<b>GROUND</b></span><br>"
        private const val IMG =
            "[caption align=\"alignright\"]<img src=\"https://examplebloge.files.wordpress.com/2017/02/3def4804-d9b5-11e6-88e6-d7d8864392e0.png\" />Caption[/caption]"
        private const val FOREGROUND = "<span style=\"color:#ff0000\">Text Color</span><br>"

        private val EXAMPLE =
            BOLD + ITALIC + UNDERLINE + STRIKETHROUGH + FOREGROUND + BACKGROUND + IMG

    }

    private val MEDIA_PHOTOS_PERMISSION_REQUEST_CODE: Int = 1003
    private val REQUEST_MEDIA_PHOTO: Int = 2003

    protected lateinit var aztecObject: Aztec

    private val viewModel: PostWriteViewModel by viewModels()

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
        initTextEditor()
    }

    private fun initTextEditor() = with(binding) {
        //aztec 코드
        aztecObject = Aztec.with(aztec, source, formattingToolbar, this@PostWriteFragment)
            .setImageGetter(GlideImageLoader(requireContext()))
            .setVideoThumbnailGetter(GlideVideoThumbnailLoader(requireContext()))
        formattingToolbar.setToolbarItems(
            ToolbarItems.BasicLayout(
                ToolbarAction.BOLD,
                ToolbarAction.ITALIC,
                ToolbarAction.UNDERLINE,
                ToolbarAction.STRIKETHROUGH
            )
        )
        aztec.addMediaAfterBlocks()
        aztecObject.sourceEditor?.displayStyledAndFormattedHtml(EXAMPLE)


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

        includePostWriteAppBar.tvPostWriteCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        includePostWriteAppBar.tvPostWriteRegister.setOnClickListener {
            if (binding.etPostWriteTitle.text.isEmpty() || binding.aztec.text.isEmpty()) {
                Snackbar.make(binding.root, "제목 또는 내용을 입력해주세요!", Snackbar.LENGTH_SHORT).show()
            } else {
                Log.d("aztec",aztec.toFormattedHtml())
                val dialog = builder.create()
                dialog.show()

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val post = PostEdit(
                        title = etPostWriteTitle.text.toString(),
                        text = aztec.toFormattedHtml(),
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
        aztec.setText(post.text)
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

//        binding.ivPostWriteImage.setImageBitmap(roundedBitmap)
//        binding.ivPostWriteImage.visibility = View.VISIBLE
    }

    private fun getOrientation(uri: Uri): Int {
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
        when (orientation) {
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

    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.visible()
        _binding = null
    }

    override fun onToolbarCollapseButtonClicked() {
    }

    override fun onToolbarExpandButtonClicked() {
    }

    override fun onToolbarFormatButtonClicked(format: ITextFormat, isKeyboardShortcut: Boolean) {
    }

    override fun onToolbarHeadingButtonClicked() {
    }

    override fun onToolbarHtmlButtonClicked() {
    }

    override fun onToolbarListButtonClicked() {
    }

    override fun onToolbarMediaButtonClicked(): Boolean {
        return false
    }
}
