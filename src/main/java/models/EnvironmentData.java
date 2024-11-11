package models;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentData {
    public Map<?,?> jsonServer;
    public HashMap<String, String> urls;

    public EnvironmentData() {
        Yaml yaml = new Yaml();
        File fileCredentials = new File("./src/test/resources/testdata/testData.yml");

        try (FileInputStream inputStream = new FileInputStream(fileCredentials)) {
            Map<String, Object> data = yaml.load(inputStream);
            jsonServer = (HashMap<String, HashMap<String, String>>) data.get("json_server");
            urls = (HashMap<String, String>)jsonServer.get("urls");

        } catch (FileNotFoundException e) {
            System.err.println("Error: testData.yml file not found.");
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println("Error: YAML data structure does not match expected format.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error loading YAML file.");
            e.printStackTrace();
        }
    }

}

