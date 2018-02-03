package net.nickac.customblocks.core;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import net.nickac.customblocks.utils.ItemBuilder;

@Getter
@AllArgsConstructor
public class CustomBlock {
	
	public void placeBlock(Block b) {
		placeBlock(b.getLocation());
	}
	
	final String ENTITY_PREFIX = "CUSTOMBLOCK-";
	
	public String locToString(Location loc) {
		return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
	}

	
	public ItemStack getItem() {
		return new ItemBuilder(Material.DIAMOND_HOE).data(blockNumber).unBreakable().name("&f" + blockName).build();
	}
	
	public void placeBlock(Location l) {
		val toPlace = l.getBlock();

		toPlace.setType(Material.BARRIER);

		// Summon Armor Stand
		ArmorStand a = (ArmorStand) toPlace.getWorld().spawnEntity(toPlace.getLocation().clone().add(0.5, -1, 0.5),
				EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setCustomName(ENTITY_PREFIX + (locToString(toPlace.getLocation())));
		a.setCustomNameVisible(false);
		// a.setMarker(true);
		a.setRemoveWhenFarAway(false);
		a.setCollidable(false);
		a.setInvulnerable(true);
		a.setHelmet(getItem());
		a.setGravity(false);
	}
	
	public String generateModel() {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("{");
		builder.append(System.lineSeparator());
		builder.append("\t\"parent\": \"block/customblock_base\",");
		builder.append(System.lineSeparator());
		builder.append("\t\"textures\": {");
		builder.append(System.lineSeparator());
		builder.append("\t\t\"all\": \"blocks/block" + getBlockNumber() + "\"");
		builder.append(System.lineSeparator());
		builder.append("\t}");
		builder.append(System.lineSeparator());
		builder.append("}");
		
		return builder.toString();		
	}
	
	//The name displayed to the player
	private String blockName;
	//The internal id of the block
	private int blockNumber;
	//Is the block transparent ?
	//If it is, we should place barriers instead of glass
	private boolean transparent;
	//Do we need to rotate the block ?
	private boolean directionIndepentent;
}
