package com.mohammad_nazieh_amro.cityconquer.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.model.User;
import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private EditText friendUsernameInput;
    private Button addFriendBtn;
    private RecyclerView friendsRecycler;
    private TextView searchStatusText, friendsCountBadge;
    private LinearLayout emptyState;
    private FirebaseFirestore db;
    private String currentUserId;
    private List<User> friendsList = new ArrayList<>();
    private com.mohammad_nazieh_amro.cityconquer.adapter.FriendsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        friendUsernameInput = view.findViewById(R.id.friend_username_input);
        addFriendBtn = view.findViewById(R.id.add_friend_btn);
        friendsRecycler = view.findViewById(R.id.friends_recycler);
        searchStatusText = view.findViewById(R.id.search_status_text);
        friendsCountBadge = view.findViewById(R.id.friends_count_badge);
        emptyState = view.findViewById(R.id.empty_state);

        friendsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new com.mohammad_nazieh_amro.cityconquer.adapter.FriendsAdapter(friendsList);
        friendsRecycler.setAdapter(adapter);

        // Clear status text when typing
        friendUsernameInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchStatusText.setVisibility(View.GONE);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Allow pressing search on keyboard to trigger add
        friendUsernameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                addFriend();
                return true;
            }
            return false;
        });

        addFriendBtn.setOnClickListener(v -> addFriend());
        loadFriends();
        return view;
    }

    private void addFriend() {
        String username = friendUsernameInput.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            searchStatusText.setVisibility(View.VISIBLE);
            searchStatusText.setText("⚠ Enter a username to search.");
            return;
        }

        searchStatusText.setVisibility(View.VISIBLE);
        searchStatusText.setText("Searching...");
        addFriendBtn.setEnabled(false);

        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    addFriendBtn.setEnabled(true);
                    if (queryDocumentSnapshots.isEmpty()) {
                        searchStatusText.setText("❌ No user found with that username.");
                        return;
                    }
                    String friendId = queryDocumentSnapshots.getDocuments().get(0).getId();
                    if (friendId.equals(currentUserId)) {
                        searchStatusText.setText("⚠ You can't add yourself!");
                        return;
                    }
                    // Check if already friends
                    boolean alreadyFriend = false;
                    for (User u : friendsList) {
                        if (u.getId() != null && u.getId().equals(friendId)) {
                            alreadyFriend = true;
                            break;
                        }
                    }
                    if (alreadyFriend) {
                        searchStatusText.setText("✅ Already in your friends list!");
                        return;
                    }
                    db.collection("users").document(currentUserId)
                            .update("friends", FieldValue.arrayUnion(friendId))
                            .addOnSuccessListener(unused -> {
                                searchStatusText.setText("✅ Friend added successfully!");
                                friendUsernameInput.setText("");
                                loadFriends();
                            })
                            .addOnFailureListener(e -> {
                                searchStatusText.setText("❌ Failed: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    addFriendBtn.setEnabled(true);
                    searchStatusText.setText("❌ Error: " + e.getMessage());
                });
    }

    private void loadFriends() {
        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> friendIds = (List<String>) documentSnapshot.get("friends");
                    if (friendIds == null || friendIds.isEmpty()) {
                        friendsList.clear();
                        adapter.updateList(friendsList);
                        updateEmptyState();
                        return;
                    }
                    friendsList.clear();
                    final int totalFriends = friendIds.size();
                    final java.util.concurrent.atomic.AtomicInteger loadedCount = new java.util.concurrent.atomic.AtomicInteger(0);
                    for (String friendId : friendIds) {
                        db.collection("users").document(friendId)
                                .get()
                                .addOnSuccessListener(friendDoc -> {
                                    User friend = friendDoc.toObject(User.class);
                                    if (friend != null) {
                                        friend.setId(friendDoc.getId());
                                        friendsList.add(friend);
                                    }
                                    if (loadedCount.incrementAndGet() == totalFriends) {
                                        adapter.updateList(friendsList);
                                        updateEmptyState();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    if (loadedCount.incrementAndGet() == totalFriends) {
                                        adapter.updateList(friendsList);
                                        updateEmptyState();
                                    }
                                });
                    }
                });
    }

    private void updateEmptyState() {
        int count = friendsList.size();
        if (count == 0) {
            emptyState.setVisibility(View.VISIBLE);
            friendsRecycler.setVisibility(View.GONE);
            friendsCountBadge.setText("0 Friends");
        } else {
            emptyState.setVisibility(View.GONE);
            friendsRecycler.setVisibility(View.VISIBLE);
            friendsCountBadge.setText(count + (count == 1 ? " Friend" : " Friends"));
        }
    }
}