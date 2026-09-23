package tn.iit.masi.miniprojet.service;

import tn.iit.masi.miniprojet.model.DrawableShape;
import tn.iit.masi.miniprojet.observer.DrawingEvent;
import tn.iit.masi.miniprojet.observer.Observable;
import tn.iit.masi.miniprojet.observer.Observer;
import tn.iit.masi.miniprojet.persistence.DrawingRepository;
import tn.iit.masi.miniprojet.persistence.DrawingSummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawingService implements Observable {
    private final DrawingRepository drawingRepository;
    private final List<DrawableShape> currentShapes;
    private final List<Observer> observers;

    public DrawingService(DrawingRepository drawingRepository) {
        this.drawingRepository = drawingRepository;
        this.currentShapes = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void addShape(DrawableShape shape) {
        currentShapes.add(shape);
        notifyDrawingChanged("DESSIN", "Forme " + shape.getType() + " ajoutee");
    }

    public void clearCurrentDrawing() {
        currentShapes.clear();
        notifyDrawingChanged("EFFACER", "Dessin courant efface");
    }

    public List<DrawableShape> getCurrentShapes() {
        return Collections.unmodifiableList(currentShapes);
    }

    public List<DrawableShape> copyCurrentShapes() {
        return List.copyOf(currentShapes);
    }

    public long saveCurrentDrawing(String name) {
        return drawingRepository.save(name, currentShapes);
    }

    public List<DrawingSummary> listDrawings() {
        return drawingRepository.findAllDrawings();
    }

    public void loadDrawing(long drawingId) {
        loadDrawing(drawingId, "id=" + drawingId);
    }

    public void loadDrawing(long drawingId, String drawingName) {
        List<DrawableShape> loadedShapes = drawingRepository.loadShapes(drawingId);
        currentShapes.clear();
        currentShapes.addAll(loadedShapes);
        notifyDrawingChanged("OUVRIR", "Dessin charge: " + drawingName);
    }

    public void replaceCurrentDrawing(List<DrawableShape> shapes, String action, String message) {
        currentShapes.clear();
        currentShapes.addAll(shapes);
        notifyDrawingChanged(action, message);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(DrawingEvent event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    private void notifyDrawingChanged(String action, String message) {
        notifyAllObservers(new DrawingEvent(action, message, currentShapes.size()));
    }
}
