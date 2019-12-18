package org.melua.api;

public interface Converter {
	
	/**
	 * Convert byte array to 4-bytes integer.
	 * @param bytes to convert
	 * @return integer
	 */
	int convertToInt(byte[] bytes);
	
	
	/**
	 * Convert short to 2-bytes array.
	 * @param value to convert
	 * @return byte array
	 */
	byte[] convertToBytes(short value);
	
	/**
	 * Convert integer to 4-bytes array.
	 * @param value to convert
	 * @return byte array
	 */
	byte[] convertToBytes(int value);

}
