package com.creativemd.creativecore.common.utils;

import com.creativemd.creativecore.core.CreativeCoreClient;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TickUtils {
	
	@SideOnly(Side.CLIENT)
	private static float getPartialTickTimeClient()
	{
		return CreativeCoreClient.getRenderPartialTicks();
	}
	
	public static float getPartialTickTime()
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			return getPartialTickTimeClient();
		return 1.0F;
	}
	
}
