package com.associates.neesarg.siren;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.associates.neesarg.siren.R;

/*****************************************************************************************
 * Created by Neesarg Patel on 7/18/2015.
 *
 * Dialog Fragment to Ask the User the name of the Commute that was just completed
 * This Dialog Fragment will only open if the specific commute was never completed before
 * ie. if the start point and end point of this trip is not found, the app considers this
 *     a new trip with a new name
 *
 * See Figure 7
 *
 ******************************************************************************************/
public class AskNameDialogFragment extends DialogFragment {

    public interface NoticeDialogListener {
        public void onAskNamePositiveClick(String routeName);       // Method to execute when Save Commute button is pressed
        public void onAskNameNegativeClick();  // Method to execute when Cancel button is pressed
    }

    NoticeDialogListener mListener;
    EditText inputName;

    // Make sure the Activity implemented the interface
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.mListener = (NoticeDialogListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        // Inflate and set the layout for the dialog
        View view = (View) inflater.inflate(R.layout.text_prompt, null);
        inputName = (EditText) view.findViewById(R.id.set_ip_address_field);                          // Text input field
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mListener.onAskNamePositiveClick(String.valueOf(inputName.getText()));      // Send value in text input field
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {              // Ask the User if they are sure they want to cancel
            public void onClick(DialogInterface dialog, int id) {
                mListener.onAskNameNegativeClick();
            }
        });
        return builder.create();
    }
}