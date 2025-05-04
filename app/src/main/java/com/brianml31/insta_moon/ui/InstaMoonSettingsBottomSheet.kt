package com.brianml31.insta_moon.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brianml31.insta_moon.R // R dosyasını import etmeyi unutma
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
         if (parentFragment is SettingsSaveListener) {
            saveListener = parentFragment as SettingsSaveListener
        } else if (context is SettingsSaveListener) {
            saveListener = context as SettingsSaveListener
        }
    }

    fun setSettingsSaveListener(listener: SettingsSaveListener) {
        this.saveListener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve arguments
        title = arguments?.getString(ARG_TITLE) ?: "Settings"
        // Retrieve options (assuming they are passed correctly, e.g., via ViewModel or static workaround)
        // initialOptions = arguments?.getParcelableArrayList(ARG_OPTIONS) ?: listOf()

        return inflater.inflate(R.layout.bottom_sheet_instamoon_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView: TextView = view.findViewById(R.id.bottom_sheet_title)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_settings)
        val closeButton: MaterialButton = view.findViewById(R.id.button_close)
        val saveButton: MaterialButton = view.findViewById(R.id.button_save)

        titleTextView.text = title

        // Create a mutable copy for the adapter
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

        // Need a reliable way to pass options (ViewModel preferred)
        private var tempOptions: List<SettingOption> = listOf()

        fun newInstance(title: String, options: List<SettingOption>): InstaMoonSettingsBottomSheet {
             val fragment = InstaMoonSettingsBottomSheet()
             val args = Bundle()
             args.putString(ARG_TITLE, title)
             // Parcelable/Serializable or ViewModel needed here for proper argument passing
             tempOptions = options // Temporary workaround
             fragment.arguments = args
             return fragment
        }

         fun getTempOptions(): List<SettingOption> {
            return tempOptions
        }
    }
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve options using the temporary workaround
         if (initialOptions.isEmpty()) {
             initialOptions = getTempOptions()
         }
    }
}
