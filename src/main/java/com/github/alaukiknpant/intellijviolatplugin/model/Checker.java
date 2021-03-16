package com.github.alaukiknpant.intellijviolatplugin.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum Checker {

    VALIDATOR("validator", Category.DEFAULT),                               //validates if a given implementation is linearizable
    HISTORIES("histories", Category.INPROGRESS);          //prints possible histories of a given implementation

    enum Category {DEFAULT, INPROGRESS}

    private String argument;
    private Category category;


    Checker(String arg, Category category) {
        this.argument = arg;
        this.category = category;

    }

    public String getActivationArgument() {
        return "-" + this.argument;
    }

    public Boolean isDefault() {
        return this.category == Category.DEFAULT;
    }
    public Boolean isInProgress() {
        return this.category == Category.INPROGRESS;
    }
    public String getName() {
        return super.toString();
    }

    public String toString() {
        switch(this.category) {
            case DEFAULT:
                return "[Default] " + super.toString();
            case INPROGRESS:
                return "[In Progress] " + super.toString();
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
            if(!checkers.contains(checker)) missingCheckers.add(checker);
        }
        return missingCheckers;
    }
}
