package com.example.zotfinder20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class Arrival_Dialog extends AppCompatDialogFragment {
    private ArrivalDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder arrival_dialog = new AlertDialog.Builder(getActivity());
        arrival_dialog.setTitle("Instructions")
                .setMessage("If you are searching for a particular classroom, please click [Return] to return to the main menu. Then, select [An Indoor Classroom] or [An Outdoor Classroom] and enter the information.")
                .setPositiveButton("Return to Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onReturnclicked();
                    }
                });
        return arrival_dialog.create();
    }

    public interface ArrivalDialogListener{
        void onReturnclicked();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try{
            listener = (ArrivalDialogListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "Must Implement ArrivalDialogListener");
        };
    }
}
