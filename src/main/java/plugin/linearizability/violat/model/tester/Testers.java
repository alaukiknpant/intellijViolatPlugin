package plugin.linearizability.violat.model.tester;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum Testers {
    //DEFAULT SINCE VERSION 0.15.0
    JPF("\"Java Pathfinder\""),
    JCSTRESS("\"JCStress\"");

    private String cmnd;


    Testers(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getName() {
        return this.cmnd;
    }

    public String getActivationArgument() {
        return "--tester" + " " + this.cmnd;
    }


    @NotNull
    public static List<Testers> getTesters() {
        List<Testers> tester = new ArrayList<>();
        for(Testers tstr : Testers.values()) {
            tester.add(tstr);
        }
        return tester;
    }

}
