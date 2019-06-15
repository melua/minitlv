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
import java.security.GeneralSecurityException;

public interface Crypto {

	/**
	 * Encrypt data using given secret.
	 * @param data to encrypt
	 * @param secret used for encryption
	 * @return encrypted data with salt
	 * @throws GeneralSecurityException
	 */
	byte[] encrypt(String secret) throws GeneralSecurityException;

	/**
	 * Decrypt data with salt using given secret.
	 * @param data to decrypt
	 * @param secret used for decryption
	 * @return decrypted data
	 * @throws GeneralSecurityException
	 */
	byte[] decrypt(String secret) throws GeneralSecurityException;

	/**
	 * Add the given bytes to buffer
	 * for later processing
	 * @param data
	 * @return
	 * @throws IOException
	 */
	Crypto add(byte[] data) throws IOException;

}
