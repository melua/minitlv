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

import static org.melua.MiniTLV.INPUT_ERROR;
import static org.melua.MiniTLV.TLV_MINSIZE;
import static org.melua.MiniTLV.TYPE_ERROR;
import static org.melua.util.Tools.BYTE_SIZE;
import static org.melua.util.Tools.INT_SIZE;
import static org.melua.util.Tools.SHORT_SIZE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.melua.api.Parser;
import org.melua.util.Tools;

public class MiniTLVParser implements Parser {
	
	private ByteArrayOutputStream innerStream = new ByteArrayOutputStream();
	
	protected MiniTLVParser() {
	}
	
	@Override
	public byte[] parse(byte... type) throws IOException {
		
		/*
		 * Convert stream to byte array
		 */
		byte[] tlv = this.innerStream.toByteArray();

		/*
		 * Prevent bad TLV
		 */
		if (tlv.length < TLV_MINSIZE) {
			throw new IllegalArgumentException(INPUT_ERROR);
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
		int givenType = Tools.convertToInt(type);

		try (DataInputStream stream = new DataInputStream(new ByteArrayInputStream(tlv))) {

			while(stream.available() >= TLV_MINSIZE) {
				
				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int currentType = Tools.convertToInt(Tools.minimalBytes(Tools.getBuffer(stream)));
				
				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int length = Tools.convertToInt(Tools.minimalBytes(Tools.getBuffer(stream)));
				
				/*
				 * Read or skip value
				 */
				if (stream.available() >= length) {
					if (currentType == givenType) {
						byte[] value = new byte[length];
						stream.readFully(value);
						return value;
					} else {
						stream.skip(length);
					}
				}
			}
		}

		/*
		 * Type not found
		 */
		return null;
	}
	
	@Override
	public Map<Integer, byte[]> parse() throws IOException {

		/*
		 * Convert stream to byte array
		 */
		byte[] tlv = this.innerStream.toByteArray();

		/*
		 * Prevent bad TLV
		 */
		if (tlv.length < TLV_MINSIZE) {
			throw new IllegalArgumentException(INPUT_ERROR);
		}

		Map<Integer, byte[]> map = new HashMap<>();

		try (DataInputStream stream = new DataInputStream(new ByteArrayInputStream(tlv))) {

			while(stream.available() >= TLV_MINSIZE) {

				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int type = Tools.convertToInt(Tools.minimalBytes(Tools.getBuffer(stream)));

				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int length = Tools.convertToInt(Tools.minimalBytes(Tools.getBuffer(stream)));

				/*
				 * Read or skip value
				 */
				if (stream.available() >= length) {
					if (!map.containsKey(type)) {
						byte[] value = new byte[length];
						stream.readFully(value);
						map.put(type, value);
					} else {
						stream.skip(length);
					}
				}
			}
		}

		return map;
	}
	
	@Override
	public Parser read(byte[] tlv) throws IOException {
		this.innerStream.write(tlv);
		return this;
	}

}
