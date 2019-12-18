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

import static org.melua.MiniTLV.BYTE_SIZE;
import static org.melua.MiniTLV.INT_SIZE;
import static org.melua.MiniTLV.SHORT_SIZE;

import java.nio.ByteBuffer;

import org.melua.api.Converter;

/**
 * A simple implementation of Converter
 * which uses {@link java.nio.ByteBuffer ByteBuffer}
 *
 */
public class DefaultConverter implements Converter {
	
	protected DefaultConverter() {
	}
	
	/**
	 * Convert byte array to 4-bytes integer.
	 * @param bytes to convert
	 * @return integer
	 */
	@Override
	public int convertToInt(byte[] bytes) {
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
	 * Convert short to 2-bytes array.
	 * @param value to convert
	 * @return byte array
	 */
	@Override
	public byte[] convertToBytes(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(SHORT_SIZE);
		buffer.putShort(value);
		return buffer.array();
	}
	
	/**
	 * Convert integer to 4-bytes array.
	 * @param value to convert
	 * @return byte array
	 */
	@Override
	public byte[] convertToBytes(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(INT_SIZE);
		buffer.putInt(value);
		return buffer.array();
	}

}
