package tn.iit.masi.miniprojet.observer;

public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyAllObservers(DrawingEvent event);
}
