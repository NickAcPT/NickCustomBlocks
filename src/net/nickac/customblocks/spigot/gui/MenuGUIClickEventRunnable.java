package net.nickac.customblocks.spigot.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class MenuGUIClickEventRunnable implements Runnable {

	
	public MenuGUIClickEventRunnable() {
	}

	@Override
	public void run() {
	}

	public abstract void run(InventoryClickEvent e);
}
