package com.mohammad_nazieh_amro.cityconquer.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    // Debounce handler for live search
    private final Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private static final int SEARCH_DEBOUNCE_MS = 400;

    // The found user from live search (to be added on button press)
    private String foundUserId = null;
    private String foundUsername = null;

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

        // Live search as the user types — debounced by 400ms
        friendUsernameInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel any pending search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                foundUserId = null;
                foundUsername = null;

                String query = s.toString().trim();
                if (query.length() < 2) {
                    // Hide status when query is too short
                    searchStatusText.setVisibility(View.GONE);
                    return;
                }

                // Show "searching..." immediately
                searchStatusText.setVisibility(View.VISIBLE);
                searchStatusText.setTextColor(0x88FFFFFF);
                searchStatusText.setText("🔍  Searching...");

                // Debounce: fire search 400ms after user stops typing
                searchRunnable = () -> liveSearchUser(query);
                searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_MS);
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        // Allow pressing Search on keyboard to add the found user
        friendUsernameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                addFoundFriend();
                return true;
            }
            return false;
        });

        // Add button fires the add action (uses cached foundUserId if available)
        addFriendBtn.setOnClickListener(v -> addFoundFriend());

        loadFriends();
        return view;
    }

    /**
     * Searches Firestore for a user matching the prefix query.
     * Shows the result inline below the search bar.
     */
    private void liveSearchUser(String query) {
        if (getContext() == null) return;

        // Firestore doesn't support prefix search natively, so we use range query trick:
        // query >= "abc" && query < "abd" to match any username starting with "abc"
        String endQuery = query.substring(0, query.length() - 1)
                + (char)(query.charAt(query.length() - 1) + 1);

        db.collection("users")
                .whereGreaterThanOrEqualTo("username", query)
                .whereLessThan("username", endQuery)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshots -> {
                    if (getContext() == null) return;
                    searchStatusText.setVisibility(View.VISIBLE);

                    if (snapshots.isEmpty()) {
                        foundUserId = null;
                        foundUsername = null;
                        searchStatusText.setTextColor(0xFFFF5252);
                        searchStatusText.setText("❌  No user found matching \"" + query + "\"");
                        return;
                    }

                    String uid = snapshots.getDocuments().get(0).getId();
                    String username = snapshots.getDocuments().get(0).getString("username");

                    if (uid.equals(currentUserId)) {
                        foundUserId = null;
                        foundUsername = null;
                        searchStatusText.setTextColor(0xFFFFAA00);
                        searchStatusText.setText("⚠  That's you!");
                        return;
                    }

                    // Check if already a friend
                    boolean alreadyFriend = false;
                    for (User u : friendsList) {
                        if (u.getId() != null && u.getId().equals(uid)) {
                            alreadyFriend = true;
                            break;
                        }
                    }

                    foundUserId = uid;
                    foundUsername = username;

                    if (alreadyFriend) {
                        searchStatusText.setTextColor(0xFF00BCD4);
                        searchStatusText.setText("✅  " + username + " is already your friend!");
                    } else {
                        searchStatusText.setTextColor(0xFF00E676);
                        searchStatusText.setText("👤  Found: " + username + "  — press Add to add!");
                    }
                })
                .addOnFailureListener(e -> {
                    if (getContext() == null) return;
                    searchStatusText.setVisibility(View.VISIBLE);
                    searchStatusText.setTextColor(0xFFFF5252);
                    searchStatusText.setText("❌  Error: " + e.getMessage());
                });
    }

    /**
     * Adds the currently found user (from live search) as a friend.
     */
    private void addFoundFriend() {
        String query = friendUsernameInput.getText().toString().trim();
        if (query.isEmpty()) {
            searchStatusText.setVisibility(View.VISIBLE);
            searchStatusText.setTextColor(0xFFFFAA00);
            searchStatusText.setText("⚠  Enter a username to search.");
            return;
        }

        if (foundUserId == null) {
            // Trigger a fresh search first
            liveSearchUser(query);
            searchStatusText.setVisibility(View.VISIBLE);
            searchStatusText.setTextColor(0xFFFFAA00);
            searchStatusText.setText("⚠  Wait for search result before adding.");
            return;
        }

        // Check if already a friend
        for (User u : friendsList) {
            if (u.getId() != null && u.getId().equals(foundUserId)) {
                searchStatusText.setVisibility(View.VISIBLE);
                searchStatusText.setTextColor(0xFF00BCD4);
                searchStatusText.setText("✅  " + foundUsername + " is already your friend!");
                return;
            }
        }

        addFriendBtn.setEnabled(false);
        db.collection("users").document(currentUserId)
                .update("friends", FieldValue.arrayUnion(foundUserId))
                .addOnSuccessListener(unused -> {
                    if (getContext() == null) return;
                    addFriendBtn.setEnabled(true);
                    searchStatusText.setVisibility(View.VISIBLE);
                    searchStatusText.setTextColor(0xFF00E676);
                    searchStatusText.setText("✅  " + foundUsername + " added successfully!");
                    friendUsernameInput.setText("");
                    foundUserId = null;
                    foundUsername = null;
                    loadFriends();
                })
                .addOnFailureListener(e -> {
                    if (getContext() == null) return;
                    addFriendBtn.setEnabled(true);
                    searchStatusText.setVisibility(View.VISIBLE);
                    searchStatusText.setTextColor(0xFFFF5252);
                    searchStatusText.setText("❌  Failed: " + e.getMessage());
                });
    }

    private void loadFriends() {
        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (getContext() == null) return;
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
                                    if (getContext() == null) return;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel any pending searches when view is destroyed
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}