# MiniTLV: a simple TLV parser
MinitTLV let you convert your UTF-8 String into a Type-Length-Value byte array and reciprocally.
The result is as short as possible without compression.
The type and length have a dynamically resolved size (1, 2 or 4 bytes) using the `0x00` reserved byte for extension.
The value field is of variable size (up to 2<sup>32</sup>-1).
Additionally you can compress and/or encrypt serialized data.

Method | Description
------ | -----------
*getWriter* | Create a new instance of Serializer (using your own Converter or not).
*getReader* | Create a new instance of Parser (using your own Converter or not).
*getCipher* | Create a new instance of Crypto.
*getCompacter* | Create a new instance of Compressor.

## Serializer
Method | Description
------ | -----------
*write* | Write a Type-Length-Value for the given type and value..
*serialize* | ..and store them as 1, 2 or 4-bytes.

## Parser
Method | Description
------ | -----------
*read* | Read the Type-Length-Value bytes..
*parse* | ..and extract value (for the given 1, 2 or 4-bytes type).

## Crypto
Method | Description
------ | -----------
*add* | Add bytes.
*encrypt* | Encrypt the byte array with AES-128.
*decrypt* | Decrypt the byte array.

## Compressor
Method | Description
------ | -----------
*add* | Add bytes.
*deflate* | Compress the byte array with DEFLATE.
*inflate* | Decompress the byte array.

## Converter
Method | Description
------ | -----------
*convertToInt* | Convert byte array to integer.
*convertToBytes* | Convert short or integer to byte array.
