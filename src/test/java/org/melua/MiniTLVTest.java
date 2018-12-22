package org.melua;

import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MiniTLVTest {
	
	private static final int BUFFER_MAX = 4096;
	
	//private int type;
	private String value;

	@Before	
	public void setUp() {
		//type = RandomUtils.nextInt();
		value = RandomStringUtils.random(RandomUtils.nextInt(0, 1000));
	}
	
	@Test
	public void encDec1() {
		System.out.println("type = " + 257);
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, (byte)0x01));
		byte[] tlv = MiniTLV.minimalBytes(buffer);
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = MiniTLV.parse(tlv, (byte)0x01);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void encDec2() {
		System.out.println("type = " + 257);
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, (byte)0x01, (byte)0x01));
		byte[] tlv = MiniTLV.minimalBytes(buffer);
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = MiniTLV.parse(tlv, (byte)0x01, (byte)0x01);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void encDec3() {
		System.out.println("type = " + 257);
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x01));
		byte[] tlv = MiniTLV.minimalBytes(buffer);
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = MiniTLV.parse(tlv, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x01);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}

}
