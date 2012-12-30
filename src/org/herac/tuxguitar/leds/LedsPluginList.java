package org.herac.tuxguitar.leds;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.gui.system.plugins.TGPluginSetup;
import org.herac.tuxguitar.gui.system.plugins.base.TGMidiOutputPortProviderPlugin;
import org.herac.tuxguitar.gui.system.plugins.base.TGMidiSequencerProviderPlugin;
import org.herac.tuxguitar.gui.system.plugins.base.TGPluginList;
import org.herac.tuxguitar.player.base.MidiOutputPortProvider;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;

import org.herac.tuxguitar.leds.sequencer.LedsSequencerProvider;

public class LedsPluginList extends TGPluginList implements TGPluginSetup{
	
	protected List getPlugins() {
		List plugins = new ArrayList();
		plugins.add(new TGMidiSequencerProviderPlugin() {
			protected LedsSequencerProvider getProvider() {
				return new LedsSequencerProvider();
			}
		});
		return plugins;
	}
	
	public void setupDialog(Shell parent) {
		//MidiConfigUtils.setupDialog(parent);
	}
	
	public String getAuthor() {
		return "Nicolas";
	}
	
	public String getDescription() {
		return "Plugin for Frelight and GuitDuino led guitars";
	}
	
	public String getName() {
		return "Led Guitars plugin";
	}
	
	public String getVersion() {
		return "1.0";
	}
}
