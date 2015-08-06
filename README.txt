ConText: Network Construction from Texts
===================

ConText supports a) the construction of network data from natural language text data, a process also known as relation extraction and b) the joint analysis of text data and network data.


Contributors:
-------------

**PI:** Jana Diesner <jdiesner@illinois.edu>

* Amirhossein Aleyasen <aleyase2@illinois.edu>
* Chieh-Li Chin <cchin6@illinois.edu>
* Shubhanshu Mishra <smishra8@illinois.edu>
* Kiumars Soltani <soltani2@illinois.edu>
* Liang Tao <ltao3@illinois.edu>



License:
--------

### ConText Application Executable Files
The ConText application executable files (i.e. ConText.dmg, ConText-1.0-x64.exe, ConText-1.0-x86.exe, ConText.jar, and ConText.zip) are licensed under **GNU General Public License version 3.0 or later** license.

The executable files include the following:
* The application code, packaged into a set of JAR files, plus any other application resources (data files, native libraries)
* A private copy of the Java and JavaFX Runtimes, to be used by this application only
* A native launcher for the application
* Metadata, such as icons


Copyright (c) 2015 University of Illinois Board of Trustees, All rights reserved. Other copyright statements provided below.

Developed at GSLIS/ the iSchool, by Dr. Jana Diesner, Amirhossein Aleyasen, Chieh-Li Chin, Shubhanshu Mishra, Kiumars Soltani, and Liang Tao.


### U of I Source Codes
The following files are released under **GNU General Public License version 2.0 or later** license:
* All files in directory "build"
* All files in directory "installer"
* All files in directory "logo"
* All files in directory "src"
* .classpath, .project, build.fxbuild, build.xml, mainfest.mf, and train_model.sh

Copyright (c) 2015 University of Illinois Board of Trustees, All rights reserved.

Developed at GSLIS/ the iSchool, by Dr. Jana Diesner, Amirhossein Aleyasen, Chieh-Li Chin, Shubhanshu Mishra, Kiumars Soltani, and Liang Tao.


### Dependencies
The following dependencies are required for the application, and should be used under their licenses.


#### Apache License 2.0: 

