package com.example.notes_cm.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes_cm.R
import com.example.notes_cm.data.entities.Note
import com.example.notes_cm.data.vm.NoteViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateFragment : Fragment() {
    private  val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mNoteViewModel: NoteViewModel
    private var updateNoteDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mNoteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        view.findViewById<TextView>(R.id.updateNote).text = args.currentNote.note

        val updateButton = view.findViewById<Button>(R.id.update)
        updateButton.setOnClickListener {
            updateNote()
        }

        val deleteButton = view.findViewById<Button>(R.id.delete)
        deleteButton.setOnClickListener {
            deleteNote()
        }

        val backButton = view.findViewById<Button>(R.id.backToListFromUpdate)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        val dateButton = view.findViewById<Button>(R.id.updateDate)
        dateButton.setOnClickListener {
            showDateModal()
        }

        return  view
    }

    private fun showDateModal() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            updateNoteDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selection))
        }
        datePicker.show(childFragmentManager, "datePicker")
    }

    private  fun updateNote(){
        val updatedNote = view?.findViewById<EditText>(R.id.updateNote)?.text.toString()

        if(updatedNote.isEmpty()) {
            makeText(context , "Não pode uma nota vazia!", Toast.LENGTH_LONG).show()
        }
        else {
            if (updateNoteDate.isEmpty()) {
                val updatedNote1 = Note(args.currentNote.id, updatedNote, args.currentNote.date)
                mNoteViewModel.updateNote(updatedNote1)
            } else {
                val updatedNote2 = Note(args.currentNote.id, updatedNote, updateNoteDate)
                mNoteViewModel.updateNote(updatedNote2)
            }

            makeText(requireContext(), getString(R.string.text_update_complete), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Sim") { _, _ ->
            mNoteViewModel.deleteNote(args.currentNote)
            makeText(
                requireContext(),
                "Nota apagada com sucesso!",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Não") { _, _ -> }
        builder.setTitle("Remover")
        builder.setMessage("Tem a certeza que deseja remover a Nota?")
        builder.create().show()
    }
}