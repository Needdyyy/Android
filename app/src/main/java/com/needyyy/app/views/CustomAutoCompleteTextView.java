package com.needyyy.app.views;

import android.content.Context;
import android.util.AttributeSet;

import java.util.HashMap;

/** Customizing AutoCompleteTextView to return Place Description   
 *  corresponding to the selected item
 */
public class CustomAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView
{
	
	public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** Returns the place description corresponding to the selected item */
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem)
	{
		/** Each item in the autocompetetextview suggestion list is a hashmap object */
		HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
		return hm.get("description");
	}
	
}
