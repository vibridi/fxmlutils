package com.vibridi.fxmlutils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FXMLUtils {

	/**
	 * 	 
	 * @param clazz Class relative to which the <code>.fxml</code> file is found
	 * @param fxml Path to the <code>.fxml</code> file
	 * @return Instance of <code>FXMLBuilder</code>
	 */
	public static FXMLBuilder newView(Class<?> clazz, String fxml) {		
		return newView(clazz.getResource(fxml));
	}
	
	public static FXMLBuilder newView(URL url) {
		return new FXMLBuilder(url);
	}
	
	public static Alert errorAlert(String message) {
		return errorAlert(null, message, null);
	}
	
	public static Alert errorAlert(String header, String message) {
		return errorAlert(header, message, null);
	}
	
	public static Alert errorAlert(String message, Throwable t) {
		return errorAlert(null, message, t);
	}
	
	public static Alert errorAlert(String header, String message, Throwable t) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message);
		if(t != null)
			alert.getDialogPane().setExpandableContent(createExpandableContent(t));
		return alert;
	}
	
	public static Alert warningAlert(String message) {
		return warningAlert(null, message, null);
	}
	
	public static Alert warningAlert(String header, String message) {
		return warningAlert(header, message, null);
	}
	
	public static Alert warningAlert(String message, Throwable t) {
		return warningAlert(null, message, t);
	}
	
	public static Alert warningAlert(String header, String message, Throwable t) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(header);
		alert.setContentText(message);
		if(t != null)
			alert.getDialogPane().setExpandableContent(createExpandableContent(t));
		return alert;
	}
	
	public static Alert infoAlert(String message) {
		return infoAlert(null, message);
	}
	
	public static Alert infoAlert(String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(header);
		alert.setContentText(message);
		return alert;
	}
	
	public static void copyToClipboard(String text) {
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		Clipboard.getSystemClipboard().setContent(content);
	}
	
	public static void saveFile(byte[] bytes, Stage owner, String... extensions) throws IOException {
		File dest = createFileChooser("Save file", extensions).showSaveDialog(owner);
		Files.write(Paths.get(dest.toURI()), bytes);
	}
	
	/**
	 * 
	 * @param owner The node owner of the search dialog
	 * @param extensions File extensions filters. Do not prepend '.' to the extension.
	 * @return
	 */
	public static File openFile(Stage owner, String... extensions) {
		return createFileChooser("Open file", extensions).showOpenDialog(owner);
	}
	
	/**
	 * 
	 * @param owner The node owner of the search dialog
	 * @param extensions File extensions filters. Do not prepend '.' to the extension.
	 * @return
	 */
	public static List<File> openFiles(Stage owner, String... extensions) {
		return createFileChooser("Open files", extensions).showOpenMultipleDialog(owner);
	}
	
	public static List<File> openDirectory(Stage owner) {
		File dir = chooseDirectory(owner);
		if(dir == null)
			return null;
		return Arrays.asList(dir.listFiles());		
	}
	
	public static File chooseDirectory(Stage owner) {
		return createDirectoryChooser("Open directory").showDialog(owner);
	}
	
	public static void turnPage(StackPane stack) {
		stack.getChildren().get(stack.getChildren().size() - 1).toBack();
	}
	
	/**
	 * Convenience setter for double-click events. The handler that you pass to this function doesn't have to 
	 * explicitly check that the event is a double click. 
	 * 
	 * @param node
	 * @param doubleClickHandler
	 */
	public static void setOnDoubleClick(Node node, EventHandler<? super MouseEvent> doubleClickHandler) {
		node.setOnMouseClicked(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				doubleClickHandler.handle(event);
			}
		});
	}
	
	/* ***********************
	 * PRIVATE METHODS
	 * ***********************/
	private static GridPane createExpandableContent(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString();
		return createExpandableContent(stackTrace);
	}
	
	private static GridPane createExpandableContent(String content) {
		Label label = new Label("Exception stacktrace");

		TextArea textArea = new TextArea(content);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		return expContent;
	}
	
	private static FileChooser createFileChooser(String title, String... extensions) {
		FileChooser fc = new FileChooser();
		fc.setTitle(title);
		fc.getExtensionFilters().addAll(Arrays.stream(extensions)
				.map(s -> { return new ExtensionFilter(s.toUpperCase()+" files", "*."+s); })
				.collect(Collectors.toList()));
		return fc;
	}
	
	private static DirectoryChooser createDirectoryChooser(String title) {
		DirectoryChooser dc = new DirectoryChooser();
		//dc.setInitialDirectory(new File(homeUri));
		dc.setTitle(title);
		return dc;
	}

	
}
