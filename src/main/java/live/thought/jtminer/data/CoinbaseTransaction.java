/*
 * jtminer Java mining software for the Thought Network
 * 
 * Copyright (c) 2018, Thought Network LLC
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, version 2, as 
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package live.thought.jtminer.data;

import java.util.Arrays;

public class CoinbaseTransaction implements Hexable
{
  protected int    version    = 1;
  protected int    inCounter  = 1;
  protected long   height;
  protected int    outCounter = 1;
  protected long   value;
  protected String address;
  protected int    lockTime   = 0;

  public CoinbaseTransaction(long height, long value, String coinbaseAddress)
  {
    this.height = height;
    this.value = value;
    this.address = coinbaseAddress;
  }

  public int getVersion()
  {
    return version;
  }

  public void setVersion(int version)
  {
    this.version = version;
  }

  public int getInCounter()
  {
    return inCounter;
  }

  public void setInCounter(int inCounter)
  {
    this.inCounter = inCounter;
  }

  public long getHeight()
  {
    return height;
  }

  public void setHeight(long height)
  {
    this.height = height;
  }

  public int getOutCounter()
  {
    return outCounter;
  }

  public void setOutCounter(int outCounter)
  {
    this.outCounter = outCounter;
  }

  public long getValue()
  {
    return value;
  }

  public void setValue(long value)
  {
    this.value = value;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public int getLockTime()
  {
    return lockTime;
  }

  public void setLockTime(int lockTime)
  {
    this.lockTime = lockTime;
  }

  @Override
  public byte[] getHex()
  {
    ByteArray cbtx = new ByteArray();
    
    cbtx.append(DataUtils.hexStringToByteArray(String.format("%08X", Integer.reverseBytes(version))));
    cbtx.append((byte) 0x01);
    
    byte[] empty = new byte[32];
    cbtx.append(empty);
    
    byte[] seq = DataUtils.hexStringToByteArray("ffffffff");
    cbtx.append(seq);
    
    byte[] heightCompact = DataUtils.encodeCompact(height);
    int scriptSize = heightCompact.length + 1;
    // BIP-34 height
    cbtx.append(DataUtils.encodeCompact(scriptSize));
    cbtx.append((byte) 0x03);
    cbtx.append(heightCompact);
    
    
    // Sequence
    seq = DataUtils.hexStringToByteArray("00000000");
    cbtx.append(seq);
    // tx_out counter
    cbtx.append((byte) 0x01);
    
    byte[] val = DataUtils.hexStringToByteArray(Long.toHexString(Long.reverseBytes(value)));
    cbtx.append(val);
    
    // Address size
    byte[] len = DataUtils.hexStringToByteArray(Integer.toHexString(Integer.reverseBytes(address.length())));
    
    cbtx.append(len);
    // Payment Address
    cbtx.append(address.getBytes());
    
    // Lock time
    byte[] loc = { 0x00, 0x00, 0x00, 0x00 };
    cbtx.append(loc);
    
    
    // Not supporting coinbase signature yet

    // Thought doesn't have any coinbaseaux flags at the moment
    
    return cbtx.get();
  }
}
