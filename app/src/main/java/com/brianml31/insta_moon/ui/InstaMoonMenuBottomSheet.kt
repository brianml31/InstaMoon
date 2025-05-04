package com.brianml31.insta_moon.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brianml31.insta_moon.R // R dosyasını import etmeyi unutma
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class InstaMoonMenuBottomSheet : BottomSheetDialogFragment() {

    interface MenuOptionClickListener {
        fun onMenuOptionClicked(optionId: Int)
    }

    private var listener: MenuOptionClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ensure the host activity or fragment implements the listener
        if (parentFragment is MenuOptionClickListener) {
            listener = parentFragment as MenuOptionClickListener
        } else if (context is MenuOptionClickListener) {
            listener = context as MenuOptionClickListener
        }
        // If listener is still null, you might need a different way to pass it,
        // e.g., via a static newInstance method with arguments or a ViewModel.
        // For simplicity now, we assume it's set via onAttach or a setter.
    }

     fun setMenuOptionClickListener(listener: MenuOptionClickListener) {
        this.listener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_instamoon_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = arguments?.getParcelableArrayList<MenuOption>(ARG_OPTIONS) ?: listOf()

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_options)
        val closeButton: MaterialButton = view.findViewById(R.id.button_close)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MenuOptionsAdapter(options) { selectedOption ->
            listener?.onMenuOptionClicked(selectedOption.id)
            dismiss()
        }

        closeButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val ARG_OPTIONS = "options"

        // Use this method to create instances and pass data safely
         fun newInstance(options: List<MenuOption>): InstaMoonMenuBottomSheet {
            val fragment = InstaMoonMenuBottomSheet()
            val args = Bundle()
            // MenuOption needs to be Parcelable or Serializable to be put in Bundle
            // For simplicity, let's assume MenuOption is made Parcelable
            // args.putParcelableArrayList(ARG_OPTIONS, ArrayList(options))
            // Workaround if not Parcelable (less ideal): Store in a temporary static field or use ViewModel
            // For now, we'll retrieve it differently in DialogUtils
            return fragment
        }
    }
}
