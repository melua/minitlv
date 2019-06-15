package org.melua.api;

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

import java.io.IOException;
import java.util.zip.DataFormatException;

public interface Compressor {

	/**
	 * Compress the given byte array.
	 * @param data to compress
	 * @param bufferSize in bytes
	 * @return compressed data
	 * @throws IOException
	 */
	byte[] deflate(int bufferSize) throws IOException;

	/**
	 * Decompress the given byte array.
	 * @param data to decompress
	 * @param bufferSize in bytes
	 * @return decompressed data
	 * @throws IOException
	 * @throws DataFormatException
	 */
	byte[] inflate(int bufferSize) throws IOException, DataFormatException;

	/**
	 * Add the given bytes to buffer
	 * for later processing
	 * @param data to buffer
	 * @return this
	 * @throws IOException
	 */
	Compressor add(byte[] data) throws IOException;

}