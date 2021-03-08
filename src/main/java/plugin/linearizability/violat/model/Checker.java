package plugin.linearizability.violat.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum Checker {
    //DEFAULT SINCE VERSION 0.15.0
    VALIDATOR("validator", Category.OPTIONAL, new ViolatVersion(0, 16, 0)),                               //validates if a given implementation is linearizable
    HISTORIES("histories", Category.OPTIONAL, new ViolatVersion(0, 16, 0));          //prints possible histories of a given implementation

    enum Category {DEFAULT, OPTIONAL, EXPERIMENTAL}

    private String argument;
    private Category category;
    private ViolatVersion sinceVersion;

    Checker(String arg, Category category, ViolatVersion sinceVersion) {
        this.argument = arg;
        this.category = category;
        this.sinceVersion = sinceVersion;
    }

    public String getActivationArgument() {
        return "-" + this.argument;
    }

//    public String getDeactivationArgument() {
//        return "--no-" + this.argument;
//    }
    public ViolatVersion getSinceVersion() {
        return sinceVersion;
    }
    public Boolean isDefault() {
        return this.category == Category.DEFAULT;
    }
    public String getName() {
        return super.toString();
    }

    public String toString() {
        switch(this.category) {
            case DEFAULT:
                return "[Default] " + super.toString();
            case OPTIONAL:
                return "[Optional] " + super.toString();
            case EXPERIMENTAL:
                return "[Experimental] " + super.toString();
        }
        return super.toString();
    }

    @NotNull
    public static List<Checker> getDefaultCheckers() {
        List<Checker> defaultChecker = new ArrayList<>();
        for(Checker checker : Checker.values()) {
            if(checker.isDefault()) defaultChecker.add(checker);
        }

        return defaultChecker;
    }

    /**
     * Gets a List of Checkers supported by the given version and not included in the given list.
     * @param checkers The given list of checkers
     * @param version The given version
     * @return A diff of the given checkers list and all checkers for this version
     */
    @NotNull
    public static List<Checker> getMissingCheckers(List<Checker> checkers, ViolatVersion version) {
        List<Checker> missingCheckers = new ArrayList<>();
        for(Checker checker : Checker.values()) {
            if(!checkers.contains(checker) && checker.getSinceVersion().compareTo(version) <= 0) missingCheckers.add(checker);
        }
        return missingCheckers;
    }
}
