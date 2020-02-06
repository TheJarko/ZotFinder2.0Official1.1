package com.example.zotfinder20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class Safety_Dialog extends AppCompatDialogFragment {

    private SafetyDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder safety_dialogue = new AlertDialog.Builder(getActivity());
        safety_dialogue.setTitle("Safety Notice")
                        .setMessage("Please keep aware of your surroundings and exercise caution while traversing the campus. When you arrive to your destination, please click on the [I have arrived] button for further instructions.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onOKclicked();
                            }
                        });
        return safety_dialogue.create();
    }

    public interface SafetyDialogListener{
        void onOKclicked();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try{
            listener = (SafetyDialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Must Implement SafetyDialogListener");

        };

    }

}
