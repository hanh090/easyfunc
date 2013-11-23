package controller;


import java.util.List;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import main.CodeGenerationMain;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import document.wrapper.FieldName;

public class SearchController {
	public static Logger logger = LoggerFactory.getLogger(SearchController.class);
	@FXML
	private TextField txtQuery;

	@FXML
	private ProgressIndicator pgrSearching;

	@FXML
	private AnchorPane resultPane;

	@FXML
	private TabPane navigatePane;
	
	@FXML
	private Tab tabResult;
	
	@FXML
	private Button btnSearch;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private TextField txtStuID;
	
	@FXML
	private RadioButton chbHonor;
	
	@FXML
	private RadioButton chbNormal;

	
	public void init() {
	}


	
	@FXML
	private void handleSearchAction(ActionEvent e) {
		logger.info("USER:{} FROM: {}", txtStuID.getText(),(chbHonor.isSelected())?"Honor program":"Normal program");
		logger.info("QUERY:" + txtQuery.getText());
		final Task<List<Document>> task = new Task<List<Document>>() {

			@Override
			protected List<Document> call() throws Exception {
				long start = System.currentTimeMillis();
				CodeGenerationMain codeGeneration = new CodeGenerationMain();
				List<Document> resultSearch = codeGeneration.search(txtQuery
						.getText());
				logger.info("TIME TO SEARCH:{}",System.currentTimeMillis() - start);
				logger.info("NUMBER RESULT: {}", resultSearch.size());
				return resultSearch;
			}

		};

		task.setOnRunning(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				pgrSearching.setVisible(true);
				resultPane.setDisable(true);
				btnSearch.setDisable(true);
			
			}
		});

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				pgrSearching.setVisible(false);
				resultPane.setDisable(false);
				btnSearch.setDisable(false);
				
				List<Document> processedResult = task.getValue();
				
				VBox vboxContent = new VBox();
				VBox.setVgrow(scrollPane, Priority.ALWAYS);
				//Node [] elements
				for (Document document : processedResult) {
					
					final String typeId = document.get(FieldName.TYPE_ID.getName());
					final String methodName = document.get(FieldName.METHOD_NAME.getName());
					final String methodDesc = document.get(FieldName.METHOD_DESC.getName());
					final int index = processedResult.indexOf(document);
					
					Hyperlink hyperlink = new Hyperlink(methodName + ":" + typeId);
					hyperlink.setUnderline(true);
					hyperlink.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							logger.info("Related result:{}:{} is index={}",methodName,typeId,index);
							Tab t = new FullDescriptionTab().setContent(typeId, methodName, methodDesc);
							
							navigatePane.getTabs().add(t);
						}
					});
					
					Label l = new Label(methodDesc.split("\\.")[0]);
					
					//Add element to anchor pane
					vboxContent.getChildren().addAll(hyperlink,l);
					
				}
				
				scrollPane.setContent(vboxContent); 
			}
		});
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
}
