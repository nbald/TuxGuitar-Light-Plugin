package org.herac.tuxguitar.leds.stroke;

import java.nio.FloatBuffer;
import java.util.Map;

import com.ritolaaudio.GPL;
import com.ritolaaudio.javajack.helper.SimpleJACKClient;
import com.ritolaaudio.javajack.port.MIDIInputPort;
import com.ritolaaudio.javajack.port.MIDIOutputPort;

import org.herac.tuxguitar.leds.sequencer.LedsMidiSequencerImpl;

public class StrokeDetect
        {
	private LedsMidiSequencerImpl seq;

        public StrokeDetect(LedsMidiSequencerImpl sequencer)
                {
                this.seq = sequencer;
                try{new SimpleJACKClient("TuxGuitar-Stroke-Detection")
                        {
	  
                                private static final long serialVersionUID = 8312905434657214802L;
				
				private float lastMoy;
				private float maxMoy;
				
				private long count = 0;
				private long lastCount = 0;
				
                                @Override
                                public String[] getAudioInputPortNames()
                                        {return new String []{"GuitarIn"};}

                                @Override
                                public String[] getAudioOutputPortNames()
                                        {return null;};

                                @Override
                                public String[] getMIDIInputPortNames()
                                        {return null;}

                                @Override
                                public String[] getMIDIOutputPortNames()
                                        {return null;}

                                
                                        
                                @Override
                                public void process(Map<String, FloatBuffer> audioIn,
                                                Map<String, FloatBuffer> audioOut,
                                                Map<String, MIDIInputPort> midiIn,
                                                Map<String, MIDIOutputPort> midiOut)
                                        {
                                        
                                        final FloatBuffer aIn = audioIn.get("GuitarIn");
                                        final int nFrames=aIn.capacity();

                                        float moy = 0;
                                        float volDiff;
                                        long countDiff;
                                        
                                        
                                        for(int i=0; i<nFrames; i++)
                                                {
                                                final float inFloat=aIn.get(i);
                                                moy += Math.abs(inFloat/nFrames);
                                        }//end process()
                                        
                                        volDiff = moy-lastMoy;
                                        
                                       if (volDiff > 0.08f) {
					countDiff = count - lastCount;
					if (countDiff > 5) {
					  System.out.print(volDiff);
					  System.out.print('\t');
					  System.out.print(countDiff);
					  System.out.print('\t');
					  System.out.print(count);
					  System.out.println();
					  seq.play();
					 }
					}

					lastMoy = moy;
                                        count++;
                                       //end process()
				    };
                        
                        };
                        }
                catch(Exception e){e.printStackTrace();}
                }//end StrokeDetect()
                
                
        }//end StrokeDetect