* Apache: Commons BeanUtils (http://commons.apache.org/proper/commons-beanutils), Commons Codec (http://commons.apache.org/proper/commons-codec/), Commons Collections (http://commons.apache.org/proper/commons-collections), Commons Digester (http://commons.apache.org/proper/commons-digester), Commons IO (http://commons.apache.org/proper/commons-io), Commons JEXL (http://commons.apache.org/proper/commons-jexl), Commons Lang (http://commons.apache.org/proper/commons-lang), Commons Logging (http://commons.apache.org/proper/commons-logging), POI (http://poi.apache.org)
 * Apache License 2.0

* Joda-Time:
 * http://www.joda.org/joda-time/
 * Apache License 2.0

* Jollyday:
 * http://jollyday.sourceforge.net/
 * Apache License 2.0

* JSONIC:
 * http://en.sourceforge.jp/projects/jsonic/
 * Apache License 2.0

* language-detection:
 * http://code.google.com/p/language-detection/
 * Apache License 2.0

* OpenCSV:
 * http://opencsv.sourceforge.net/ 
 * Apache license 2.0
 * Copyright 2007,2010 Kyle Miller.

* Sentiment Word Clusters
 * https://github.com/napsternxg/SentimentWordClusters
 * Apache License 2.0


#### GNU License:

* Gephi Toolkit
 * http://gephi.org/toolkit/
 * GNU General Public License v3.0

* jXLS:
 * http://jxls.sourceforge.net
 * GNU Lesser General Public License v3.0

* MALLET
 * http://mallet.cs.umass.edu
 * GNU General Public License v3.0
 * McCallum, Andrew Kachites.  "MALLET: A Machine Learning for Language Toolkit."
    http://mallet.cs.umass.edu. 2002.

* Stanford CoreNLP:
 * http://nlp.stanford.edu/software/corenlp.shtml
 * GNU General Public License (v3 or later)
 * Manning, Christopher D., Surdeanu, Mihai, Bauer, John, Finkel, Jenny, Bethard, Steven J., and McClosky, David. 2014. The Stanford CoreNLP Natural Language Processing Toolkit (http://nlp.stanford.edu/pubs/StanfordCoreNlp2014.pdf). In Proceedings of 52nd Annual Meeting of the Association for Computational Linguistics: System Demonstrations, pp. 55-60. [[pdf](http://nlp.stanford.edu/pubs/StanfordCoreNlp2014.pdf)] [[bib](http://nlp.stanford.edu/pubs/StanfordCoreNlp2014.bib)]

* Stanford Named Entity Recognizer (NER)
 * http://nlp.stanford.edu/software/CRF-NER.shtml
 * GNU General Public License (v2 or later)
 
 * Note: We are using the following files in the data/Classifiers folder: 
 english.all.3class.distsim.crf.ser.gz, english.all.3class.distsim.prop,
 english.conll.4class.distsim.crf.ser.gz, english.conll.4class.distsim.prop,
 english.muc.7class.distsim.crf.ser.gz, english.muc.7class.distsim.prop,
 ner-eng-ie.crf-3-all2008-distsim.ser.gz, ner-eng-ie.crf-3-all2008.ser.gz,
 ner-eng-ie.crf-4-conll-distsim.ser.gz, ner-eng-ie.crf-4-conll.ser.gz


* Stanford Parser:
 * http://nlp.stanford.edu/software/lex-parser.shtml
 * GNU General Public License (v2 or later)

 * Note: We've removed the following models from stanford-parser-3.4.1-models.jar: arabicFactored.ser.gz, chineseFactored.ser.gz, chinesePCFG.ser.gz, englishRNN.ser.gz, frenchFactored.ser.gz, germanFactored.ser.gz, germanPCFG.ser.gz, wsjRNN.ser.gz
 
* Dan Klein and Christopher D. Manning. 2003. Accurate Unlexicalized Parsing (http://nlp.stanford.edu/~manning/papers/unlexicalized-parsing.pdf). Proceedings of the 41st Meeting of the Association for Computational Linguistics, pp. 423-430.
 * Dan Klein and Christopher D. Manning. 2003. Fast Exact Inference with a Factored Model for Natural Language Parsing (http://www-nlp.stanford.edu/~manning/papers/lex-parser.pdf). In Advances in Neural Information Processing Systems 15 (NIPS 2002), Cambridge, MA: MIT Press, pp. 3-10.
 * Marie-Catherine de Marneffe, Bill MacCartney and Christopher D. Manning. 2006. Generating Typed Dependency Parses from Phrase Structure Parses (http://nlp.stanford.edu/pubs/LREC06_dependencies.pdf). In LREC 2006.


* Stanford POS Tagger:
 * http://nlp.stanford.edu/software/tagger.shtml
 * GNU General Public License (v2 or later)
 * Kristina Toutanova, Dan Klein, Christopher Manning, and Yoram Singer. 2003. Feature-Rich Part-of-Speech Tagging with a Cyclic Dependency Network (http://nlp.stanford.edu/~manning/papers/tagging.pdf). In Proceedings of HLT-NAACL 2003, pp. 252-259.

* Subjectivity Lexicon: 
 * http://mpqa.cs.pitt.edu/lexicons/subj_lexicon/
 * GNU General Public License v3.0
 * Theresa Wilson, Janyce Wiebe, and Paul Hoffmann (2005). Recognizing Contextual Polarity in Phrase-Level Sentiment Analysis (http://www.cs.pitt.edu/~wiebe/pubs/papers/emnlp05polarity.pdf). Proceedings of HLT/EMNLP-2005.

* Trove:
 * http://trove.starlight-systems.com/license
 * GNU Lesser General Public License (LGPL) 2.1 or later

* XOM:
 * http://www.xom.nu/
 * GNU Lesser General Public License (LGPL) 2.1


#### Other Licenses:

* D3.js:
 * http://d3js.org/
 * 3-Clause BSD License(https://github.com/mbostock/d3/blob/master/LICENSE)
 * Copyright (c) 2010-2015, Michael Bostock

* MonologFX:
 * https://github.com/hecklerm/MonologFX
 * MIT License(https://github.com/hecklerm/MonologFX/blob/master/LICENSE.md)

* SecondString:
 * http://secondstring.sourceforge.net/
 * University of Illinois/NCSA Open Source License (http://secondstring.cvs.sourceforge.net/viewvc/secondstring/secondstring/LICENSE.txt?revision=1.1&view=markup&pathrev=HEAD)


Citing ConText:
-------------
While not a condition of use, the developers would appreciate if you acknowledge its use with a citation:

Diesner, J., Aleyasen, A., Chin, C., Mishra, S., Soltani, K., Tao, L. (2015). ConText: Network Construction from Texts [Software]. Available from http://context.lis.illinois.edu/




