package ws.zettabyte.zettalib.block;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.EnumFacing;

/**
 * A container of six icons associated with each side of a cube. 
 * 
 * A convenience object for rotatable blocks - so you don't have to write the whole "which side is this" 
 * code every time you have a block with a distinct front, back, etc...
 * @author Sam "Gyro" C.
 *
 */
public class CubeIconSet {

	protected String textures[] = new String[6];
	protected IIcon icons[] = new IIcon[6];

	public CubeIconSet() {}
	public CubeIconSet(String defTex) {
		for(int i = 0; i < 6; ++i) {
			textures[i] = defTex;
		}
	}
	public CubeIconSet(String defTex, String topBottomTex) {
		this(defTex);
		textures[RotationTools.EnumFacingToIndex(EnumFacing.UP)] = topBottomTex;
		textures[RotationTools.EnumFacingToIndex(EnumFacing.DOWN)] = topBottomTex;
	}
	public void setTextureName(String name, EnumFacing side) {
		textures[RotationTools.EnumFacingToIndex(side)] = name;
	}
	public void setAllSidesName(String name) { 
		for(int i = 0; i < 6; ++i) {
			textures[i] = name;
		}
	}
	public void registerBlockIcons(IIconRegister iconRegister) {
		HashMap<String, Integer> repeat = new HashMap<String, Integer>();
		for(int i = 0; i < 6; ++i) {
			//Don't re-register textures we have already registered for this.
			if(repeat.containsKey(textures[i])){
				icons[i] = icons[repeat.get(textures[i])]; //Set it to the original user of this texture's icon.
			} else { //Haven't done this one in this run? Register, add to repeat checker.
				icons[i] = iconRegister.registerIcon(textures[i]);
				repeat.put(textures[i], i);
			}
		}
	}

	public IIcon getIconFurnaceStyle(EnumFacing side, EnumFacing facing) {
		return icons[RotationTools.EnumFacingToIndex(
				RotationTools.getTranslatedSideFStyle(side, facing))];
	}
	public IIcon getIconPistonStyle(EnumFacing side, EnumFacing facing) {
		return icons[RotationTools.EnumFacingToIndex(
				RotationTools.getTranslatedSidePStyle(side, facing))];
	}
    
    public String getTexture(EnumFacing side) {
    	return textures[RotationTools.EnumFacingToIndex(side)];
    }

    /**
     * Makes this object's texture names identical to the argument's texture names. 
     */
	public void makeCopy(CubeIconSet cpy) {
		for(int i = 0; i < 6; ++i) {
			textures[i] = cpy.getTexture(EnumFacing.VALID_DIRECTIONS[i]);
		}
	}

}
