import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.agvber.kekkek.R

class SmokingSettingDialogFragment : DialogFragment() {

    interface OnSaveListener {
        fun onSave(newValue: String)
    }

    private var title: String? = null
    private var currentValue: String? = null
    private var onSaveListener: OnSaveListener? = null

    private lateinit var editText: EditText
    private lateinit var errorText: TextView
    private lateinit var finishButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_common_et_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById(R.id.et_dialog_content)
        errorText = view.findViewById(R.id.tv_common_et_dialog_description)
        finishButton = view.findViewById(R.id.btn_dialog_finish)

        errorText.visibility = View.GONE // 초기에는 에러 메시지를 숨김

        val dialogTitle = view.findViewById<TextView>(R.id.tv_dialog_title)
        val cancelButton = view.findViewById<AppCompatButton>(R.id.btn_dialog_cancel)

        dialogTitle.text = title
        editText.setText(currentValue)

        cancelButton.setOnClickListener {
            dismiss()
        }

        finishButton.setOnClickListener {
            val newValue = editText.text.toString()
            if (isValidInput(newValue)) {
                onSaveListener?.onSave(newValue)
                dismiss()
            } else {
                errorText.visibility = View.VISIBLE
                errorText.text = "100 이하의 숫자를 입력하세요"
            }
        }
    }

    private fun isValidInput(input: String): Boolean {
        return try {
            val intValue = input.toInt()
            intValue in 1..100 // 1부터 100 사이의 숫자만 허용
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setCurrentValue(currentValue: String) {
        this.currentValue = currentValue
    }

    fun setOnSaveListener(onSaveListener: OnSaveListener) {
        this.onSaveListener = onSaveListener
    }

    companion object {
        const val TAG = "SmokingSettingDialogFragment"

        fun newInstance(
            title: String,
            currentValue: String,
            onSaveListener: OnSaveListener
        ): SmokingSettingDialogFragment {
            val fragment = SmokingSettingDialogFragment()
            fragment.setTitle(title)
            fragment.setCurrentValue(currentValue)
            fragment.setOnSaveListener(onSaveListener)
            return fragment
        }
    }
}
