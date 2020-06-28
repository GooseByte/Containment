package com.goosebyte.containment;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.LogInCallback;

import java.util.List;

import static com.goosebyte.containment.GobalValues.USER_EMAILADDRESS_SUFFIX;
import static com.goosebyte.containment.GobalValues.USER_PASSWORD_SUFFIX;
import static com.parse.Parse.getApplicationContext;

public class ParseUtils {

    private static Boolean signUpResult = null;

    public static void createUser(String userName, String password, String email) {
        signUpResult = null;

        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);
        // Other fields can be set just like any other ParseObject,
        // using the "put" method, like this: user.put("attribute", "its value");
        // If this field does not exists, it will be automatically created

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    signUpResult = true;
                } else {
                    signUpResult = false;
                    String data = "Create User Failed : " + e.getMessage();
                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                }
            }
        });

        while (signUpResult == null) {
        }

    }

    public static Boolean userNameAlreadyExists(String userName)
    {
        Boolean result = null;
        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", userName);
            List<ParseUser> matchingUsers = query.find();
            if (!matchingUsers.isEmpty()) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception x) {
            Log.e("ParseUtils",x.getMessage());
        }
        return result;
    }

    public void loginUser() {
        ParseUser.logInInBackground("<userName>", "<password>", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });

        //TODO: WaitUntilResponse

    }

    public void getCurrentUser() {
        // After login, Parse will cache it on disk, so
        // we don't need to login every time we open this
        // application
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
        } else {
            // show the signup or login screen
        }
    }

    public static void createParseUserDialog(final Context context) {

        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.newplayerdialog, null);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("New Player")
                .setMessage("What is your name?")
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
                        TextView infoErrorTextView = dialogView.findViewById(R.id.infoErrorTextView);

                        String name = nameEditText.getText().toString();
                        if (!name.isEmpty()) {
                            infoErrorTextView.setTextColor(context.getResources().getColor(R.color.busyText));
                            infoErrorTextView.setText(R.string.checknameavailable);
                            nameEditText.setEnabled(false);

                            Boolean userNameExists = userNameAlreadyExists(nameEditText.getText().toString());
                            if (userNameExists != null) {
                                if (!userNameExists) {
                                    String emailAddress= nameEditText.getText().toString() + USER_EMAILADDRESS_SUFFIX;
                                    String password = nameEditText.getText().toString() + USER_PASSWORD_SUFFIX;

                                    createUser(nameEditText.getText().toString(),password,emailAddress);
                                    if (signUpResult != null && signUpResult == true) {
                                        //TODO: Check SharedPrefs created
                                        SharedPrefs.createSharedPrefs(context,nameEditText.getText().toString());
                                        dialog.dismiss();
                                        //TODO: Restart

                                    } else {
                                        infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                                        infoErrorTextView.setText(R.string.signuperror);
                                        nameEditText.setEnabled(true);
                                    }

                                } else {
                                    infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                                    infoErrorTextView.setText(R.string.namealreadyinuse);
                                    nameEditText.setEnabled(true);
                                }

                            } else {
                                infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                                infoErrorTextView.setText(R.string.backendfailure);
                                nameEditText.setEnabled(true);
                            }

                        } else {
                            infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                            infoErrorTextView.setText(R.string.namenotblank);
                        }
                        }
                });
            }
        });
        dialog.show();
    }

}
