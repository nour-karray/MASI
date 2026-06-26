package tn.iit.masi.miniprojet.ui;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import tn.iit.masi.miniprojet.command.AddShapeCommand;
import tn.iit.masi.miniprojet.command.ClearDrawingCommand;
import tn.iit.masi.miniprojet.command.CommandManager;
import tn.iit.masi.miniprojet.command.LoadDrawingCommand;
import tn.iit.masi.miniprojet.decorator.BlackShapeColor;
import tn.iit.masi.miniprojet.decorator.BlueShapeColor;
import tn.iit.masi.miniprojet.decorator.GreenShapeColor;
import tn.iit.masi.miniprojet.decorator.RedShapeColor;
import tn.iit.masi.miniprojet.decorator.ShapeColor;
import tn.iit.masi.miniprojet.factory.ShapeFactory;
import tn.iit.masi.miniprojet.logging.ConsoleLoggerStrategy;
import tn.iit.masi.miniprojet.logging.DatabaseLoggerStrategy;
import tn.iit.masi.miniprojet.logging.FileLoggerStrategy;
import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.model.ShapeType;
import tn.iit.masi.miniprojet.observer.LoggingObserver;
import tn.iit.masi.miniprojet.observer.StatusObserver;
import tn.iit.masi.miniprojet.persistence.DatabaseManager;
import tn.iit.masi.miniprojet.persistence.DrawingSummary;
import tn.iit.masi.miniprojet.service.ActionLoggerService;
import tn.iit.masi.miniprojet.service.DrawingService;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class DrawingView {
    private static final double CANVAS_WIDTH = 900;
    private static final double CANVAS_HEIGHT = 550;

    private final ShapeFactory shapeFactory;
    private final DatabaseManager databaseManager;
    private final DrawingService drawingService;
    private final ActionLoggerService loggerService;
    private final CommandManager commandManager;

    private ShapeType currentShapeType = ShapeType.RECTANGLE;
    private ShapeColor currentColor = new BlueShapeColor();
    private DrawableShape previewShape;
    private double startX;
    private double startY;

    public DrawingView(
            ShapeFactory shapeFactory,
            DatabaseManager databaseManager,
            DrawingService drawingService,
            ActionLoggerService loggerService
    ) {
        this.shapeFactory = shapeFactory;
        this.databaseManager = databaseManager;
        this.drawingService = drawingService;
        this.loggerService = loggerService;
        this.commandManager = new CommandManager();
    }

    public BorderPane createRoot() {
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Label statusLabel = new Label("Pret - Choisis une forme puis dessine.");

        drawingService.addObserver(new StatusObserver(statusLabel::setText));
        drawingService.addObserver(new LoggingObserver(loggerService));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(createToolbar(statusLabel, () -> redraw(gc)));
        root.setCenter(canvas);
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(8, 0, 0, 0));

        bindDrawingEvents(canvas, gc, statusLabel);
        redraw(gc);

        return root;
    }

    private HBox createToolbar(Label statusLabel, Runnable redrawAction) {
        ToggleButton rectangleButton = new ToggleButton("Rectangle");
        ToggleButton circleButton = new ToggleButton("Cercle");
        ToggleButton lineButton = new ToggleButton("Ligne");

        ToggleGroup group = new ToggleGroup();
        rectangleButton.setToggleGroup(group);
        circleButton.setToggleGroup(group);
        lineButton.setToggleGroup(group);
        rectangleButton.setSelected(true);

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == rectangleButton) {
                currentShapeType = ShapeType.RECTANGLE;
            } else if (newToggle == circleButton) {
                currentShapeType = ShapeType.CIRCLE;
            } else if (newToggle == lineButton) {
                currentShapeType = ShapeType.LINE;
            }
            loggerService.log("SELECTION_FORME", "Forme selectionnee: " + currentShapeType);
            statusLabel.setText("Forme active: " + currentShapeType);
        });

        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(event -> saveDrawing(statusLabel));

        Button openButton = new Button("Ouvrir");
        openButton.setOnAction(event -> openDrawing(redrawAction));

        Button clearButton = new Button("Effacer");
        clearButton.setOnAction(event -> clearDrawing(statusLabel, redrawAction));

        Button undoButton = new Button("Annuler");
        undoButton.setOnAction(event -> undo(statusLabel, redrawAction));

        ComboBox<ShapeColor> colorChoice = new ComboBox<>();
        colorChoice.getItems().addAll(
                new BlueShapeColor(),
                new RedShapeColor(),
                new GreenShapeColor(),
                new BlackShapeColor()
        );
        colorChoice.setValue(currentColor);
        colorChoice.setOnAction(event -> changeShapeColor(colorChoice.getValue(), statusLabel));

        ComboBox<String> loggerChoice = new ComboBox<>();
        loggerChoice.getItems().addAll("Console", "Fichier", "Base de donnees");
        loggerChoice.setValue("Console");
        loggerChoice.setOnAction(event -> changeLoggerStrategy(loggerChoice.getValue(), statusLabel));

        HBox toolbar = new HBox(8, rectangleButton, circleButton, lineButton, saveButton, openButton,
                clearButton, undoButton, new Label("Couleur:"), colorChoice,
                new Label("Log:"), loggerChoice);
        toolbar.setPadding(new Insets(0, 0, 10, 0));
        return toolbar;
    }

    private void bindDrawingEvents(Canvas canvas, GraphicsContext gc, Label statusLabel) {
        canvas.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();
        });

        canvas.setOnMouseDragged(event -> {
            previewShape = createShape(event.getX(), event.getY());
            redraw(gc);
            statusLabel.setText("Apercu: " + currentShapeType);
        });

        canvas.setOnMouseReleased(event -> {
            DrawableShape newShape = createShape(event.getX(), event.getY());
            commandManager.executeCommand(new AddShapeCommand(drawingService, newShape));
            previewShape = null;
            redraw(gc);
        });
    }

    private DrawableShape createShape(double endX, double endY) {
        return shapeFactory.createShape(currentShapeType, startX, startY, endX, endY,
                currentColor.getColor(), 2.0);
    }

    private void redraw(GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        for (DrawableShape shape : drawingService.getCurrentShapes()) {
            shape.draw(gc);
        }

        if (previewShape != null) {
            gc.save();
            gc.setGlobalAlpha(0.45);
            previewShape.draw(gc);
            gc.restore();
        }
    }

    private void clearDrawing(Label statusLabel, Runnable redrawAction) {
        if (drawingService.getCurrentShapes().isEmpty()) {
            statusLabel.setText("Dessin deja vide.");
            return;
        }

        commandManager.executeCommand(new ClearDrawingCommand(drawingService));
        previewShape = null;
        redrawAction.run();
    }

    private void undo(Label statusLabel, Runnable redrawAction) {
        if (!commandManager.undo()) {
            statusLabel.setText("Aucune action a annuler.");
            return;
        }

        previewShape = null;
        redrawAction.run();
    }

    private void changeLoggerStrategy(String selected, Label statusLabel) {
        if ("Console".equals(selected)) {
            loggerService.setLoggerStrategy(new ConsoleLoggerStrategy());
        } else if ("Fichier".equals(selected)) {
            loggerService.setLoggerStrategy(new FileLoggerStrategy(Path.of("actions.log")));
        } else {
            loggerService.setLoggerStrategy(new DatabaseLoggerStrategy(databaseManager));
        }

        loggerService.log("STRATEGIE_LOG", "Nouvelle strategie: " + selected);
        statusLabel.setText("Journalisation: " + selected);
    }

    private void changeShapeColor(ShapeColor color, Label statusLabel) {
        currentColor = color;
        loggerService.log("DECORATOR_COULEUR", "Nouvelle couleur: " + color.getLabel());
        statusLabel.setText("Couleur active: " + color.getLabel());
    }

    private void saveDrawing(Label statusLabel) {
        if (drawingService.getCurrentShapes().isEmpty()) {
            showInfo("Info", "Aucune forme a sauvegarder.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("Dessin-" + System.currentTimeMillis());
        dialog.setHeaderText("Enregistrer le dessin");
        dialog.setContentText("Nom du dessin:");
        Optional<String> input = dialog.showAndWait();
        if (input.isEmpty() || input.get().isBlank()) {
            return;
        }

        long id = drawingService.saveCurrentDrawing(input.get().trim());
        loggerService.log("ENREGISTRER", "Dessin id=" + id + " enregistre");
        statusLabel.setText("Dessin sauvegarde avec succes (id=" + id + ").");
    }

    private void openDrawing(Runnable redrawAction) {
        List<DrawingSummary> drawings = drawingService.listDrawings();
        if (drawings.isEmpty()) {
            showInfo("Info", "Aucun dessin enregistre.");
            return;
        }

        ChoiceDialog<DrawingSummary> dialog = new ChoiceDialog<>(drawings.get(0), drawings);
        dialog.setHeaderText("Ouvrir un dessin");
        dialog.setContentText("Selection:");
        Optional<DrawingSummary> selected = dialog.showAndWait();
        if (selected.isEmpty()) {
            return;
        }

        DrawingSummary summary = selected.get();
        commandManager.executeCommand(new LoadDrawingCommand(drawingService, summary.id(), summary.name()));
        previewShape = null;
        redrawAction.run();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
