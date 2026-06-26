package tn.iit.masi.miniprojet.command;

public interface Command {
    void execute();

    void undo();

    String name();
}
