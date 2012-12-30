package org.herac.tuxguitar.leds.sequencer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;

public class LedsSequencerProvider implements MidiSequencerProvider{
	
	private List ledsSequencerProviders;
	
	public LedsSequencerProvider(){
	  super();
	}
	
	public List listSequencers() throws MidiPlayerException {
		if(this.ledsSequencerProviders == null){
			this.ledsSequencerProviders = new ArrayList();
			this.ledsSequencerProviders.add(new LedsMidiSequencerImpl());
		}
		return this.ledsSequencerProviders;
	}
	
	public void closeAll() throws MidiPlayerException {
		Iterator it = listSequencers().iterator();
		while(it.hasNext()){
			MidiSequencer sequencer = (MidiSequencer)it.next();
			sequencer.close();
		}
	}
	
}
