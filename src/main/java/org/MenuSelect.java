package org;

public enum MenuSelect {
    ADD,
    EDIT,
    REMOVE,
    VIEW,
    CALC,
    EXIT;

    public static MenuSelect fromString(String input) {
        return switch (input) {
            case "1" -> ADD;
            case "2" -> EDIT;
            case "3" -> REMOVE;
            case "4" -> VIEW;
            case "5" -> CALC;
            default -> EXIT;
        };
    }
}
