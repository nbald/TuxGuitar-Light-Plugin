package org.herac.tuxguitar.leds.sequencer;

import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequenceHandler;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiTransmitter;

import org.herac.tuxguitar.player.impl.sequencer.MidiEvent;
import org.herac.tuxguitar.leds.stroke.StrokeDetect;

public class LedsMidiSequencerImpl implements MidiSequencer{
	
	private boolean reset;
	private boolean running;
	private boolean stopped;
	private MidiTransmitter transmitter;
	private LedsMidiTickPlayer midiTickPlayer;
	private LedsMidiEventPlayer midiEventPlayer;
	private LedsMidiEventDispacher midiEventDispacher;
	private LedsMidiTrackController midiTrackController;
	private StrokeDetect stroke;
	
	public LedsMidiSequencerImpl() {
		System.out.println("Init Leds Sequencer");
		this.running = false;
		this.stopped = true;
		this.midiTickPlayer = new LedsMidiTickPlayer();
		this.midiEventPlayer = new LedsMidiEventPlayer(this);
		this.midiEventDispacher = new LedsMidiEventDispacher(this);
		this.midiTrackController = new LedsMidiTrackController(this);
		this.stroke = new StrokeDetect(this);
	}
	
	
	public synchronized void pause () {
	    this.midiTickPlayer.pause();
	}
	public synchronized void play () {
	    this.midiTickPlayer.play();
	}
	
	public synchronized LedsMidiTrackController getMidiTrackController(){
		return this.midiTrackController;
	}
	
	public synchronized void setTempo(int tempo){
		this.midiTickPlayer.setTempo(tempo);
	}
	
	public synchronized long getTickPosition(){
		return this.midiTickPlayer.getTick();
	}
	
	public synchronized void setTickPosition(long tickPosition){
		this.reset = true;
		this.midiTickPlayer.setTick(tickPosition);
	}
	
	public synchronized long getTickLength(){
		return this.midiTickPlayer.getTickLength();
	}
	
	public synchronized void sendEvent(MidiEvent event) throws MidiPlayerException{
		if(!this.reset){
			this.midiEventDispacher.dispatch(event);
		}
	}
	
	public synchronized void addEvent(MidiEvent event){
		this.midiEventPlayer.addEvent(event);
		this.midiTickPlayer.notifyTick(event.getTick());
	}
	
	public synchronized boolean isRunning() {
		return this.running;
	}
	
	public synchronized void setRunning(boolean running) throws MidiPlayerException {
		this.running = running;
		if(this.running){
			this.setTempo(120);
			this.setTickPosition( this.getTickPosition() );
			new MidiTimer(this).start();
		}else{
			this.process();
		}
	}
	
	public synchronized void stop() throws MidiPlayerException{
		this.setRunning(false);
	}
	
	public synchronized void start() throws MidiPlayerException{
		this.setRunning(true);
	}
	
	public synchronized void reset(boolean systemReset)  throws MidiPlayerException{
		this.getTransmitter().sendAllNotesOff();
		for(int channel = 0; channel < 16;channel ++){
			this.getTransmitter().sendPitchBend(channel, 64);
		}
		if( systemReset ){
			this.getTransmitter().sendSystemReset();
		}
	}
	
	protected synchronized boolean process() throws MidiPlayerException{
		boolean running = this.isRunning();
		if(running){
			if(this.reset){
				this.reset( false );
				this.reset = false;
				this.midiEventPlayer.reset();
			}
			this.stopped = false;
			this.midiTickPlayer.process();
			this.midiEventPlayer.process();
			if(this.getTickPosition() > this.getTickLength()){
				this.stop();
			}
		}
		else if( !this.stopped ){
			this.stopped = true;
			this.midiEventPlayer.clearEvents();
			this.midiTickPlayer.clearTick();
			this.reset( true );
		}
		return running;
	}
	
	public synchronized MidiTransmitter getTransmitter() {
		return this.transmitter;
	}
	
	public synchronized void setTransmitter(MidiTransmitter transmitter) {
		this.transmitter = transmitter;
	}
	
	public void check() {
		// Not implemented
	}
	
	public synchronized void open() {
		//not implemented
	}
	
	public synchronized void close() throws MidiPlayerException {
		if(isRunning()){
			this.stop();
		}
	}
	
	public synchronized MidiSequenceHandler createSequence(int tracks) throws MidiPlayerException{
		return new LedsMidiSequenceHandlerImpl(this,tracks);
	}
	
	public synchronized void setSolo(int index,boolean solo) throws MidiPlayerException{
		this.getMidiTrackController().setSolo(index, solo);
	}
	
	public synchronized void setMute(int index,boolean mute) throws MidiPlayerException{
		this.getMidiTrackController().setMute(index, mute);
	}
	
	public String getKey() {
		return "light.sequencer";
	}
	
	public String getName() {
		return "Light Sequencer";
	}
	
	private class MidiTimer extends Thread{
		
		private static final int TIMER_DELAY = 15; //15 original
		
		private LedsMidiSequencerImpl sequencer;
		
		public MidiTimer(LedsMidiSequencerImpl sequencer){
			this.sequencer = sequencer;
		}
		
		public void run() {
			try {
				synchronized(this.sequencer) {
					while( this.sequencer.process() ){
						this.sequencer.wait( TIMER_DELAY );
					}
				}
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}
}
