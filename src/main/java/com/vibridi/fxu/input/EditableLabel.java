package com.vibridi.fxu.input;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

/**
 * A TextField, that implements some Label functionality
 *
 * It acts as a Label, by removing the TextField style and making it non-editable.
 * It is also not focus traversable.
 *
 * When clicking on it, it will switch to editable mode.
 * Changing focus away from the EditableLabel or pressing ENTER will save the changes and deactivate editable mode.
 * When pressing ESC it will exit editable mode without saving the changes made.
 *
 * Credit: R. J. Westman https://github.com/rjwestman/JavaFX_EditableLabel
 */
public class EditableLabel extends TextField {

	private IntegerProperty editableClicks;
	private StringProperty baseText;

	public EditableLabel() {
		this("");
	}

	public EditableLabel(String text) {
		super(text);
		getStyleClass().setAll("editable-label");
		init();
	}

	private void init() {
		editableClicks = new SimpleIntegerProperty(1);
		baseText = new SimpleStringProperty(getText());
		setFocusTraversable(false);
		setEditable(false);
	}

	@Override
	protected Skin<?> createDefaultSkin() { 
		return new EditableLabelSkin(this); 
	}

	/*********************************************
	 *                                           *
	 * FX PROPERTIES		                     *
	 *                                           *
	 *********************************************/
	/**
	 * Clicks needed to enter editable-mode.
	 * @return Number of clicks
	 */
	public int getEditableClicks() { 
		return editableClicks.get(); 
	}

	public IntegerProperty editableClicksProperty() { 
		return editableClicks; 
	}

	public void setEditableClicks(int editableClicks) {
		this.editableClicks.set(editableClicks);
	}

	/**
	 * This saves the text that is going to be displayed.
	 *
	 * Since we can't override the final set/get methods of the super class, we need to use this
	 * to set the text that is to be displayed.
	 * Since the displayed text can be a truncated base text we need to save the base in it's own property.
	 * @return Text to be displayed
	 */
	public String getBaseText() { 
		return baseText.get(); 
	}

	public StringProperty baseTextProperty() { 
		return baseText; 
	}

	public void setBaseText(String baseText) {
		if(baseText == null || baseText.isEmpty())
			baseText = "(Empty)";
		this.baseText.set(baseText); 
	}



}
