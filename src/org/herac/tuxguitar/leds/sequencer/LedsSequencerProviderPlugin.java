package org.herac.tuxguitar.leds.sequencer;

import org.herac.tuxguitar.gui.system.plugins.TGPluginException;
import org.herac.tuxguitar.gui.system.plugins.base.TGMidiSequencerProviderPlugin;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;

public class LedsSequencerProviderPlugin extends TGMidiSequencerProviderPlugin {
	
	private MidiSequencerProvider ledsSequencerProvider;
	
	public LedsSequencerProviderPlugin(){
		this.ledsSequencerProvider = new LedsSequencerProvider();
	}
	
	/*public LedsSequencerProviderPlugin(){
		this(new JackClient());
	}
	
	public LedsSequencerProviderPlugin(JackClient jackClient){
		this.ledsSequencerProvider = new JackSequencerProvider( jackClient );
	}*/
	
	protected MidiSequencerProvider getProvider() throws TGPluginException {
		return this.ledsSequencerProvider;
	}
}
