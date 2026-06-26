package tn.iit.masi.miniprojet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.iit.masi.miniprojet.factory.ShapeFactory;
import tn.iit.masi.miniprojet.logging.ConsoleLoggerStrategy;
import tn.iit.masi.miniprojet.persistence.DatabaseManager;
import tn.iit.masi.miniprojet.persistence.DrawingRepository;
import tn.iit.masi.miniprojet.service.ActionLoggerService;
import tn.iit.masi.miniprojet.service.DrawingService;
import tn.iit.masi.miniprojet.ui.DrawingView;

public class MainApp extends Application {
	@Override
	public void start(Stage stage) {
		ShapeFactory shapeFactory = new ShapeFactory();
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		DrawingRepository drawingRepository = new DrawingRepository(databaseManager, shapeFactory);
		DrawingService drawingService = new DrawingService(drawingRepository);
		ActionLoggerService loggerService = new ActionLoggerService(new ConsoleLoggerStrategy());
		DrawingView drawingView = new DrawingView(shapeFactory, databaseManager, drawingService, loggerService);

		Scene scene = new Scene(drawingView.createRoot(), 980, 680);
		stage.setTitle("Mini Projet JavaFX - Dessin Geometrique");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
