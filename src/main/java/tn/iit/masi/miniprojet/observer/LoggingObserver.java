package tn.iit.masi.miniprojet.observer;

import tn.iit.masi.miniprojet.service.ActionLoggerService;

public class LoggingObserver implements Observer {
    private final ActionLoggerService loggerService;

    public LoggingObserver(ActionLoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public void update(DrawingEvent event) {
        loggerService.log(event.action(), event.message() + " | Total: " + event.shapeCount());
    }
}
