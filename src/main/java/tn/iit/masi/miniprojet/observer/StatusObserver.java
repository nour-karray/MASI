package tn.iit.masi.miniprojet.observer;

import java.util.function.Consumer;

public class StatusObserver implements Observer {
    private final Consumer<String> statusConsumer;

    public StatusObserver(Consumer<String> statusConsumer) {
        this.statusConsumer = statusConsumer;
    }

    @Override
    public void update(DrawingEvent event) {
        statusConsumer.accept(event.message() + " | Total: " + event.shapeCount());
    }
}
