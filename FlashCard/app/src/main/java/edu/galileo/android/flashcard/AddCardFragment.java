package edu.galileo.android.flashcard;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.galileo.android.flashcard.main.adapter.AddCardListener;

public class AddCardFragment extends DialogFragment implements DialogInterface.OnShowListener {

    @Bind(R.id.questionTextView)
    EditText questionTextView;
    @Bind(R.id.answerTextView)
    EditText answerTextView;


    AddCardListener listener;

    public AddCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (AddCardListener) context;

        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement AddCardListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.card_message_title);
                builder.setPositiveButton(R.string.card_message_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.card_message_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_card, null);
        ButterKnife.bind(this,view);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);

        return dialog;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog)getDialog();

        if (dialog != null){
            Button saveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button cancelButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(questionTextView.getText().toString()) &&
                            !TextUtils.isEmpty(answerTextView.getText().toString())){
                        listener.onDialogPositiveClick(questionTextView.getText().toString(),
                                answerTextView.getText().toString());
                        dismiss();
                    }else{
                        String msg = String.format(getString(R.string.card_message_empty),"");
                        answerTextView.setError(msg);
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
}
