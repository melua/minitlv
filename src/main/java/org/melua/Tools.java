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

import static org.melua.MiniTLV.EXTENTED_BYTES;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

public class Tools {
	
	private static final int UBYTE_MAXVALUE = 255;
	private static final int USHORT_MAXVALUE = 65_535;
	
	protected static final int BYTE_SIZE = 1;
	protected static final int SHORT_SIZE = 2;
	protected static final int INT_SIZE = 4;
	
	/**
	 * Automatically add extra {@link #EXTENTED_BYTES} for 2 and 4-bytes type.
	 * @param buffer to append
	 * @param type to check
	 */
	protected static void addExtendedType(ByteBuffer buffer, byte[] type) {
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
	protected static void addExtendedLength(ByteBuffer buffer, int length) {
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
	protected static int convertToInt(byte[] bytes) {
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
	protected static byte[] minimalBytes(ByteBuffer buffer) {
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
	protected static ByteBuffer minimalBuffer(int value) {
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
	protected static ByteBuffer getBuffer(DataInputStream stream) throws IOException {
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
	
	/**
	 * Convert short to 2-bytes array.
	 * @param value to convert
	 * @return byte array
	 */
	protected static byte[] convertToBytes(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(SHORT_SIZE);
		buffer.putShort(value);
		return buffer.array();
	}
	
	/**
	 * Convert integer to 4-bytes array.
	 * @param value to convert
	 * @return byte array
	 */
	protected static byte[] convertToBytes(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(INT_SIZE);
		buffer.putInt(value);
		return buffer.array();
	}

}
