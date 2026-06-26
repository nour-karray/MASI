package tn.iit.masi.miniprojet.service;

import tn.iit.masi.miniprojet.logging.LoggerStrategy;

public class ActionLoggerService {
    private LoggerStrategy loggerStrategy;

    public ActionLoggerService(LoggerStrategy loggerStrategy) {
        this.loggerStrategy = loggerStrategy;
    }

    public void setLoggerStrategy(LoggerStrategy loggerStrategy) {
        this.loggerStrategy = loggerStrategy;
    }

    public void log(String action, String details) {
        loggerStrategy.log(action, details);
    }
}
