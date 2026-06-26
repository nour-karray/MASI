package tn.iit.masi.miniprojet.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private final Deque<Command> undoStack = new ArrayDeque<>();

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
    }

    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }

        Command command = undoStack.pop();
        command.undo();
        return true;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public void clearHistory() {
        undoStack.clear();
    }
}
