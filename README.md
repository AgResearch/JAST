JAST
====

Java Assembling and Scaffolding Tool
-----------


JAST (Java Assembling and Scaffolding Tool) is a program performs assembling and scaffolding from paired-end Illumina files. It uses following softwares :
   * Flexbar [link](http://sourceforge.net/projects/flexbar/)
   * A5 [link](http://code.google.com/p/ngopt/wiki/A5PipelineREADME)
   * Bowtie [link](http://bowtie-bio.sourceforge.net/index.shtml)
   * Velvet Optimizer [link](http://bioinformatics.net.au/software.velvetoptimiser.shtml)
   * SSPACE [link](http://www.baseclear.com/lab-products/bioinformatics-tools/)


Please see their license agreements for further details and check they all are installed on your computer before using JAST.


The main aim of JAST is to let it manages intermediate steps between the several softwares.
JAST needs a config file for all steps (except A5 pipeline). These files contains options you want to use
for softwares above. Every options must be separated by a line break, you can find examples on https://github.com/AgResearch/JAST/tree/master/ExampleConfigFiles . Some options are forbidden because they are already
use by JAST (see help option for more informations). 

To know how to use JAST, use '-h' or '--help' option. For example : java -jar jast.jar -h


Extern library
----------

JAST use the JSAP library v2.1 downloaded in June 2014, under LGPL licence.

More informations [here](http://www.martiansoftware.com/jsap/)



GitHub Repository 
-----------

Here you can find :


   * Directory :
      * [javadoc](https://github.com/AgResearch/JAST/tree/master/Javadoc)
      * [source code](https://github.com/AgResearch/JAST/tree/master/src)
      * [Example of config files](https://github.com/AgResearch/JAST/tree/master/ExampleConfigFiles)
      * [Documentation](https://github.com/AgResearch/JAST/tree/master/Doc)

   * Files at the root :

      * JAR file
      * buildJAST.xml : the ant script used to generate the jar file (builded with Eclipse)
      * jast.zip containing both src and javadoc
      * jast.condor : an example of how to use jast with condor
    




Contact
-----------


For any enquiries please [contact the author](mailto:cclementddel@gmail.com)

Clement DELESTRE (c) 2014

