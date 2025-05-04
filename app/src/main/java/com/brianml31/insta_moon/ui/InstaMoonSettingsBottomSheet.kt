package com.brianml31.insta_moon.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brianml31.insta_moon.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class InstaMoonSettingsBottomSheet : BottomSheetDialogFragment() {

    interface SettingsSaveListener {
        fun onSettingsSaved(options: List<SettingOption>)
    }

    private var saveListener: SettingsSaveListener? = null
    private lateinit var settingsAdapter: SettingsOptionsAdapter
    private var initialOptions: List<SettingOption> = listOf()
    private var title: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    fun setSettingsSaveListener(listener: SettingsSaveListener) {
        this.saveListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE) ?: "Settings"
            initialOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_OPTIONS, SettingOption::class.java) ?: listOf()
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
        return inflater.inflate(R.layout.bottom_sheet_instamoon_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView: TextView = view.findViewById(R.id.bottom_sheet_title)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_settings)
        val closeButton: MaterialButton = view.findViewById(R.id.button_close)
        val saveButton: MaterialButton = view.findViewById(R.id.button_save)

        titleTextView.text = title

        val mutableOptions = initialOptions.map { it.copy() }.toMutableList()
        settingsAdapter = SettingsOptionsAdapter(mutableOptions)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = settingsAdapter

        closeButton.setOnClickListener {
            dismiss()
        }

        saveButton.setOnClickListener {
            saveListener?.onSettingsSaved(settingsAdapter.getUpdatedOptions())
            dismiss()
        }
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_OPTIONS = "options"

        fun newInstance(title: String, options: List<SettingOption>): InstaMoonSettingsBottomSheet {
            val fragment = InstaMoonSettingsBottomSheet()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putParcelableArrayList(ARG_OPTIONS, ArrayList(options))
            fragment.arguments = args
            return fragment
        }
    }
}
