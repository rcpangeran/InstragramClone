package com.learn.apinstragramclone;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserTabFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    public UserTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_tab, container, false);

        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);
        final TextView txtUserLoading = view.findViewById(R.id.txtUsersLoading);

        listView.setOnItemClickListener(UserTabFragment.this);
        listView.setOnItemLongClickListener(UserTabFragment.this);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    if (users.size() > 0) {
                        for (ParseUser user : users) {
                            arrayList.add(user.getUsername());
                        }

                        listView.setAdapter(arrayAdapter);
                        txtUserLoading.animate().alpha(0).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getContext(), UsersPostsActivity.class);
        intent.putExtra("username", arrayList.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(position));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (user != null && e == null) {
                    // FancyToast.makeText(getContext(), user.get("profileProfession") + "", Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());

                    prettyDialog.setTitle(user.getUsername() + "'s Info")
                            .setMessage(user.get("profileBio") + "\n"
                                + user.get("profileProfession") + "\n"
                                + user.get("profileHobbies") + "\n"
                                + user.get("profileFavSport"))
                            .setIcon(R.drawable.ic_person)
                            .addButton(
                                    "OK",                      // button text
                                    R.color.pdlg_color_white,       // button text color
                                    R.color.pdlg_color_green,       // button background color
                                    new PrettyDialogCallback() {    // button OnClick listener
                                        @Override
                                        public void onClick() {
                                            // Do what you gotta do
                                            prettyDialog.dismiss();
                                        }
                                    })
                            .show();

                }

            }
        });

        return true;
    }
}
