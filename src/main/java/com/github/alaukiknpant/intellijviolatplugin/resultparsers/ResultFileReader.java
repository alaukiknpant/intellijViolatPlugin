package com.github.alaukiknpant.intellijviolatplugin.resultparsers;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultFileReader {
    private String filePath;

    public ResultFileReader(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<String> returnViolationList() throws IOException {

        File file = new File(this.filePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String fin = "";
        String line;

        while((line = br.readLine()) != null){
            //process the line
            Pattern pattern1 = Pattern.compile("Found.*violations.*");
            Pattern pattern2 = Pattern.compile("violat version.*");
            Matcher matcher1 = pattern1.matcher(line);
            Matcher matcher2 = pattern2.matcher(line);


            if (!(matcher1.lookingAt() || matcher2.lookingAt())) {
                if (!line.trim().isEmpty()) {
                    fin = fin + line + "\n";
                }

            }

        }
        String[] violationList = fin.split("---\n" +
                "violation discovered\n" +
                "---");

        ArrayList<String> vioList = new ArrayList<String>();

        System.out.println(violationList[0]);

        for (int i=0; i < violationList.length; i++){
            if (i> 0) {
                System.out.println("--------------------------\n");
                vioList.add(violationList[i]);
                System.out.println(violationList[i]);
                System.out.println("--------------------------\n");
            }
        }
        return vioList;
    }

    public static void main(String args[]) throws IOException {
        ResultFileReader result = new ResultFileReader("/Users/alaukik/Desktop/result.txt");
        ArrayList<String> resList = result.returnViolationList();
    }

}

