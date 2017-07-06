package edu.galileo.android.flashcard.detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.galileo.android.flashcard.R;
import edu.galileo.android.flashcard.data.CardListContract;
import edu.galileo.android.flashcard.main.MainActivity;

public class DetailCardFragment extends DialogFragment {

    @Bind(R.id.questionTextView)
    TextView questionTextView;
    @Bind(R.id.answerTextView)
    TextView answerTextView;

    private String question;
    private String answer;

    public DetailCardFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.detail_message_title)
                .setNeutralButton(R.string.detail_message_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_detail_card, null);
        ButterKnife.bind(this, view);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        setTextViews();

        return dialog;
    }

    private void setTextViews() {
        questionTextView.setText(question + "?");
        answerTextView.setText(answer);
    }

    @Override
    public void setArguments(Bundle args) {
        if (args != null){
            question = args.getString(MainActivity.QUESTION);
            answer = args.getString(MainActivity.ANSWER);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
