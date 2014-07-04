/*
 * BaseDialog
 * 
 * 0.2
 * 
 * 2014/05/16
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package r2b.apps.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

/**
 * A fragment that handles a dialog that can show a title, up to three 
 * buttons, a traditional single choice list, a list of selectable items 
 * (single with radio or multiple choice with checkbox), or a custom layout.
 */
public class BaseDialog extends android.support.v4.app.DialogFragment {

	private static final String ICON_RES_KEY = "ICON_RES_KEY";
	private static final String TITLE_KEY = "TITLE_KEY";
	private static final String TEXT_KEY = "TEXT_KEY";
	private static final String IS_CANCELABLE_KEY = "IS_CANCELABLE_KEY";
	private static final String CUSTOM_TITLE_VIEW_RES_KEY = "CUSTOM_TITLE_VIEW_RES_KEY";
	private static final String VIEW_RES_KEY = "VIEW_RES_KEY";
	private static final String POSITIVE_BUTTON_TEXT_RES = "POSITIVE_BUTTON_TEXT_RES";
	private static final String NEGATIVE_BUTTON_TEXT_RES = "NEGATIVE_BUTTON_TEXT_RES";
	private static final String NEUTRAL_BUTTON_TEXT_RES = "NEUTRAL_BUTTON_TEXT_RES";
	private static final String LIST_ARRAY_RES = "LIST_ARRAY_RES";
	private static final String SINGLE_CHOICE_LIST_ARRAY_RES = "SINGLE_CHOICE_LIST_ARRAY_RES";
	private static final String SINGLE_CHOICE_DEFAULT_SELECTED = "SINGLE_CHOICE_DEFAULT_SELECTED";
	private static final String MULTI_CHOICE_LIST_ARRAY_RES = "MULTI_CHOICE_LIST_ARRAY_RES";
	private static final String MULTI_CHOICE_DEFAULT_SELECTED = "MULTI_CHOICE_DEFAULT_SELECTED";
	
	private int iconRes;
	private String title;
	private String text;
	private boolean isCancelable;
	private int customTitleViewRes;
	private int viewRes;
	private int positiveButonTextRes;
	private int negativeButonTextRes;
	private int neutralButonTextRes;
	private int listArrayRes;
	private int singleChoiceListArrayRes;
	private int singleChoiceDefaultSelected;
	private int multiChoiceListArrayRes;
	private boolean[] multiChoiceDefaultSelected;	
	
	/**
	 * The interface to deliver action events
	 */
    private BaseDialogListener dialogListener;
	    
