package com.goosebyte.containment;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.goosebyte.containment.GobalValues.USER_EMAILADDRESS_SUFFIX;
import static com.goosebyte.containment.GobalValues.USER_PASSWORD_SUFFIX;
import static com.parse.Parse.getApplicationContext;

public class ParseUtils {

    private static String createUserException ="";
    private static String userLoginException = "";

    public static boolean createUser(String userName, String password, String email) {
        boolean createUser = false;
        try {
            createUserException = "";
            ParseUser user = new ParseUser();
            user.setUsername(userName);
            user.setPassword(password);
            user.setEmail(email);
            // Other fields can be set just like any other ParseObject,
            // using the "put" method, like this: user.put("attribute", "its value");
            // If this field does not exists, it will be automatically created
            user.signUp();
            createUser = true;

        } catch (Exception x) {
            createUserException = x.getMessage();
        }
        return createUser;
    }

    public static List<ParseObject> getScores(int limit, int skip) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
            query.setLimit(limit);
            query.setSkip(skip);
            query.orderByDescending("year").addDescendingOrder("population");
            return query.find();

        } catch (Exception x) {
            return null;
        }
    }



    public static void updateScoreRecord(int year, int population) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            List<ParseObject> results = query.find();
            if (results != null && !results.isEmpty()){
                //Check if higher and update if so
                if (year == (Integer) results.get(0).get("year")) {
                    if (population > (Integer) results.get(0).get("population")) {
                        results.get(0).put("population", population);
                        results.get(0).save();
                    }
                } else {
                    if (year > (Integer) results.get(0).get("year")) {
                        results.get(0).put("year", year);
                        results.get(0).put("population", population);
                        results.get(0).save();
                    }
                }
            } else {
                ParseObject entity = new ParseObject("Score");
                entity.put("username", ParseUser.getCurrentUser().getUsername());
                entity.put("year", year);
                entity.put("population", population);
                entity.save();
            }

        } catch (Exception x) {
            updateScoreRecord(year,population);
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

    public static boolean loginUser(String userName, String password) {
        boolean userLoggedIn = false;
        try {
            userLoginException = "";
            ParseUser.logIn(userName,password);
            userLoggedIn = true;
        } catch (Exception x) {
            userLoginException = x.getMessage();
        }
        return userLoggedIn;
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
                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
                        final TextView infoErrorTextView = dialogView.findViewById(R.id.infoErrorTextView);

                        nameEditText.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s){}

                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                infoErrorTextView.setText("");
                            }
                        });

                        String name = nameEditText.getText().toString();
                        if (!name.isEmpty()) {
                            infoErrorTextView.setTextColor(context.getResources().getColor(R.color.busyText));
                            infoErrorTextView.setText(R.string.onemoment);
                            nameEditText.setEnabled(false);

                            Boolean userNameExists = userNameAlreadyExists(nameEditText.getText().toString());
                            if (userNameExists != null) {
                                if (!userNameExists) {

                                    String emailAddress= nameEditText.getText().toString() + USER_EMAILADDRESS_SUFFIX;
                                    String password = nameEditText.getText().toString() + USER_PASSWORD_SUFFIX;

                                    if (createUser(nameEditText.getText().toString(),password,emailAddress)) {
                                        SharedPrefs.createSharedPrefs(context,nameEditText.getText().toString());

                                        if (loginUser(nameEditText.getText().toString(), password)) {
                                            dialog.dismiss();
                                        } else {
                                            infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                                            infoErrorTextView.setText(R.string.loginfailed);
                                            nameEditText.setEnabled(true);
                                            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                            Toast.makeText(getApplicationContext(), "Login Failed : " + userLoginException, Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                                        infoErrorTextView.setText(R.string.signuperror);
                                        nameEditText.setEnabled(true);
                                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                        Toast.makeText(getApplicationContext(), "Create User Failed : " + createUserException, Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                                    infoErrorTextView.setText(R.string.namealreadyinuse);
                                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                    nameEditText.setEnabled(true);
                                }

                            } else {
                                infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                                infoErrorTextView.setText(R.string.backendfailure);
                                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                nameEditText.setEnabled(true);
                            }

                        } else {
                            infoErrorTextView.setTextColor(context.getResources().getColor(R.color.errorText));
                            infoErrorTextView.setText(R.string.namenotblank);
                            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });
            }
        });
        dialog.show();

    }







}
