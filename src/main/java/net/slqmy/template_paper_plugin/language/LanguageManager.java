package net.slqmy.template_paper_plugin.language;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.slqmy.template_paper_plugin.TemplatePaperPlugin;
import net.slqmy.template_paper_plugin.data.player.PlayerProfile;
import net.slqmy.template_paper_plugin.util.FileUtil;

import java.util.UUID;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class LanguageManager {

  private final MiniMessage miniMessage = MiniMessage.miniMessage();

  private final TemplatePaperPlugin plugin;

  private final String languagesFolderName = "languages";
  private final String languagesFolderPath;
  private final File languagesFolder;

  private final String defaultLanguage;

  private final Map<String, Map<Message, String>> languages = new HashMap<>();

  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  public Set<String> getLanguages() {
    return languages.keySet();
  }

  public LanguageManager(TemplatePaperPlugin plugin) {
    this.plugin = plugin;

    File dataFolder = plugin.getDataFolder();
    languagesFolderPath = dataFolder.getPath() + File.separator + languagesFolderName;
    languagesFolder = new File(languagesFolderPath);

    saveLanguageFiles();
    loadLanguageMessages();

    defaultLanguage = plugin.getConfig().getString("default-language");
  }

  private void saveLanguageFiles() {
    String languagesResourceFolderName = languagesFolderName + "/";
    List<String> languageResourceFileNames = FileUtil.getResourceFolderResourceFileNames(languagesResourceFolderName);

    languageResourceFileNames.forEach((fileName) -> plugin.saveResource(languagesFolderName + File.separator + fileName, false));
  }

  private void loadLanguageMessages() {
    for (File languageMessagesFile : languagesFolder.listFiles()) {
      String languageName = languageMessagesFile.getName().split("\\.", 2)[0];

      String languageMessagesResourcePath = languagesFolderName + File.separator + languageName + ".yaml";
      plugin.saveResource(languageMessagesResourcePath, false);

      YamlConfiguration messagesConfiguration = YamlConfiguration.loadConfiguration(languageMessagesFile);
      Map<Message, String> messages = new HashMap<>();

      for (Message message : Message.values()) {
        String mappedResult = messagesConfiguration.getString(message.name());

        if (mappedResult != null) {
          messages.put(message, mappedResult);
        }
      }

      languages.put(languageName, messages);
    }
  }

  public Component getMessage(Message message, String language, Object... arguments) {
    Map<Message, String> languageMessageMap = languages.get(language);
    String miniMessageString = languageMessageMap.get(message);

    if (miniMessageString == null) {
      return getMessage(message, defaultLanguage, arguments);
    }

    return miniMessage.deserialize(String.format(miniMessageString, arguments));
  }

  public Component getMessage(Message message, PlayerProfile playerProfile, Object... arguments) {
    String selectedLanguage = playerProfile.getLanguage();

    return getMessage(message, selectedLanguage != null ? selectedLanguage : defaultLanguage, arguments);
  }

  public Component getMessage(Message message, UUID playerUuid, Object... arguments) {
    return getMessage(message, plugin.getPlayerDataManager().getPlayerProfile(playerUuid), arguments);
  }

  public Component getMessage(Message message, Player player, Object... arguments) {
    return getMessage(message, player.getUniqueId(), arguments);
  }

  public Component getMessage(Message message, CommandSender commandSender, Object... arguments) {
    String language;

    if (commandSender instanceof Player player) {
      language = plugin.getPlayerDataManager().getPlayerProfile(player.getUniqueId()).getLanguage();
    } else {
      language = defaultLanguage;
    }

    return getMessage(message, language, arguments);
  }
}
