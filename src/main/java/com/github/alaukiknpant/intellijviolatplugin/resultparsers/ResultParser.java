package com.github.alaukiknpant.intellijviolatplugin.resultparsers;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ResultParser {
        private static final Logger log = Logger.getInstance(ResultParser.class);

        private Map<Integer, String> violationMap;
        private PropertyChangeSupport changes = new PropertyChangeSupport( this );

        public ResultParser() {
        }

        public static ResultParser getInstance(@NotNull Project project) {
            return ServiceManager.getService(project, ResultParser.class);
        }

        public Map<Integer, String> parse(Path resultPath) {
            if(!Files.exists(Paths.get(String.valueOf(resultPath)))) {
                log.warn("result.txt does not exist - aborting parsing");
                return null;
            }
            try {
                changeViolationMap(readViolationList(resultPath));
                return getViolationMap();
            } catch (IOException e) {
                log.error("Could not parse given result file", e);
            }
            return null;
        }


        private ArrayList<String> readViolationList(Path path) throws IOException {
            ResultFileReader result = new ResultFileReader(path.toString());
            ArrayList<String> resList = result.returnViolationList();
            return resList;
        }

        private void changeViolationMap(List<String> violationList) {
            System.out.println("HEre in Violation Map");
            Map<Integer, String> map = new HashMap<>();
            Integer i = 1;
            for(String violation : violationList) {
                map.put(i, violation);
                i++;
            }
            Map<Integer, String> oldMap = this.violationMap;
            this.violationMap = map;
            changes.firePropertyChange("bugsPerFile", oldMap, violationMap);
        }

        public Map<Integer, String> getViolationMap() {
            return violationMap;
        }


        public void addPropertyChangeListener(PropertyChangeListener l) {
            changes.addPropertyChangeListener(l);
        }

        public void removePropertyChangeListener(PropertyChangeListener l) {
            changes.removePropertyChangeListener(l);
        }
}
