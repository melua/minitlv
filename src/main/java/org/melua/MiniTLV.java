package org.melua;

/*
 * Copyright (C) 2018 Kevin Guignard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MiniTLV {

	public static final byte EXTENTED_BYTES = 0x00;
	private static final String TYPE_ERROR = "Type must be represented as 1, 2 or 4 bytes.";
	private static final Logger LOGGER = Logger.getLogger(MiniTLV.class.getName());
	private static final Charset CHARSET = StandardCharsets.UTF_8;
	
	private static final int TLV_MINLENGTH = 3;
	
	private static final int UBYTE_MAXVALUE = 255;
	private static final int USHORT_MAXVALUE = 65535;
	
	private static final int BYTE_SIZE = 1;
	private static final int SHORT_SIZE = 2;
	private static final int INT_SIZE = 4;

	/**
	 * Read the Type-Length-Value bytes and extract value for the given 1, 2 or 4-bytes type.
	 * From 0x01 (1) to 0xff (255) the type must be represented as one byte.
	 * From 0x0100 (256) to 0xffff (65535) the type must be represented as two bytes,
	 * from 0x010000 (65536) to 0xffffffff (4294967295) the type must be represented as four bytes,
	 * and must be given in {@link ByteOrder#BIG_ENDIAN} order.
	 * 
	 * @param tlv bytes to read
	 * @param type to search for
	 * @return value for the given type
	 */
	public static String parse(byte[] tlv, byte... type) {

		/*
		 * Prevent bad TLV
		 */
		if (tlv == null || tlv.length < TLV_MINLENGTH) {
			return null;
		}
		
		/*
		 * Prevent bad type
		 */
		if (type.length != BYTE_SIZE && type.length != SHORT_SIZE && type.length != INT_SIZE) {
			throw new IllegalArgumentException(TYPE_ERROR);
		}
		
		/*
		 * Convert type byte array to integer
		 */
		int givenType = convertToInt(type);

		try (DataInputStream stream = new DataInputStream(new ByteArrayInputStream(tlv))) {

			while(stream.available() >= TLV_MINLENGTH) {
				
				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int currentType = convertToInt(minimalBytes(getBuffer(stream)));
				
				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int length = convertToInt(minimalBytes(getBuffer(stream)));
				
				/*
				 * Read or skip value
				 */
				if (stream.available() >= length) {
					if (currentType == givenType) {
						byte[] value = new byte[length];
						stream.readFully(value);
						return new String(value, CHARSET);
					} else {
						stream.skip(length);
					}
				}
			}
			
		} catch (IOException ioex) {
			LOGGER.log(Level.WARNING, "Error while parsing TLV: {0}", ioex.getMessage());
		}

		/*
		 * Type not found
		 */
		return null;
	}
	
	/**
	 * Write a Type-Length-Value for the given type and integer value.
	 * @see #serialize(String, byte...)
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return byte in Type-Length-Value representation
	 */
	public static byte[] serialize(int value, byte... type) {
		return serialize(String.valueOf(value), type);
	}
	
	/**
	 * Write a Type-Length-Value for the given type and value,
	 * and store them as 1, 2 or 4-bytes.
	 * From 0x01 (1) to 0xff (255) type and length are represented as one byte.
	 * From 0x0100 (256) to 0xffff (65535) type and length are represented as two bytes,
	 * from 0x010000 (65536) to 0xffffffff (4294967295) type and length are represented as four bytes,
	 * and must be given in {@link ByteOrder#BIG_ENDIAN} order. An extra
	 * {@link #EXTENTED_BYTES} byte is automatically added for 2 and 4-bytes type and length.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return byte in Type-Length-Value representation
	 */
	public static byte[] serialize(String value, byte... type) {
		
		/*
		 * Prevent bad value
		 */
		if (value == null) {
			return null;
		}
		
		/*
		 * Prevent bad type
		 */
		if (type.length != BYTE_SIZE && type.length != SHORT_SIZE && type.length != INT_SIZE) {
			throw new IllegalArgumentException(TYPE_ERROR);
		}
		
		/*
		 * Convert string value to byte array
		 */		
		byte[] bytes = value.getBytes(CHARSET);
		
		/*
		 * Prepare type and add extended marks if necessary
		 */
		ByteBuffer tbuffer = ByteBuffer.allocate(6);
		addExtendedType(tbuffer, type);
		tbuffer.put(type);
		byte[] givenType = minimalBytes(tbuffer);
		
		/*
		 * Prepare length and add extended marks if necessary
		 */
		ByteBuffer lbuffer = ByteBuffer.allocate(6);
		addExtendedLength(lbuffer, bytes.length);
		lbuffer.put(minimalBytes(minimalBuffer(bytes.length)));
		byte[] length = minimalBytes(lbuffer);
		
		/*
		 * Create Type-Length-Value with calculated size
		 */
		ByteBuffer buffer = ByteBuffer.allocate(givenType.length + length.length + bytes.length);
		buffer.put(givenType);
		buffer.put(length);
		buffer.put(bytes);
		return buffer.array();
	}
	
	/**
	 * Automatically add extra {@link #EXTENTED_BYTES} for 2 and 4-bytes type.
	 * @param buffer to append
	 * @param type to check
	 */
	private static void addExtendedType(ByteBuffer buffer, byte[] type) {
		switch (type.length) {
		default:
			throw new IllegalArgumentException();
		case INT_SIZE:
			buffer.put(EXTENTED_BYTES);
		case SHORT_SIZE:
			buffer.put(EXTENTED_BYTES);
		case BYTE_SIZE:
		}
	}
	
	/**
	 * Automatically add extra {@link #EXTENTED_BYTES} for 2 and 4-bytes length.
	 * @param buffer to append
	 * @param length to check
	 */
	private static void addExtendedLength(ByteBuffer buffer, int length) {
		if (length > UBYTE_MAXVALUE) {
			buffer.put(EXTENTED_BYTES);
			if (length > USHORT_MAXVALUE) {
				buffer.put(EXTENTED_BYTES);
			}
		}
	}
	
	/**
	 * Convert byte array to 4-bytes integer.
	 * @param bytes to convert
	 * @return integer
	 */
	private static int convertToInt(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(INT_SIZE);
		switch (bytes.length) {
		default:
			throw new IllegalArgumentException();
		case BYTE_SIZE:
			buffer.put((byte) 0x00);
		case SHORT_SIZE:
			buffer.putShort((short) 0x0000);
		case INT_SIZE:
			buffer.put(bytes);
			buffer.flip();
		}
		return buffer.getInt();
	}
	
	/**
	 * Extract the shortest byte array from the given byte buffer.
	 * @param buffer
	 * @return byte array
	 */
	public static byte[] minimalBytes(ByteBuffer buffer) {
		buffer.flip();
		byte[] result = new byte[buffer.limit()];
		buffer.get(result, 0, buffer.limit());
		return result;	
	}
	
	/**
	 * Create the shortest buffer from the given integer.
	 * @param value
	 * @return byte buffer
	 */
	private static ByteBuffer minimalBuffer(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(INT_SIZE);
		if (value > UBYTE_MAXVALUE) {
			if (value > USHORT_MAXVALUE) {
				buffer.putInt(value);
			} else {
				buffer.putShort((short) value);
			}
		} else {
			buffer.put((byte) value);
		}
		return buffer;
	}
	
	/**
	 * Read the given stream and extract type and length
	 * according to the extra {@link #EXTENTED_BYTES}.
	 * @param stream to read
	 * @return byte buffer
	 * @throws IOException
	 */
	private static ByteBuffer getBuffer(DataInputStream stream) throws IOException {
		reading:
		for(int bytes = 1; stream.available() >= bytes; bytes *= 2) {
			int result = stream.readByte();
			if (result != EXTENTED_BYTES) {
				ByteBuffer buffer = ByteBuffer.allocate(INT_SIZE);
				buffer.put((byte) result);
				switch (bytes) {
				case BYTE_SIZE:
					break;
				case SHORT_SIZE:
					buffer.put((byte) stream.readByte());
					break;
				case INT_SIZE:
					buffer.put((byte) stream.readByte());
					buffer.putShort((short) stream.readShort());
					break;
				default:
					break reading;
				}
				return buffer;
			}
		}
		throw new StreamCorruptedException();
	}

}
