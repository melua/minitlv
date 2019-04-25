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

public class MiniTLV {

	public static final byte EXTENTED_BYTES = 0x00;

	protected static final String TYPE_ERROR = "Type must be represented as 1, 2 or 4 bytes.";
	protected static final String INPUT_ERROR = "Invalid input.";

	protected static final int TLV_MINSIZE = 3;
	protected static final int EXT_MAXSIZE = 2;

	private MiniTLV() {
	}

	/**
	 * Retrieve an instance.
	 *
	 * @return a new instance
	 */
	public static TlvSerializer getWriter() {
		return new TlvSerializer();
	}
	
	/**
	 * Retrieve an instance.
	 *
	 * @return a new instance
	 */
	public static TlvParser getReader() {
		return new TlvParser();
	}
	
	/**
	 * Retrieve an instance.
	 *
	 * @return a new instance
	 */
	public static TlvCrypto getCipher() {
		return new TlvCrypto();
	}
	
	/**
	 * Retrieve an instance.
	 *
	 * @return a new instance
	 */
	public static TlvCompressor getCompacter() {
		return new TlvCompressor();
	}

}
