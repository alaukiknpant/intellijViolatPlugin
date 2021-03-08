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

        /**
         * Parses a given violat result.txt file, it in the {@link ResultParser}. The result file has 'result.txt' as the default name. The Violat ToolWindow is notified, so it can show the parsed results.
         * @param resultPath The Path of the file in the txt format
         * @return A Map of the rearranged Results. Mainly for testing purposes. Is null when the file doesnt exist.
         */
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

        /**
         * Reads a report.json from an Infer analysis and deserializes it into a list of InferBug objects
         * @param path The Path of the result.txt
         * @return A list of Violations
         * @throws IOException If the text file couldn't be read
         */
        private ArrayList<String> readViolationList(Path path) throws IOException {
            ResultFileReader result = new ResultFileReader(path.toString());
            ArrayList<String> resList = result.returnViolationList();
            return resList;
        }

        /**
         * Rearranges the buglist from the format infer delivers to a map with the filenames as keys and a list of bugs from that file as value
         * @param violationList The buglist, deserialized from the infer report.json
         * @return
         */
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
