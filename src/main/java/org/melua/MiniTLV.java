package org.melua;

import org.melua.api.Compressor;
import org.melua.api.Crypto;
import org.melua.api.Parser;
import org.melua.api.Serializer;

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

public class MiniTLV {

	public static final byte EXTENTED_BYTES = 0x00;

	protected static final String TYPE_ERROR = "Type must be represented as 1, 2 or 4 bytes.";
	protected static final String INPUT_ERROR = "Invalid input.";

	protected static final int TLV_MINSIZE = 3;
	protected static final int EXT_MAXSIZE = 2;

	private MiniTLV() {
	}

	/**
	 * Retrieve an instance
	 * of the MiniTLV Serializer
	 *
	 * @return a new instance
	 */
	public static Serializer getWriter() {
		return new MiniTLVSerializer();
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Parser
	 *
	 * @return a new instance
	 */
	public static Parser getReader() {
		return new MiniTLVParser();
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Crypto
	 *
	 * @return a new instance
	 */
	public static Crypto getCipher() {
		return new MiniTLVCrypto();
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Compressor
	 *
	 * @return a new instance
	 */
	public static Compressor getCompacter() {
		return new MiniTLVCompressor();
	}

}
