package net.nickac.customblocks.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YamlConfig extends YamlConfiguration {

	private File bruteFile;
	private Plugin plugin;

	public YamlConfig(File file) {

		this.bruteFile = file;
		try {
			if (!this.bruteFile.exists()) {
				this.saveDefault();
			}

			this.load(this.bruteFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public YamlConfig(String name, Plugin plugin) {
		this.plugin = plugin;

		this.bruteFile = new File(plugin.getDataFolder(), name.matches(".*(?i).yml$") ? name : name.concat(".yml"));
		try {
			if (!plugin.getDataFolder().exists()) {
					plugin.getDataFolder().mkdir();

			}

			if (!this.bruteFile.exists()) {
					this.saveDefault();
			}

			this.load(this.bruteFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public void reload() {
		try {
			this.load(this.bruteFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			super.save(this.bruteFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveDefault() {
		if (this.plugin.getResource(this.bruteFile.getName()) == null) {
			System.err.println("[" + this.plugin.getName() + "] Unable to save default ");
			System.err.println("[" + this.plugin.getName() + "] config file " + this.bruteFile.getName()
					+ " because the jar file");
			System.err.println("[" + this.plugin.getName() + "] does not contain any file with that name.");
		} else {
			if (!this.bruteFile.exists()) {
				this.plugin.saveResource(this.bruteFile.getName(), true);
			}

		}
	}

	@Override
	public void set(String path, Object obj) {
		super.set(path, obj);
		this.save();
	}

	public Object getOrSetDefault(String path, Object defaultValue) {
		if (isSet(path)) {
			return get(path);
		}
		super.set(path, defaultValue);
		this.save();
		return defaultValue;
	}
	public boolean getBooleanOrSetDefault(String path, boolean defaultValue, boolean toSave) {
		if (isSet(path)) {
			return getBoolean(path);
		}
		super.set(path, toSave);
		this.save();
		return defaultValue;
	}
}