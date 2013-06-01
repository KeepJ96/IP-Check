package net.risenphoenix.jnk.ipcheck.translation;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author FR34KYN01535
 */
public class TranslationManager {
    private Map<String,String> translation;
    private Map<String,String> defaultTranslation;
    private String selectedLanguage;
    private FileConfiguration loadedlanguage;
    
    public TranslationManager(String language){
        File f = new File(IPcheck.Instance.getDataFolder()+File.separator+language+".yml");
        if(f.exists()) {
            this.selectedLanguage=language;
        }else{
            selectedLanguage = "en";
            IPcheck.Instance.getLogger().warning("Languagefile "+language+".yml not found, falling back to english language!");
        }
        reloadTranslation();
    }
    public void reloadTranslation(){
        if(selectedLanguage.equals("en")){
            loadDefaults();
        }else{
            File languageFile = new File(IPcheck.Instance.getDataFolder()+File.separator+selectedLanguage+".yml");
            loadedlanguage = YamlConfiguration.loadConfiguration(languageFile);
            loadTranslation();
        }
    }
    public String getTranslation(String key){
        if(translation!=null){
            String value = translation.get("translation."+key);
            if(value==null || value.isEmpty()){
                return "Translation missing: "+key;
            }else{
                return value;
            }
        }
        else{
            return defaultTranslation.get(key);
        }
    }
    private void loadTranslation(){
        translation = new HashMap<String, String>();
        for(Map.Entry<String,Object> entry : loadedlanguage.getValues(true).entrySet()){
            translation.put(entry.getKey(),entry.getValue().toString());
        }
    }
    private void loadDefaults(){
        defaultTranslation = new HashMap<String, String>();
        defaultTranslation.put("BAN_LIST_READ_ERR","Error occurred while attempting to read banned-ips.txt!");
        defaultTranslation.put("NO_FIND","The player or IP specified could not be found.");
        defaultTranslation.put("NO_PERM_ERR","You don't have permission to do that!");
        defaultTranslation.put("NUM_ARGS_ERR","Incorrect Number of Arguments.");
        defaultTranslation.put("ILL_ARGS_ERR","Illegal Argument(s) were passed into the command.");
        defaultTranslation.put("PLAYER_EXEMPT_SUC","Player added to exemption list!");
        defaultTranslation.put("IP_EXEMPT_SUC","IP-Address added to exemption list!");
        defaultTranslation.put("EXEMPTION_FAIL","Sorry. :( Something went wrong. The exemption could not be added.");
        defaultTranslation.put("TOGGLE_SECURE","Secure-Mode set to: ");
        defaultTranslation.put("TOGGLE_NOTIFY","Notify-On-Login set to: ");
        defaultTranslation.put("TOGGLE_DETAIL","Descriptive-Notify set to: ");
        defaultTranslation.put("TOGGLE_ERR","An error occurred while attempting to set state of toggle.");
        defaultTranslation.put("EXEMPTION_DEL_SUC","Exemption successfully removed!");
        defaultTranslation.put("EXEMPTION_DEL_ERR","Exemption specified does not exist.");
        defaultTranslation.put("ERROR_LOG_RMDR","An error occurred! A log summary of this error has been saved to IP-Check's directory under ''Error_Reports''");
        defaultTranslation.put("PURGE_SUC","Sucessfully purged %s.");
        defaultTranslation.put("PURGE_ERR","Failed to purge %s!");
    }
    
}