    /**
     * 
     * The activity that creates an instance of this dialog fragment must implement 
     * this interface in order to receive event callbacks. Each method passes the 
     * DialogFragment in case the host needs to query it.
     *
     */
    public interface BaseDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onDialogNeutralClick(DialogFragment dialog);
		public void onItemClick(DialogFragment dialog, int which);
		public void onSelectedItems(DialogFragment dialog, List<Integer> selectedItems);
		public void onCancel(DialogInterface dialog);
		public void onDismiss(DialogInterface dialog);
    }
    
    /**
     * Set dialog style and functionality.
     * 
     * @param iconRes 0 disabled
     * @param title null disabled
     * @param text null disabled
     * @param isCancelable By default true
     * @param positiveButonTextRes 0 disabled
     * @return BaseDialog
     */
	public static BaseDialog newInstance(int iconRes, String title, String text,
			boolean isCancelable, int positiveButonTextRes) {
		
		BaseDialog f = new BaseDialog();
		
		Bundle args = new Bundle();
		args.putInt(ICON_RES_KEY, iconRes);
		args.putString(TITLE_KEY, title);
		args.putString(TEXT_KEY, text);		
		args.putBoolean(IS_CANCELABLE_KEY, isCancelable);
		args.putInt(POSITIVE_BUTTON_TEXT_RES, positiveButonTextRes);
		
		f.setArguments(args);

		return f;
	}
	
    /**
     * Set dialog style and functionality.
     * 
     * @param iconRes 0 disabled
     * @param title null disabled
     * @param text null disabled
     * @param isCancelable By default true
     * @param positiveButonTextRes 0 disabled
     * @param negativeButonTextRes 0 disabled
     * @return BaseDialog
     */
	public static BaseDialog newInstance(int iconRes, String title, String text,
			boolean isCancelable, int positiveButonTextRes, int negativeButonTextRes) {
		
		BaseDialog f = new BaseDialog();
		
		Bundle args = new Bundle();
		args.putInt(ICON_RES_KEY, iconRes);
		args.putString(TITLE_KEY, title);
		args.putString(TEXT_KEY, text);		
		args.putBoolean(IS_CANCELABLE_KEY, isCancelable);
		args.putInt(POSITIVE_BUTTON_TEXT_RES, positiveButonTextRes);
		args.putInt(NEGATIVE_BUTTON_TEXT_RES, negativeButonTextRes);
		
		f.setArguments(args);

		return f;
	}
	
    /**
     * Set dialog style and functionality.
     * 
     * @param iconRes 0 disabled
     * @param title null disabled
     * @param text null disabled
     * @param isCancelable By default true
     * @param positiveButonTextRes 0 disabled
     * @param negativeButonTextRes 0 disabled
     * @param listArrayRes 0 disabled
     * @return BaseDialog 
     */
	public static BaseDialog newInstance(int iconRes, String title, String text,
			boolean isCancelable, int positiveButonTextRes, 
			int negativeButonTextRes, int listArrayRes) {
		
		BaseDialog f = new BaseDialog();
		
		Bundle args = new Bundle();
		args.putInt(ICON_RES_KEY, iconRes);
		args.putString(TITLE_KEY, title);
		args.putString(TEXT_KEY, text);		
		args.putBoolean(IS_CANCELABLE_KEY, isCancelable);
		args.putInt(POSITIVE_BUTTON_TEXT_RES, positiveButonTextRes);
		args.putInt(NEGATIVE_BUTTON_TEXT_RES, negativeButonTextRes);
		args.putInt(LIST_ARRAY_RES, listArrayRes);
		
		f.setArguments(args);

		return f;
	}
	
    /**
     * Set dialog style and functionality.
     * 
     * @param iconRes 0 disabled
     * @param title null disabled
     * @param text null disabled
     * @param isCancelable By default true
     * @param positiveButonTextRes 0 disabled
     * @param negativeButonTextRes 0 disabled
     * @param singleChoiceListArrayRes 0 disabled
     * @param singleChoiceDefaultSelected -1 no selected
     * @return BaseDialog
     */
	public static BaseDialog newInstance(int iconRes, String title, String text,
			boolean isCancelable, int positiveButonTextRes, int negativeButonTextRes, 
			int singleChoiceListArrayRes, int singleChoiceDefaultSelected) {
		
		BaseDialog f = new BaseDialog();
		
		Bundle args = new Bundle();
		args.putInt(ICON_RES_KEY, iconRes);
		args.putString(TITLE_KEY, title);
		args.putString(TEXT_KEY, text);		
		args.putBoolean(IS_CANCELABLE_KEY, isCancelable);
		args.putInt(POSITIVE_BUTTON_TEXT_RES, positiveButonTextRes);
		args.putInt(NEGATIVE_BUTTON_TEXT_RES, negativeButonTextRes);
		args.putInt(SINGLE_CHOICE_LIST_ARRAY_RES, singleChoiceListArrayRes);
		args.putInt(SINGLE_CHOICE_DEFAULT_SELECTED, singleChoiceDefaultSelected);
		
		f.setArguments(args);

		return f;
	}	
	
    /**
     * Set dialog style and functionality.
     * 
     * @param iconRes 0 disabled
     * @param title null disabled
     * @param text null disabled
     * @param isCancelable By default true
     * @param positiveButonTextRes 0 disabled
     * @param negativeButonTextRes 0 disabled
     * @param multiChoiceListArrayRes 0 disabled
     * @param multiChoiceDefaultSelected null no selected
     * @return BaseDialog
     */
	public static BaseDialog newInstance(int iconRes, String title, String text,
			boolean isCancelable, int positiveButonTextRes, int negativeButonTextRes, 
			int multiChoiceListArrayRes, boolean[] multiChoiceDefaultSelected) {
		
		BaseDialog f = new BaseDialog();
		
		Bundle args = new Bundle();
		args.putInt(ICON_RES_KEY, iconRes);
		args.putString(TITLE_KEY, title);
		args.putString(TEXT_KEY, text);		
		args.putBoolean(IS_CANCELABLE_KEY, isCancelable);
		args.putInt(POSITIVE_BUTTON_TEXT_RES, positiveButonTextRes);
		args.putInt(NEGATIVE_BUTTON_TEXT_RES, negativeButonTextRes);
		args.putInt(MULTI_CHOICE_LIST_ARRAY_RES, multiChoiceListArrayRes);
		args.putBooleanArray(MULTI_CHOICE_DEFAULT_SELECTED, multiChoiceDefaultSelected);
		
		f.setArguments(args);

		return f;
	}
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the SupportBaseDialogListener so we can send events to the host
        	dialogListener = (BaseDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SupportBaseDialogListener");
        }
        
    }	
	
	
	@Override
	public void onDetach() {
		dialogListener = null;
		super.onDetach();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		iconRes = getArguments().getInt(ICON_RES_KEY, 0);
		title = getArguments().getString(TITLE_KEY);
		text = getArguments().getString(TEXT_KEY);
		isCancelable = getArguments().getBoolean(IS_CANCELABLE_KEY, true);
		customTitleViewRes = getArguments().getInt(CUSTOM_TITLE_VIEW_RES_KEY, 0);
		viewRes = getArguments().getInt(VIEW_RES_KEY, 0);
		positiveButonTextRes = getArguments().getInt(POSITIVE_BUTTON_TEXT_RES, 0);
		negativeButonTextRes = getArguments().getInt(NEGATIVE_BUTTON_TEXT_RES, 0);
		neutralButonTextRes = getArguments().getInt(NEUTRAL_BUTTON_TEXT_RES, 0);
		listArrayRes = getArguments().getInt(LIST_ARRAY_RES, 0);
		singleChoiceListArrayRes = getArguments().getInt(SINGLE_CHOICE_LIST_ARRAY_RES, 0);
		singleChoiceDefaultSelected = getArguments().getInt(SINGLE_CHOICE_DEFAULT_SELECTED, -1);
		multiChoiceListArrayRes = getArguments().getInt(MULTI_CHOICE_LIST_ARRAY_RES, 0);
		multiChoiceDefaultSelected = getArguments().getBooleanArray(MULTI_CHOICE_DEFAULT_SELECTED);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final ArrayList<Integer> selectedItems = new ArrayList<Integer>();
		
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
		
		if(iconRes != 0) {
			builder.setIcon(iconRes);
		}
		
		if(title != null) {
			builder.setTitle(title);
		}
		
		if(text != null && 
				multiChoiceListArrayRes == 0 && 
				singleChoiceListArrayRes == 0 && 
				listArrayRes == 0) {
			builder.setMessage(text);	
		}
		
		builder.setCancelable(isCancelable);
		
		if(customTitleViewRes != 0) {
			builder.setCustomTitle(inflater.inflate(customTitleViewRes, null)); // TODO NULL OR this, activity??
		}
		
		if(viewRes != 0) {
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(viewRes, null));
		}

		if(positiveButonTextRes != 0) {
			builder.setPositiveButton(positiveButonTextRes, new DialogInterface.OnClickListener() {
				@Override
	            public void onClick(DialogInterface dialog, int id) {
					if(multiChoiceListArrayRes == 0) {
		            	// Send the positive button event back to the host activity
		            	dialogListener.onDialogPositiveClick(BaseDialog.this);						
					}
					else {
		            	// Send the positive items selected to the host activity
		            	dialogListener.onSelectedItems(BaseDialog.this, selectedItems);
					}

	            }
	        });			
		}

		if(negativeButonTextRes != 0) {
			builder.setNegativeButton(negativeButonTextRes, new DialogInterface.OnClickListener() {
				@Override				
	            public void onClick(DialogInterface dialog, int id) {
	            	// Send the negative button event back to the host activity
	            	dialogListener.onDialogNegativeClick(BaseDialog.this);
	            }
	        });
		}
		
		if(neutralButonTextRes != 0) {
			builder.setNeutralButton(neutralButonTextRes, new DialogInterface.OnClickListener() {
				@Override				
				public void onClick(DialogInterface dialog, int id) {
	            	// Send the neutral button event back to the host activity
	            	dialogListener.onDialogNeutralClick(BaseDialog.this);
	            }
	        });		
		}
		
		if (listArrayRes != 0) {
			builder.setItems(listArrayRes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// The 'which' argument contains the index position of the selected item
					dialogListener.onItemClick(BaseDialog.this, which);
				}
			});
		}
		
		if(singleChoiceListArrayRes != 0) {			
			// Specify the list array, the items to be selected by default (-1 for none),
			// and the listener through which to receive callbacks when item is selected
			builder.setSingleChoiceItems(singleChoiceListArrayRes, singleChoiceDefaultSelected, 
					new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// The 'which' argument contains the index position of the selected item
					dialogListener.onItemClick(BaseDialog.this, which);					
				}
			});
		}
		
		if(multiChoiceListArrayRes != 0) {			
			// Specify the list array, the items to be selected by default (null for none),
			// and the listener through which to receive callbacks when items are selected
			builder.setMultiChoiceItems(multiChoiceListArrayRes, multiChoiceDefaultSelected, 
					new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					if (isChecked) {
						// If the user checked the item, add it to the selected items
						selectedItems.add(which);
					} else if (selectedItems.contains(which)) {
						// Else, if the item is already in the array, remove it
						selectedItems.remove(Integer.valueOf(which));
					}
				}
			});	   
		}
						
		return builder.create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		dialogListener.onCancel(dialog);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		dialogListener.onDismiss(dialog);
	}
	
}
