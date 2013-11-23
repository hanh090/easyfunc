package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.ResourceUtils;

public class Main extends Application {
	public static Logger logger = LoggerFactory.getLogger(Main.class);
	@Override
	public void start(Stage stage) throws Exception {
	
		FXMLLoader loader = new FXMLLoader();
		
		String base = ResourceUtils.getClasspathBase(Main.class);
		loader.setLocation(new File(base).toURI().toURL());
		InputStream resourceAsStream = new FileInputStream(new File(base + "/SearchResultScreen.fxml"));
		Parent root = (Parent) loader.load(resourceAsStream);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(new File(base + "/styles.css").toURI().toURL().toExternalForm());
		stage.setTitle("EasyFunc Search");
		stage.setIconified(true);
		stage.setScene(scene);
		stage.show();
		
		logger.info("Start Easyfunc");
	}

	public static void main(String[] args) {
		launch(args);
	}

}
