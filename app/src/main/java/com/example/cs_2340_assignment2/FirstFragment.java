package com.example.cs_2340_assignment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_assignment2.data.Message;
import com.example.cs_2340_assignment2.data.MessageData;
import com.example.cs_2340_assignment2.data.User;
import com.example.cs_2340_assignment2.data.UserScope;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.databinding.FragmentFirstBinding;
import com.example.cs_2340_assignment2.state.State;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DocumentReference doc = FirebaseFirestore.getInstance().collection("temp").document("users");
//                Map<String, Object> map = new HashMap<>() {
//                    {
//                        put("test", new
//                                User(
//                                1,
//                                "hi",
//                                new ArrayList<>(),
//                                new ArrayList<>(),
//                                new ArrayList<>(),
//                                new ArrayList<>(),
//                                new ArrayList<>(),
//                                UserScope.PRIVATE));
//                    }
//                };
//                doc.set(map);
//
//                map = new HashMap<>() {
//                    {
//                        put("test",
//                                new Message(
//                                1,
//                                new MessageData(new Wrapped(),"hi"),
//                                new ArrayList<>()
//                                )
//                        );
//                    }
//                };
//                doc = FirebaseFirestore.getInstance().collection("temp").document("messages");
//                doc.set(map);

//                State.getInstance().readFromDB();

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}