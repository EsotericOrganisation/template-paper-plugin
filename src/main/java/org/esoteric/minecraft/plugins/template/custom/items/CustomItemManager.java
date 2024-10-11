package org.esoteric.minecraft.plugins.template.custom.items;

import org.bukkit.entity.Player;
import org.esoteric.minecraft.plugins.template.TemplatePaperPlugin;

import java.util.HashMap;
import java.util.Map;

public class CustomItemManager {

  private final TemplatePaperPlugin plugin;

  private final Map<CustomItem, AbstractCustomItem> customItemMap = new HashMap<>();

  public TemplatePaperPlugin getPlugin() {
    return plugin;
  }

  public CustomItemManager(TemplatePaperPlugin plugin) {
    this.plugin = plugin;
  }

  public void addCustomItem(CustomItem itemId, AbstractCustomItem customItem) {
    customItemMap.put(itemId, customItem);
  }

  public AbstractCustomItem getAbstractCustomItem(CustomItem itemId) {
    return customItemMap.get(itemId);
  }

  public void giveCustomItem(CustomItem itemId, Player player) {
    customItemMap.get(itemId).give(player);
  }
}
