package org.esoteric.minecraft.plugins.template.language;

import org.esoteric.minecraft.plugins.template.custom.items.CustomItem;
import org.esoteric.minecraft.plugins.template.custom.multiblocks.CustomMultiblock;
import org.esoteric.minecraft.plugins.template.custom.multientities.CustomMultientity;

import java.util.function.BooleanSupplier;

public enum Message {
  SET_LANGUAGE_SUCCESSFULLY(() -> false),
  UNKNOWN_LANGUAGE(() -> false),

  UNKNOWN_CUSTOM_ITEM(CustomItem::isEnabled),
  UNKNOWN_CUSTOM_MULTIENTITY(CustomMultientity::isEnabled),
  UNKNOWN_CUSTOM_MULTIBLOCK(CustomMultiblock::isEnabled);

  private final BooleanSupplier isMessageInUseSupplier;

  public boolean isMessageInUse() {
    return isMessageInUseSupplier.getAsBoolean();
  }

  Message(BooleanSupplier isMessageInUseSupplier) {
    this.isMessageInUseSupplier = isMessageInUseSupplier;
  }

  Message() {
    this(() -> true);
  }

  public static boolean isEnabled() {
    for (Message message : values()) {
      if (message.isMessageInUse()) {
        return true;
      }
    }

    return false;
  }
}
