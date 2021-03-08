package com.github.alaukiknpant.intellijviolatplugin.specgenerator;

import com.github.cliftonlabs.json_simple.JsonObject;

public class ClassJSON {
    public String class_name;
    public Integer invocations;
    public Methoed[] methoeds;

    public ClassJSON(String class_name, Integer invocations, Methoed[] methoeds) {
        this.class_name = class_name;
        this.invocations = invocations;
        this.methoeds = methoeds;
    }

    public JsonObject classToJSON(){
        JsonObject classJSON = new JsonObject();
        JsonObject invocations = new JsonObject();
        JsonObject[] json_methods = new JsonObject[this.methoeds.length];
        for (int i = 0; i < this.methoeds.length; i++){
            json_methods[i] = this.methoeds[i].convertToJson();
        }
        invocations.put("invocations", this.invocations);

        classJSON.put("class", this.class_name);
        classJSON.put("harnessParameters", invocations);
        classJSON.put("methods", json_methods);
        return classJSON;
    }
}
