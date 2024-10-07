package com.example.hiveeapp.company_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.invitations.InvitationAdapter;
import com.example.hiveeapp.company_user.invitations.InvitationApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InvitationsFragment extends Fragment {

    private RecyclerView invitationsRecyclerView;
    private InvitationAdapter invitationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invitations, container, false);

        invitationsRecyclerView = view.findViewById(R.id.invitationsRecyclerView);
        invitationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter without edit/delete buttons
        invitationAdapter = new InvitationAdapter(getContext(), false); // false indicates read-only
        invitationsRecyclerView.setAdapter(invitationAdapter);

        getInvitations();

        return view;
    }

    private void getInvitations() {
        InvitationApi.getInvitations(
                getContext(),
                response -> {
                    invitationAdapter.setInvitations(response);
                },
                error -> {
                    // Handle error
                }
        );
    }
}
