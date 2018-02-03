package net.nickac.customblocks.spigot.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.nickac.customblocks.spigot.NickCustomBlocks;

public class MenuGUIListener implements Listener {

	JavaPlugin pl = NickCustomBlocks.getInstance();
	
	@EventHandler()
	public void on(InventoryClickEvent e) {
		MenuGUI.checkForMenuClick(pl, e, true);
	}
	@EventHandler()
	public void on(InventoryCloseEvent e) {
		MenuGUI.checkForMenuClose(pl, e);
	}
	
}
