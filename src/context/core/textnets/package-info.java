/**
 * This package contains a generic suite of utilities for generating networks from text data.
 * Throughout this suite we make the following assumptions:
 * Corpus: Map of text_stream with file name.
 * TextStream: A list of tokens based on the parsing of the text. The tokens will have properties like labels.
 * 				If the corpus is parsed into multi words entities like in the case of codebook or entity extraction
 * 				or any other implementation then in that case the tokens will be multi word entitities. 
 * 				The text stream should also have an Enum labels which should include all the labels for the stream. 
 * TokenElement: Each token element should have the following properties:
 * 				String text
 * 				Corpus.label
 * 				  
 */
/**
 * @author Shubhanshu
 *
 */
package context.core.textnets;