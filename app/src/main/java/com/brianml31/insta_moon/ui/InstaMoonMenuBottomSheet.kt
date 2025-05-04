package com.brianml31.insta_moon.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brianml31.instamoon.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class InstaMoonMenuBottomSheet : BottomSheetDialogFragment() {

    interface MenuOptionClickListener {
        fun onMenuOptionClicked(optionId: Int)
    }

    private var listener: MenuOptionClickListener? = null
    private var options: List<MenuOption> = listOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    fun setMenuOptionClickListener(listener: MenuOptionClickListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
             options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_OPTIONS, MenuOption::class.java) ?: listOf()
             } else {
                @Suppress("DEPRECATION")
                it.getParcelableArrayList(ARG_OPTIONS) ?: listOf()
             }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_instamoon_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        fun newInstance(options: List<MenuOption>): InstaMoonMenuBottomSheet {
            val fragment = InstaMoonMenuBottomSheet()
            val args = Bundle()

            args.putParcelableArrayList(ARG_OPTIONS, ArrayList(options))
            fragment.arguments = args
            return fragment
        }
    }
}
