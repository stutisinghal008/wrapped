package com.example.cs_2340_assignment2.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.cs_2340_assignment2.R;

public class CommentDialogFragment extends DialogFragment {

    private static final String ARG_POSITION = "position";

    public static CommentDialogFragment newInstance(int position) {
        CommentDialogFragment fragment = new CommentDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_comment, null);
        EditText editText = view.findViewById(R.id.comment_edit_text);

        builder.setView(view)
                .setTitle("Write a comment")
                .setPositiveButton("Post", (dialog, id) -> {
                    String comment = editText.getText().toString();
                    // Post the comment to your server or handle it accordingly
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        return builder.create();
    }
}
