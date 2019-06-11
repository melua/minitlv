# MiniTLV: a (not so) simple TLV parser
MinitTLV let you convert your UTF-8 String into a Type-Length-Value byte array and reciprocally.
The result is as short as possible without compression.
The type and length have a dynamically resolved size (1, 2 or 4 bytes) using the `0x00` reserved byte for extension.
The value field is of variable size (up to 2<sup>32</sup>-1).
Additionally you can compress and/or encrypt serialized data.

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
*add* | Add bytes
*encrypt* | Encrypt the given byte array with AES-128.
*decrypt* | Decrypt the given byte array.

## Compressor
Method | Description
------ | -----------
*add* | Add bytes
*deflate* | Compress the given byte array with DEFLATE.
*inflate* | Decompress the given byte array.
