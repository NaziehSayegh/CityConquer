package com.mohammad_nazieh_amro.cityconquer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.adapter.LeaderboardAdapter;
import com.mohammad_nazieh_amro.cityconquer.model.User;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private RecyclerView leaderboardRecycler;
    private FirebaseFirestore db;
    private List<User> userList = new ArrayList<>();
    private LeaderboardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        db = FirebaseFirestore.getInstance();
        leaderboardRecycler = view.findViewById(R.id.leaderboard_recycler);
        leaderboardRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LeaderboardAdapter(userList);
        leaderboardRecycler.setAdapter(adapter);

        loadLeaderboard();
        return view;
    }

    private void loadLeaderboard() {
        db.collection("users")
                .orderBy("totalXP", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (var doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        user.setId(doc.getId());
                        userList.add(user);
                    }
                    adapter.updateList(userList);
                });
    }
}