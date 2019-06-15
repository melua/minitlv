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
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.melua.api.Crypto;

public class MiniTLVCrypto implements Crypto {
	
	private static final int PBKDF2_ITERATIONS = 10_000;
	private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String AES_ALGORITHM = "AES";
	private static final int SALT_SIZE = 16;
	
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	protected MiniTLVCrypto() {
	}

	/**
	 * Computes the PBKDF2 hash.
	 *
	 * @param secret the password to hash
	 * @param salt the salt
	 * @return the PBDKF2 hash of the password
	 * @throws GeneralSecurityException
	 */
	private static byte[] pbkdf2(String secret, byte[] salt) throws GeneralSecurityException {
		KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt, PBKDF2_ITERATIONS, SALT_SIZE*8);
		return SecretKeyFactory.getInstance(PBKDF2_ALGORITHM).generateSecret(spec).getEncoded();
	}

	@Override
	public byte[] encrypt(String secret) throws GeneralSecurityException {
		
		/*
		 * Convert buffer to byte array
		 */
		byte[] data = this.buffer.toByteArray();

		/*
		 * Generate random salt
		 */
		byte[] salt = new byte[SALT_SIZE];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);

		/*
		 * Create cipher key with salt and password
		 */
		Key key = new SecretKeySpec(pbkdf2(secret, salt), AES_ALGORITHM);

		/*
		 * Encrypt data
		 */
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encrypted = cipher.doFinal(data);

		/*
		 * Return salt and encrypted data
		 */
		ByteBuffer result = ByteBuffer.allocate(salt.length + encrypted.length);
		result.put(salt);
		result.put(encrypted);

		return result.array();
    }

	@Override
	public byte[] decrypt(String secret) throws GeneralSecurityException {
		
		/*
		 * Convert buffer to byte array
		 */
		byte[] data = this.buffer.toByteArray();

		/*
		 * Extract salt
		 */
		byte[] salt = Arrays.copyOfRange(data, 0, SALT_SIZE);

		/*
		 * Create cipher key with salt and password
		 */
		Key key = new SecretKeySpec(pbkdf2(secret, salt), AES_ALGORITHM);

		/*
		 * Decrypt data
		 */
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);

		return cipher.doFinal(Arrays.copyOfRange(data, SALT_SIZE, data.length));
    }
	
	@Override
	public Crypto add(byte[] data) throws IOException {
		buffer.write(data);
		return this;
	}

}
