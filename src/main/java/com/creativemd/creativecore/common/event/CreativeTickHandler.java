package com.creativemd.creativecore.common.event;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.gui.mc.IVanillaGUI;
import com.creativemd.creativecore.common.tileentity.TileEntityCreative;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTickHandler {
	
	public static ArrayList<CreativeCoreEventBus> ServerEvents = new ArrayList<CreativeCoreEventBus>();
	
	@SubscribeEvent
	public void onEventTick(TickEvent tick) // Remove all Structures which doesn't have any connections
	{
		if (tick.phase == Phase.START && tick.type == Type.SERVER) {
			if (ServerEvents == null)
				ServerEvents = new ArrayList<CreativeCoreEventBus>();
			try {
				for (int i = 0; i < ServerEvents.size(); i++) {
					for (int j = 0; j < ServerEvents.get(i).eventsToRaise.size(); j++) {
						ServerEvents.get(i).raiseEvent(ServerEvents.get(i).eventsToRaise.get(j), true);
					}
					ServerEvents.get(i).eventsToRaise.clear();
				}
				// PacketReciever.refreshQueue(true);
			} catch (Exception e) {
				// It is ready to crash
			}
		}
	}
	
	@SubscribeEvent
	public void worldUnload(WorldEvent.Unload event) {
		for (TileEntity te : event.getWorld().loadedTileEntityList)
			if (te instanceof TileEntityCreative)
				te.onChunkUnload();
	}
	
	@SideOnly(Side.CLIENT)
	public static int defaultScale;
	@SideOnly(Side.CLIENT)
	public static boolean changed;
	
	@SideOnly(Side.CLIENT)
	public static ArrayList<CreativeCoreEventBus> ClientEvents;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTick(ClientTickEvent tick) {
		if (tick.phase == Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.player != null && mc.player.openContainer instanceof ContainerSub && ((ContainerSub) mc.player.openContainer).gui != null)
				((ContainerSub) mc.player.openContainer).gui.onTick();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTick(RenderTickEvent tick) {
		Minecraft mc = Minecraft.getMinecraft();
		if (tick.phase == Phase.START) {
			if (ClientEvents == null)
				ClientEvents = new ArrayList<CreativeCoreEventBus>();
			try {
				for (int i = 0; i < ClientEvents.size(); i++) {
					for (int j = 0; j < ClientEvents.get(i).eventsToRaise.size(); j++) {
						ClientEvents.get(i).raiseEvent(ClientEvents.get(i).eventsToRaise.get(j), true);
					}
					ClientEvents.get(i).eventsToRaise.clear();
				}
				// PacketReciever.refreshQueue(false);
			} catch (Exception e) {
				// It is ready to crash
			}
		}
		
		if (tick.phase == Phase.START && mc.currentScreen instanceof IVanillaGUI) {
			IVanillaGUI gui = (IVanillaGUI) mc.currentScreen;
			
			if (!changed)
				defaultScale = mc.gameSettings.guiScale;
			int maxScale = gui.getMaxScale(mc);
			int scale = Math.min(defaultScale, maxScale);
			if (defaultScale == 0)
				scale = maxScale;
			if (scale != mc.gameSettings.guiScale) {
				changed = true;
				mc.gameSettings.guiScale = scale;
				ScaledResolution scaledresolution = new ScaledResolution(mc);
				int k = scaledresolution.getScaledWidth();
				int l = scaledresolution.getScaledHeight();
				mc.currentScreen.setWorldAndResolution(mc, k, l);
			}
		} else if (tick.phase == Phase.START && changed) {
			changed = false;
			mc.gameSettings.guiScale = defaultScale;
			if (mc.currentScreen != null) {
				ScaledResolution scaledresolution = new ScaledResolution(mc);
				int k = scaledresolution.getScaledWidth();
				int l = scaledresolution.getScaledHeight();
				mc.currentScreen.setWorldAndResolution(mc, k, l);
			}
		}
	}
}
