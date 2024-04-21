package com.example.cs_2340_assignment2.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.cs_2340_assignment2.R;
import com.example.cs_2340_assignment2.state.State;

public class ThirdFragment extends Fragment {

    private TextView username;
    OnMyButtonClickListener myButtonCallback;
    OnMyButtonTwoClickListener myButtonTwoCallback;
    OnMyButtonThreeClickListener myButtonThreeCallback;

    private TextView id;


    // Interface for callback to the activity
    public interface OnMyButtonClickListener {
        void onMyButtonClick(String selectedTerm);
    }

    public interface OnMyButtonTwoClickListener {
        void onMyButtonTwoClick(String selectedTerm);
    }

    public interface OnMyButtonThreeClickListener {
        void onMyButtonThreeClick(String selectedTerm);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            myButtonCallback = (OnMyButtonClickListener) context;
            myButtonTwoCallback = (OnMyButtonTwoClickListener) context;
            myButtonThreeCallback = (OnMyButtonThreeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMyButtonClickListener and OnMyButtonTwoClickListener and OnMyButtonThreeClickListener");
        }
    }
    String selectedTerm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        Spinner termSpinner = view.findViewById(R.id.termSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.term_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(adapter);

        id = view.findViewById(R.id.textView7);
        id.setText(State.getInstance().getCurrentUser());
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTerm = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTerm = null; // Handle the case where no item is selected
            }
        });

        // code for settings modal to pop up
        ImageButton openModalButton = view.findViewById(R.id.settings);
        openModalButton.setOnClickListener(v -> {
            DialogFragment dialog = new MyDialogFragment();
            dialog.show(getChildFragmentManager(), "MyDialogFragment");
        });

        // click listener for myButton to open YearlyOverviewFragment
        ImageButton myButton = view.findViewById(R.id.myButton);
        myButton.setOnClickListener(v -> myButtonCallback.onMyButtonClick(selectedTerm));

        ImageButton myButton1 = view.findViewById(R.id.myButton1);
        myButton1.setOnClickListener(v -> myButtonThreeCallback.onMyButtonThreeClick(selectedTerm));

        ImageButton myButton2 = view.findViewById(R.id.myButton2);
        myButton2.setOnClickListener(v -> myButtonTwoCallback.onMyButtonTwoClick(selectedTerm));

        return view;
    }
}
