package com.stopsmoke.kekkek.presentation.post.edit

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
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.data.utils.BitmapCompressor
import com.stopsmoke.kekkek.core.domain.model.DateTime
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.PostEdit
import com.stopsmoke.kekkek.core.domain.model.toPostWriteCategory
import com.stopsmoke.kekkek.core.domain.model.toStringKR
import com.stopsmoke.kekkek.databinding.FragmentPostEditBinding
import com.stopsmoke.kekkek.presentation.NavigationKey
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.progress.CircularProgressDialogFragment
import com.stopsmoke.kekkek.presentation.putNavigationResult
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.LocalDateTime

@AndroidEntryPoint
class PostEditFragment : Fragment(), ErrorHandle {

    private var _binding: FragmentPostEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostEditViewModel by viewModels()
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
        findNavController().putNavigationResult(NavigationKey.IS_DELETED_POST, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostEditBinding.inflate(inflater, container, false)
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
                    viewModel.setLoading()
                    var inputStream: InputStream? = null
                    (binding.ivPostWriteImage.drawable as? BitmapDrawable)?.bitmap?.let { bitmap ->
                        inputStream =
                            BitmapCompressor(bitmapToInputStream(bitmap)!!).getCompressedFile().inputStream()
                    }

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
                    if (viewModel.post.value == null) {
                        if (inputStream != null) viewModel.addPost(post, inputStream!!)
                        else viewModel.addPost(post)
                    } else {
                        if (inputStream != null) viewModel.editPost(post, inputStream!!)
                        else viewModel.editPost(post)
                    }
                    dialog.dismiss()
                }
            }
        }

        ivDeleteImage.setOnClickListener {
            deleteImage()
        }

        includePostWriteAppBar.tvPostWriteType.setOnClickListener {
            includePostWriteAppBar.spinnerPostWrite.performClick()
        }
    }

    private fun deleteImage() = with(binding) {
        ivPostWriteImage.setImageBitmap(null)
        cvPostWriteImage.visibility = View.GONE
        cvPostWriteImage.visibility = View.GONE
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            post.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {
                    it?.let { onBind(it) }
                    createDialogBuilder()
                }
        }

        uiState.collectLatestWithLifecycle(lifecycle){
            when(it){
                PostEditUiState.InitUiState -> {}
                PostEditUiState.Success -> {
                    findNavController().putNavigationResult(NavigationKey.IS_DELETED_POST, true)
                    findNavController().popBackStack()
                }

                PostEditUiState.LadingUiState -> {
                    val uploadProgress = CircularProgressDialogFragment()
                    uploadProgress.show(childFragmentManager, "uploadProgress")
                }

                PostEditUiState.ErrorExit -> errorExit(findNavController())
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
        val rotatedBitmap =
            requireContext().contentResolver.openInputStream(url).use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val orientation = getOrientation(inputStream)
                rotateBitmap(bitmap, orientation)
            }

        val width = resources.getDimensionPixelSize(R.dimen.post_image_width)
        val height = resources.getDimensionPixelSize(R.dimen.post_image_height)
        val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, width, height, true)

        val roundedBitmap = getRoundedCornerBitmap(scaledBitmap, 20f)

        binding.ivPostWriteImage.setImageBitmap(roundedBitmap)
        binding.cvPostWriteImage.visibility = View.VISIBLE
        binding.cvPostWriteImage.visibility = View.VISIBLE
    }

    private fun getOrientation(inputStream: InputStream?): Int {
        if (inputStream == null) {
            return ExifInterface.ORIENTATION_UNDEFINED
        }

        val exifInterface = ExifInterface(inputStream)
        return exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
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

    private fun bitmapToInputStream(bitmap: Bitmap): InputStream? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        return if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)) {
            ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        } else null
    }

    private fun initTextEditor() = with(binding) {
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

