package org.herac.tuxguitar.io.guitarduino;


public class FretLightLeds {

  private byte[][] buffer;

  public FretLightLeds () {
    // initialize buffer array
    System.out.println("Leds init");
    buffer = new byte[3][7];
    clearBuffer();
    buffer[0][6] = 1;
    buffer[1][6] = 2;
    buffer[2][6] = 3;
  }
  
  public void clearBuffer() {
    for (int i=0; i<6; i++) {
      buffer[0][i] = 0;
      buffer[1][i] = 0;
      buffer[2][i] = 0;
    }
  }
  
  public void setAllOn() {
    for (int i=0; i<6; i++) {
      buffer[0][i] = -127;
      buffer[1][i] = -127;
      buffer[2][i] = -127;
    }
  }
  
  public void setAllOff() {
    clearBuffer();
  }
  
  public void setOn(int string, int fret) {
    int exp, index, zone;
    byte val;
    
    zone = fret/8;
    exp = (7-string+2*fret)%8;
    index = 6*zone+5-(fret*6+string)/8;
    
    val = (byte) (1 << exp);    
    if ( (buffer[zone][index] & val) == 0 ) buffer[zone][index] += val;
  }
  
  public byte[][] getBuffer() {
    return buffer;
  }
  
  
  
}