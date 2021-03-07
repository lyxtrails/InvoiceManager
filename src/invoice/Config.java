package invoice;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    public static final String FILE_SAVE_PATH_KEY = "FILE_SAVE_PATH";
    public static final String COMPANY_NAME_KEY = "COMPANY_NAME";
    public static final String COMPANY_PHONE_NUMBER_KEY = "COMPANY_PHONE_NUMBER";
    public static final String COMPANY_EMAIL_KEY = "COMPANY_EMAIL";

    private static String defaultFileSavePath =
            Paths.get(System.getProperty("user.home"), "invoices").toString();
//            Paths.get(System.getProperty("java.io.tmpdir"),  "invoices").toString();
    private static String defaultCompanyName = "Company Name";
    private static String defaultCompanyPhoneNumber = "(123)456-7890";
    private static String defaultCompanyEmail = "company@company.com";

    public static Map<String, String> getConfigurations() {
        Map<String, String> configs = new HashMap<>();
        File configFile = new File("config.properties");
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            configs.put(FILE_SAVE_PATH_KEY, props.getProperty(FILE_SAVE_PATH_KEY));
            configs.put(COMPANY_NAME_KEY, props.getProperty(COMPANY_NAME_KEY));
            configs.put(COMPANY_PHONE_NUMBER_KEY, props.getProperty(COMPANY_PHONE_NUMBER_KEY));
            configs.put(COMPANY_EMAIL_KEY, props.getProperty(COMPANY_EMAIL_KEY));
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Property file does not exist, create one.");
            configs.put(FILE_SAVE_PATH_KEY, defaultFileSavePath);
            configs.put(COMPANY_NAME_KEY, defaultCompanyName);
            configs.put(COMPANY_PHONE_NUMBER_KEY, defaultCompanyPhoneNumber);
            configs.put(COMPANY_EMAIL_KEY, defaultCompanyEmail);
            saveConfigurations(configs);
        } catch (IOException ex) {
            ex.printStackTrace();
            // I/O error
        }
        return configs;
    }

    public static void saveConfigurations(Map<String, String> configs) {
        File configFile = new File("config.properties");
        try {
            configFile.createNewFile();
            Properties props = new Properties();
            for (Map.Entry<String,String> entry : configs.entrySet()) {
                props.setProperty(entry.getKey(), entry.getValue());
            }
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Save configs");
            writer.close();
        } catch (FileNotFoundException ex2) {
            ex2.printStackTrace();
            // file does not exist
        } catch (IOException ex2) {
            ex2.printStackTrace();
            // I/O error
        }
    }

}
