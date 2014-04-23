package com.example.languagehelper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AddWordDialog extends DialogFragment implements
		OnEditorActionListener {
	
	public interface AddWordDialogListener {
		public void onFinishEditDialog(String text);
	}
	
	private EditText mEditText;

	public AddWordDialog() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_word_dialog, container);
		mEditText = (EditText) view.findViewById(R.id.txt_new_word);
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		getDialog().setTitle("Add Word");
		mEditText.setOnEditorActionListener(this);

		return view;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			// Return input text to activity
			AddWordDialogListener activity = (AddWordDialogListener) getActivity();
			activity.onFinishEditDialog(mEditText.getText().toString());
			this.dismiss();
			return true;
		}
		return false;
	}
}
