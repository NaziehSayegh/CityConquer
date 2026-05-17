package com.androidkings.cityconquer.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.androidkings.cityconquer.R;
import com.androidkings.cityconquer.model.User;
import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private EditText friendUsernameInput;
    private Button addFriendBtn;
    private RecyclerView friendsRecycler;
    private FirebaseFirestore db;
    private String currentUserId;
    private List<User> friendsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        friendUsernameInput = findViewById(R.id.friend_username_input);
        addFriendBtn = findViewById(R.id.add_friend_btn);
        friendsRecycler = findViewById(R.id.friends_recycler);
        friendsRecycler.setLayoutManager(new LinearLayoutManager(this));

        addFriendBtn.setOnClickListener(v -> addFriend());
        loadFriends();
    }

    private void addFriend() {
        String username = friendUsernameInput.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Enter a username!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String friendId = queryDocumentSnapshots.getDocuments().get(0).getId();

                    if (friendId.equals(currentUserId)) {
                        Toast.makeText(this, "You can't add yourself!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    db.collection("users").document(currentUserId)
                            .update("friends", com.google.firebase.firestore.FieldValue.arrayUnion(friendId))
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Friend added! ✅", Toast.LENGTH_SHORT).show();
                                friendUsernameInput.setText("");
                                loadFriends();
                            });
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadFriends() {
        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> friendIds = (List<String>) documentSnapshot.get("friends");
                    if (friendIds == null || friendIds.isEmpty()) return;

                    friendsList.clear();
                    for (String friendId : friendIds) {
                        db.collection("users").document(friendId)
                                .get()
                                .addOnSuccessListener(friendDoc -> {
                                    User friend = friendDoc.toObject(User.class);
                                    if (friend != null) {
                                        friend.setId(friendDoc.getId());
                                        friendsList.add(friend);
                                    }
                                });
                    }
                });
    }
}