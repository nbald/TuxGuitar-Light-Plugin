package org.herac.tuxguitar.io.guitarduino;

import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import ch.ntb.usb.USBException;
import java.lang.*;

public class GDPUSB {

  private Device dev;
  private boolean ready = false;
  
  private static void logData(byte[] data) {
    System.out.print("Data: ");
    for (int i = 0; i < data.length; i++) {
      System.out.print("0x" + Integer.toHexString(data[i] & 0xff) + " ");
    }
    System.out.println();
  }
  
  public byte ub(int x) {
    return (byte) ((int) x & 0xFF);
  } 
  
  public GDPUSB () {
  
    byte[][] data = { {30, 30, 30, 30, 30, 30, 1}, {30, 30, 30, 30, 30, 30, 2}, {30, 0, 0, 0, 0, 0, 3} };
    // data read from the device
    byte[] readData = new byte[1];
  
    // VENDOR and PRODUCT ids of the Fretlight
    dev = USB.getDevice((short) 0x0925, (short) 0x2000);
    try {
    
    dev.open(1, 0, -1);
    } catch (USBException e) {
      e.printStackTrace();
    }
 

      for (int i=0; i<6; i++) {
      data[0][i] = 50;
      data[1][i] = 50;
      data[2][i] = 50;
      }
      send(data);

  }

  public void send (byte[][] buffer) {
    byte[] readBuf = {-1};

    try {
      dev.writeInterrupt(0x02, buffer[0], 7, 1000, false);
      dev.readInterrupt(0x01, readBuf, 1, 1000, false);
      dev.writeInterrupt(0x02, buffer[1], 7, 1000, false);
      dev.readInterrupt(0x01, readBuf, 1, 1000, false);
      dev.writeInterrupt(0x02, buffer[2], 7, 1000, false);
      dev.readInterrupt(0x01, readBuf, 1, 1000, false);
    } catch (USBException e) {
      //e.printStackTrace();
    }
    
    
    ready = true; 
  }
  
  public boolean isReady() {
    return ready;
  }
}


