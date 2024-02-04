package org.project.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CredentialLoader {

    private List<Map<String, String>> users;

    public CredentialLoader() {
        // Load the YAML file upon instantiation
        InputStream inputStream = CredentialLoader.class.getResourceAsStream("/credentials.yaml");
        if (inputStream == null) {
            throw new RuntimeException("Could not load credentials.yaml");
        }
        Yaml yaml = new Yaml();
        users = yaml.load(inputStream);
    }

    public Map<String, String> getUserByName(String targetUserName) {
        for (Map<String, String> user : users) {
            String username = user.get("username");
            if (username != null && username.equals(targetUserName)) {
                return user;
            }
        }
        return null; // User not found
    }
}
