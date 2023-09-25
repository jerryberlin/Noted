package com.example.noted.ui.detail

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.example.noted.R
import com.example.noted.core.domain.model.Note
import com.example.noted.databinding.ActivityDetailNoteBinding
import com.example.noted.databinding.BottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailNoteActivity : AppCompatActivity() {
    private var color = -1
    private var currentDate = SimpleDateFormat.getInstance().format(Date())
    private val detailNoteViewModel: DetailNoteViewModel by viewModels()
    private val args: DetailNoteActivityArgs by navArgs()
    private var category = ""
    private lateinit var binding: ActivityDetailNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = color

        val noteId = args.noteId
        val categoryName = mutableListOf<String>()

        detailNoteViewModel.category.observe(this) {
            it.forEach { category ->
                if (category.categoryName == "Uncategorized") {
                } else {
                    categoryName.add(category.categoryName)
                }
            }
            if (categoryName.contains("All")) {
                categoryName.removeFirst()
                categoryName.add(0, "Uncategorized")
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryName)
            binding.spinner2.adapter = adapter

            binding.spinner2.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = categoryName[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            if (noteId != 0) {
                detailNoteViewModel.getNoteById(noteId)
                detailNoteViewModel.noteById.observe(this) { note ->
                    note?.let {
                        binding.adEdNoteTitle.text =
                            Editable.Factory.getInstance().newEditable(note.noteTitle)
                        binding.adEdNoteContent.renderMD(note.noteContent)
                        binding.adParent.setBackgroundColor(note.noteColor)
                        binding.spinner2.setSelection(adapter.getPosition(it.categoryName))
                        window.statusBarColor = note.noteColor
                        color = note.noteColor
                        binding.adSaveButton.setOnClickListener {
                            lifecycleScope.launch {
                                detailNoteViewModel.updateNote(
                                    Note(
                                        noteId = noteId,
                                        noteTitle = binding.adEdNoteTitle.text.toString(),
                                        noteContent = binding.adEdNoteContent.getMD(),
                                        categoryName = category,
                                        noteColor = color,
                                        noteDate = currentDate
                                    )
                                )
                            }
                            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                binding.spinner2.setSelection(adapter.getPosition(args.categoryName))
                binding.adSaveButton.setOnClickListener {
                    lifecycleScope.launch {
                        detailNoteViewModel.insertNote(
                            Note(
                                noteTitle = binding.adEdNoteTitle.text.toString(),
                                noteContent = binding.adEdNoteContent.getMD(),
                                noteDate = currentDate,
                                noteColor = color,
                                categoryName = if (category == "Uncategorized") "All" else category
                            )
                        )
                    }
                    Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show()
                }
            }


        }

        try {
            binding.adEdNoteContent.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    binding.adBottomBar.visibility = View.VISIBLE
                    binding.adEdNoteContent.setStylesBar(binding.adStyleBar)
                } else {
                    binding.adBottomBar.visibility = View.GONE
                }
            }
        } catch (e: Throwable) {
            Log.d("TAG", "onCreate: ${e.stackTrace}")
        }

        binding.adColorButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                this,
                R.style.BottomSheetDialogTheme
            )
            val bottomSheetView: View = layoutInflater.inflate(
                R.layout.bottom_sheet_layout,
                null
            )
            with(bottomSheetDialog)
            {
                setContentView(bottomSheetView)
                show()
            }
            val bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)
            bottomSheetBinding.apply {
                bottomSheetColorPicker.apply {
                    setSelectedColor(color)
                    setOnColorSelectedListener { value ->
                        color = value
                        binding.apply {
                            adParent.setBackgroundColor(color)
                        }
                        bottomSheetBinding.bottomSheetParent.setCardBackgroundColor(color)
                        bottomSheetBar.setBackgroundColor(color)
                        window.statusBarColor = color
                    }
                }
                bottomSheetParent.setCardBackgroundColor(color)
            }
            bottomSheetView.post {
                bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.adBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}