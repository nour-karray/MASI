package tn.iit.masi.miniprojet.persistence;

public record DrawingSummary(long id, String name) {
    @Override
    public String toString() {
        return id + " - " + name;
    }
}
