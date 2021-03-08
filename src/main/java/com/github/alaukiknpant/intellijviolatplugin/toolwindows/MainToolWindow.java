package com.github.alaukiknpant.intellijviolatplugin.toolwindows;

import com.github.alaukiknpant.intellijviolatplugin.resultparsers.ResultParser;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.util.Map;
import java.util.ResourceBundle;

import static com.intellij.ui.SimpleTextAttributes.STYLE_PLAIN;

public class MainToolWindow {
    private static final Logger log = Logger.getInstance(MainToolWindow.class);

    private JPanel MainToolWindowContent;
    private Tree issueList;

    public MainToolWindow(ToolWindow toolWindow, Project project) {
        issueList.getEmptyText().setText(ResourceBundle.getBundle("strings").getString("no.bug.list.to.show"));
        issueList.setModel(new DefaultTreeModel(null));

        ResultParser.getInstance(project).addPropertyChangeListener(evt -> {
            if(evt.getNewValue() != null && evt.getPropertyName().equals("bugsPerFile")) {
                drawBugTree((Map<Integer, String>)evt.getNewValue());
            }
        });

//        //Coloring
        issueList.setCellRenderer(new ColoredTreeCellRenderer() {
            @Override
            public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                //is a bug or bugtrace (tree depth 3 or 4)
                //is the top most entry (tree depth 1)
                if(row == 0){
                    append(value.toString(), new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD, Color.blue));
                    setIcon(AllIcons.Actions.ListFiles);
                }  else if (((DefaultMutableTreeNode)value).getDepth() == 1) {
                    append(value.toString(), new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD, Color.pink));
                    setIcon(AllIcons.Actions.Preview);

                } else {
                    append(value.toString(), new SimpleTextAttributes(STYLE_PLAIN,Color.gray));
                }
            }
        });


        drawBugTree(ResultParser.getInstance(project).getViolationMap());
    }

    public JPanel getContent() {
        return MainToolWindowContent;
    }

    /**
     * Draws the given bugMap to the Infer Tool Window
     * @param bugMap keys are filenames, while the values are lists of infer bugs
     */
    private void drawBugTree(Map<Integer,String> bugMap) {
        if(bugMap == null) return;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(String.format(ResourceBundle.getBundle("strings").getString("violat.analysis.result.bugs.found"), bugMap.size()));

        for (Map.Entry<Integer, String> entry : bugMap.entrySet()) {
            String[] bugStringList = entry.getValue().split("\n");
            String bugDescription = "";
            if (bugStringList.length >= 2) {
                bugDescription = bugStringList[1];
            }
            DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(String.format(ResourceBundle.getBundle("strings").getString("violat.bugs.found"), entry.getKey(), bugDescription));
            for (String elem: bugStringList){
                DefaultMutableTreeNode bugNode = new DefaultMutableTreeNode(elem);
                fileNode.add(bugNode);
            }
            root.add(fileNode);
        }
        TreeModel tm = new DefaultTreeModel(root);
        issueList.setModel(tm);
    }

}
