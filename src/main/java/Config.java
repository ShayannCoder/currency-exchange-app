import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static String getApiKey() {
        Properties prop = new Properties();
        // This looks for the file in the root of your project
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            return prop.getProperty("api.key");
        } catch (IOException ex) {
            System.out.println("Error: Could not find config.properties file!");
            ex.printStackTrace();
            return null;
        }
    }
}