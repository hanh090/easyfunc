package component.custom;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
/**
 * Utility control will open new tab when click on hyperlink
 *
 */
public class TabHyperlinkBox extends VBox {
	@FXML
	private
	Hyperlink link;

	@FXML
	private
	TextField field;

	public TabHyperlinkBox(TabPane tabPane) {
		FXMLLoader loader = new FXMLLoader();
		loader.getClass().getResource("TabHyperlinkBox.fxml");

		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	protected void handleOnAction() {
		
	}

	public TextField getField() {
		return field;
	}

	public void setField(TextField field) {
		this.field = field;
	}

	public Hyperlink getLink() {
		return link;
	}

	public void setLink(Hyperlink link) {
		this.link = link;
	}
	
	public void setTitle(String title){
		link.setText(title);
	}
	
	public void setContent(String content){
		field.setText(content);
	}
}
