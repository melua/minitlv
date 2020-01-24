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

import org.melua.api.Compressor;
import org.melua.api.Converter;
import org.melua.api.Crypto;
import org.melua.api.Parser;
import org.melua.api.Serializer;

public class MiniTLV {
	
	public static final byte EXTENTED_BYTES = 0x00;
	
	protected static final int BYTE_SIZE = 1;
	protected static final int SHORT_SIZE = 2;
	protected static final int INT_SIZE = 4;

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
	 * @param converter the bytes converter
	 * @return a new instance
	 */
	public static Serializer getWriter(Converter converter) {
		return new MiniTLVSerializer(converter);
	}
	
	/**
	 * Retrieve an instance of the MiniTLV Serializer
	 * using {@link org.melua.DefaultConverter DefaultConverter}
	 *
	 * @return a new instance
	 */
	public static Serializer getWriter() {
		return new MiniTLVSerializer(new DefaultConverter());
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Parser
	 * 
	 * @param converter the bytes converter
	 * @return a new instance
	 */
	public static Parser getReader(Converter converter) {
		return new MiniTLVParser(converter);
	}
	
	/**
	 * Retrieve an instance of the MiniTLV Parser
	 * using {@link org.melua.DefaultConverter DefaultConverter}
	 *
	 * @return a new instance
	 */
	public static Parser getReader() {
		return new MiniTLVParser(new DefaultConverter());
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Crypto
	 *
	 * @param algo
	 * @return a new instance
	 */
	public static Crypto getCipher(Algorithm algo) {
		return new MiniTLVCrypto(algo);
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Crypto
	 * using {@link Algorithm#AES128 AES128}
	 * algorithm
	 *
	 * @return a new instance
	 */
	public static Crypto getCipher() {
		return new MiniTLVCrypto(Algorithm.AES128);
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Compressor
	 *
	 * @param level
	 * @return a new instance
	 */
	public static Compressor getCompacter(Level level) {
		return new MiniTLVCompressor(level);
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Compressor
	 * using {@link org.melua.Level#BALANCED BALANCED}
	 * compression level
	 *
	 * @return a new instance
	 */
	public static Compressor getCompacter() {
		return new MiniTLVCompressor(Level.BALANCED);
	}
	
}
