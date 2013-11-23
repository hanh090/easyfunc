package controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder tab with complex content
 * 
 */
public class FullDescriptionTab extends Tab {
	public static Logger logger = LoggerFactory.getLogger(FullDescriptionTab.class);
	public FullDescriptionTab setContent(String typeId, String methodName,
			String methodDesc) {
		ScrollPane scpContainer = new ScrollPane();
		scpContainer.setFitToWidth(true);
		
		VBox vBoxContainer = new VBox();
		VBox.setVgrow(scpContainer, Priority.ALWAYS);
		vBoxContainer.setFillWidth(true);

		// ------------------------------------------------------------------------------
		// *****************************************************************************
		// ----------------------------------------------------------------------------
		HBox hboxTypeId = new HBox();
		Label lblTypeId = new Label("Type id:");

		Label lblTypeValue = new Label(typeId);
		lblTypeValue.setWrapText(true);

		hboxTypeId.getChildren().addAll(lblTypeId, lblTypeValue);

		// ------------------------------------------------------------------------------
		// *****************************************************************************
		// ----------------------------------------------------------------------------
		HBox hboxMethodName = new HBox();
		Label lblMethodName = new Label("Method:");

		Label lblMethodNameValue = new Label(methodName);

		hboxTypeId.getChildren().addAll(lblMethodName, lblMethodNameValue);
		
		hboxMethodName.getChildren().addAll(lblMethodName,lblMethodNameValue);

		// ------------------------------------------------------------------------------
		// *****************************************************************************
		// ----------------------------------------------------------------------------
		Label lblDescription = new Label("Description:");

		// ------------------------------------------------------------------------------
		// *****************************************************************************
		// ----------------------------------------------------------------------------
		Label lblDescriptionValue = new Label(methodDesc);
		lblDescriptionValue.setMinWidth(vBoxContainer.getWidth());
		lblDescriptionValue.setWrapText(true);

		// ------------------------------------------------------------------------------
		// *****************************************************************************
		// ----------------------------------------------------------------------------
		VBox checkGroup = new VBox();
		Label question = new Label("Is it right solution?");
		ToggleGroup toggleGroup = new ToggleGroup();
		final RadioButton rightSolution = new RadioButton("Yes");
		
		final RadioButton wrongSolution = new RadioButton("No");
		
		
		rightSolution.setToggleGroup(toggleGroup);
		wrongSolution.setToggleGroup(toggleGroup);
		checkGroup.getChildren().addAll(question,rightSolution,wrongSolution);
		
		// ------------------------------------------------------------------------------
		// *****************************************************************************
		// ----------------------------------------------------------------------------
		vBoxContainer.getChildren().addAll(hboxTypeId,hboxMethodName,lblDescription,lblDescriptionValue,checkGroup);
		
		scpContainer.setContent(vBoxContainer);
		this.setText(methodName);
		this.setContent(scpContainer);
		
		this.setOnClosed(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				String selected = (rightSolution.isSelected())?"RIGHT" :((wrongSolution.isSelected())?"WRONG":"UNDERFINE");
				logger.info("{} solution", selected);
			}
		});
		return this;
	}
}
