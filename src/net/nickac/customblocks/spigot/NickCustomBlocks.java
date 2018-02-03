package net.nickac.customblocks.spigot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;

import lombok.Getter;
import lombok.val;
import net.nickac.customblocks.core.CustomBlock;
import net.nickac.customblocks.spigot.gui.MenuGUIListener;
import net.nickac.customblocks.utils.YamlConfig;

public class NickCustomBlocks extends JavaPlugin {
	@Getter
	static NickCustomBlocks instance;
	YamlConfig config;
	@Getter
	HashMap<Integer, CustomBlock> customBlocks = new HashMap<>();

	@Override
	public void onEnable() {
		instance = this;
		
		Bukkit.getPluginManager().registerEvents(new MenuGUIListener(), this);
		
		config = new YamlConfig("config.yml", this);
		config.saveDefault();
		updateCustomBlocks();
		prepareResourceFolder();

	}

	private void log(String msg) {
		Bukkit.getConsoleSender().sendMessage("[NickCustomBlocks] " + msg);
	}

	private void prepareResourceFolder() {
		if (getDataFolder().exists()) {

			val blocksFolder = new File(getDataFolder(), "blocks");
			if (!config.getBooleanOrSetDefault("DoneFirstRun", false, true)) {

				if (!blocksFolder.exists())
					blocksFolder.mkdir();

				InputStream block0 = getResource("block0.png");
				val block0OldFile = new File(getDataFolder(), "block0.png");
				val block0File = new File(blocksFolder, "block0.png");
				if (block0 != null) {
					saveResource("block0.png", false);
					try {
						Files.move(block0OldFile, block0File);
					} catch (IOException e) {
						log("An error occured saving default texture.");
						e.printStackTrace();
					}
				}
			}

			generateAssetsFolder(blocksFolder);

		}

	}

	private void generateAssetsFolder(final java.io.File blocksFolder) {
		File assetsFolder = new File(getDataFolder(), "assets");
		if (assetsFolder.exists())
			try {
				FileUtils.deleteDirectory(assetsFolder);
			} catch (IOException e1) {
				log("An error occured trying to prune old files.");
				e1.printStackTrace();
			}

		assetsFolder.mkdir();

		File minecraftFolder = new File(assetsFolder, "minecraft");
		minecraftFolder.mkdir();
		File modelsFolder = new File(minecraftFolder, "models");
		File texturesFolder = new File(minecraftFolder, "textures");
		modelsFolder.mkdir();
		texturesFolder.mkdir();

		File blockTexturesFolder = new File(texturesFolder, "blocks");

		File blockFolder = new File(modelsFolder, "block");
		File itemFolder = new File(modelsFolder, "item");
		blockTexturesFolder.mkdir();
		blockFolder.mkdir();
		itemFolder.mkdir();

		File diamondHoeItem = new File(itemFolder, "diamond_hoe.json");

		InputStream diamondHoe = getResource("diamond_hoe.json");
		val diamondHoeItemOldFile = new File(getDataFolder(), "diamond_hoe.json");
		if (diamondHoe != null) {
			saveResource("diamond_hoe.json", false);
			try {
				Files.move(diamondHoeItemOldFile, diamondHoeItem);
			} catch (IOException e) {
				log("An error occured saving default model.");
				e.printStackTrace();
			}
		}

		InputStream customBlock = getResource("customblock_base.json");
		val customBlockOldFile = new File(getDataFolder(), "customblock_base.json");
		File customBlockFinal = new File(blockFolder, "customblock_base.json");
		if (customBlock != null) {
			saveResource("customblock_base.json", false);
			try {
				Files.move(customBlockOldFile, customBlockFinal);
			} catch (IOException e) {
				log("An error occured saving cube model.");
				e.printStackTrace();
			}
		}

		try {
			FileUtils.copyDirectory(blocksFolder, blockTexturesFolder);
		} catch (IOException e) {
			log("An error occured copying the textures.");
			e.printStackTrace();
		}

		for (CustomBlock cBlock : getCustomBlocks().values()) {

			File thisBlockFile = new File(blockFolder, "custom_block" + cBlock.getBlockNumber() + ".json");
			PrintWriter writer = null;
			FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(thisBlockFile, true);
				writer = new PrintWriter(fileWriter);
				writer.print(cBlock.generateModel());
				fileWriter.close();
				writer.close();
			} catch (IOException e) {
				log("An error occured saving custom block model.");
				e.printStackTrace();
			}
		}
	}

	private void updateCustomBlocks() {
		val cBlocks = config.getConfigurationSection("CustomBlocks");
		if (cBlocks != null) {
			val keys = cBlocks.getKeys(false);
			for (String block : keys) {
				try {
					int id = config.getInt("CustomBlocks." + block + ".BlockID");
					if (customBlocks.containsKey(id)) {
						log("The id " + id + " has already been registered.");
					} else {
						String name = config.getString("CustomBlocks." + block + ".BlockName", "Block" + id);
						boolean transparent = config.getBoolean("CustomBlocks." + block + ".Transparent", false);
						boolean directionIndepentent = config
								.getBoolean("CustomBlocks." + block + ".DirectionIndependent");
						val cBlock = new CustomBlock(name, id, transparent, directionIndepentent);
						customBlocks.put(id, cBlock);
					}
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void onDisable() {

		instance = null;
	}

}
