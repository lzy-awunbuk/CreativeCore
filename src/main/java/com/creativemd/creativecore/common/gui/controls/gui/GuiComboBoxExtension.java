package com.creativemd.creativecore.common.gui.controls.gui;

import java.util.List;

import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;

import net.minecraft.util.math.Vec3d;

public class GuiComboBoxExtension extends GuiListBox {
	
	public GuiComboBox comboBox;
	
	public GuiComboBoxExtension(String name, GuiComboBox comboBox, int x, int y, int width, int height, List<String> lines) {
		super(name, x, y, width, height, lines);
		this.comboBox = comboBox;
		this.selected = comboBox.index;
		reloadControls();
	}
	
	@Override
	public Vec3d getCenterOffset() {
		return new Vec3d(width / 2, -comboBox.height / 2, 0);
	}
	
	@Override
	public void onLoseFocus() {
		if (!comboBox.isMouseOver() && !isMouseOver())
			comboBox.closeBox();
	}
	
	@Override
	public void onSelectionChange() {
		if (selected != -1 && selected < lines.size()) {
			comboBox.caption = comboBox.getDisplay(selected);
			comboBox.index = selected;
			comboBox.raiseEvent(new GuiControlChangedEvent(comboBox));
		}
		comboBox.closeBox();
	}
	
	@Override
	public boolean canOverlap() {
		return true;
	}
	
}
