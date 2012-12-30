package org.herac.tuxguitar.leds.sequencer;

import org.herac.tuxguitar.song.models.TGDuration;

public class LedsMidiTickPlayer {
	
	private static final int SECOND_IN_MILLIS = 1000;
	
	private int tempo;
	private long tick;
	private long time;
	private long lastTime;
	private long tickLength;
	private boolean tickChanged;
	
	private boolean isPaused = false;
	
	public LedsMidiTickPlayer(){
		super();
	}
	
	public void process() {
		
		this.lastTime = this.time;
		this.time = System.currentTimeMillis();
		if(!this.tickChanged){
		  if (!this.isPaused) {
		    this.tick += (TGDuration.QUARTER_TIME * ((float)getTempo() * (float)(this.time - this.lastTime) / 60f) / SECOND_IN_MILLIS);
		  }
		}
		this.tickChanged = false;
	}
	
	public void clearTick(){
		this.tickLength = 0;
	}
	
	public int getTempo() {
		return this.tempo;
	}
	
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	public long getTick() {
		return this.tick;
	}
	
	public void setTick(long tick) {
		this.tick = tick;
		this.tickChanged = true;
	}
	
	public long getTickLength() {
		return this.tickLength;
	}
	
	public void notifyTick(long tick){
		this.tickLength = Math.max(this.tickLength,tick);
	}
	
	public void pause () {
		this.isPaused = true;
	}
	public void play () {
		this.isPaused = false;
	}
}
