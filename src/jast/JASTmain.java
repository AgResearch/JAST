/**
 *  JAST (Java Assembling and Scaffolding Tool) is a program performs assembling and scaffolding from paired-end Illumina files.
    Copyright (C) 2014 Clément DELESTRE (cclementddel@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jast;


import jast.commands.A5Command;
import jast.commands.BowtieIndexCommand;
import jast.commands.BowtieMapCommand;
import jast.commands.ColombusCommand;
import jast.commands.FlexbarCommand;
import jast.commands.SSPACEcommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.stringparsers.FileStringParser;

/**
 * JAST (Java Assembling and Scaffolding Tool) main class
 * @author Clément DELESTRE
 * @version 1.0
 * @since 1.0
 */
public class JASTmain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSAP jsap = new JSAP();
		FileStringParser fsp =  FileStringParser.getParser(); // call the factory
		fsp.setMustExist(true); // the file must exist

		// option for display help
		Switch help = new Switch("help")
		.setShortFlag('h')
		.setLongFlag("help");
		help.setHelp("Print help and exit.");

		// GNU GPL
		Switch warranty = new Switch("warranty")
		.setShortFlag('w')
		.setLongFlag("warranty");
		warranty.setHelp("Print warranty GNU-GPL and exit");

		//first input file
		FlaggedOption firstFile = new FlaggedOption("First input reads")
		.setStringParser(fsp) 
		.setRequired(true) 
		.setShortFlag('r')
		.setLongFlag("reads");
		firstFile.setHelp("First input file of paired reads (fastq format)");

		//second input file
		FlaggedOption secondFile = new FlaggedOption("Second input reads")
		.setStringParser(fsp) 
		.setRequired(true) 
		.setShortFlag('p')
		.setLongFlag("paired");
		secondFile.setHelp("Second input file of paired reads (fastq format)");

		//final output file
		FlaggedOption output = new FlaggedOption("Output file")
		.setStringParser(JSAP.STRING_PARSER) 
		.setRequired(true) 
		.setShortFlag('o')
		.setLongFlag("output");
		output.setHelp("Final output name");

		//Reference
		FlaggedOption reference = new FlaggedOption("Reference file")
		.setStringParser(fsp) 
		.setRequired(true) 
		.setLongFlag("ref");
		reference.setHelp("Referencec sequence (fasta format)");


		//Bowtie File Build
		FlaggedOption bowtieConfigBuild = new FlaggedOption("Bowtie config Build")
		.setStringParser(fsp) 
		.setRequired(false) 
		.setLongFlag("bowbuild");
		bowtieConfigBuild.setHelp("Config file use for bowtie-build command.");

		//Bowtie File Map
		FlaggedOption bowtieConfigMap = new FlaggedOption("Bowtie config Map")
		.setStringParser(fsp) 
		.setRequired(false) 
		.setLongFlag("bowmap");
		bowtieConfigMap.setHelp("Config file use for bowtie-x command. All mandatory options must be specified except : "+printForbiddenOptions(Pipeline.forbiddenBowtieMap));


		//SSPACE library file
		FlaggedOption sspacelib = new FlaggedOption("SSPACE library")
		.setStringParser(fsp) 
		.setRequired(true) 
		.setShortFlag('l')
		.setLongFlag("sspacelib");
		sspacelib.setHelp("SSPACE library file (see SSPACE doc, equivalent to file you specify with '-l' option when you use SSPACE).\nPlease note that the first line must contains only the 3 last columns : the other information (name and fastq files) will be writen in a file nammed with _JAST extension and the library will be nammed JASTlib.");

		// Flexbar
		FlaggedOption flexbarFile = new FlaggedOption("flexbar config file")
		.setStringParser(fsp) 
		.setRequired(false) 
		.setLongFlag("flexbar");
		flexbarFile.setHelp("Flexbar config file. All mandatory options must be specified except : "+printForbiddenOptions(Pipeline.forbiddenFlexbar));

		// Colombus
		FlaggedOption colombusFile = new FlaggedOption("Colombus config file")
		.setStringParser(fsp) 
		.setRequired(true) 
		.setLongFlag("colombus");
		colombusFile.setHelp("Colombus config file. All mandatory options must be specified except : "+printForbiddenOptions(Pipeline.forbiddenColombus));

		// SSPACE
		FlaggedOption sspaceFile = new FlaggedOption("SSPACE config file")
		.setStringParser(fsp) 
		.setRequired(false) 
		.setLongFlag("sspace");
		sspaceFile.setHelp("SSPACE config file. All mandatory options must be specified except : "+printForbiddenOptions(Pipeline.forbiddenSSPACE));

		try {
			jsap.registerParameter(help);
			jsap.registerParameter(warranty);
			jsap.registerParameter(firstFile);
			jsap.registerParameter(secondFile);
			jsap.registerParameter(output);
			jsap.registerParameter(bowtieConfigBuild);
			jsap.registerParameter(bowtieConfigMap);
			jsap.registerParameter(flexbarFile);
			jsap.registerParameter(colombusFile);
			jsap.registerParameter(reference);
			jsap.registerParameter(sspaceFile);
			jsap.registerParameter(sspacelib);
		} catch (JSAPException e) {
			System.err.println("[JAST_Error] with JSAP Parameter :");
			e.printStackTrace();
		}
		JSAPResult config = jsap.parse(args);
		if (config.getBoolean("help"))
			displayUsage(jsap);
		if (config.getBoolean("warranty")){
			System.out.println(JASTutils.getWarranty());
			System.exit(2);
		}
		System.out.println("[JAST] Starting "+JASTutils.appliName+"...");
		if (config.success()) {
			Pipeline p = new Pipeline(jsap,args);
			p.runPipeline();
		}
		else {
			for (Iterator errs = config.getErrorMessageIterator();
					errs.hasNext();) {
				System.err.println("[JAST_Error] : " + errs.next());
			}
		}
	}

	/**
	 * Get string containing forbidden options. 
	 * @param forbiddenOptions
	 * @return forbiddenOptions
	 */
	private static String printForbiddenOptions(String[] forbiddenOptions) {
		String toReturn="\n";
		for (String s : forbiddenOptions){
			toReturn+=s+"\n";
		}
		return toReturn;
	}

	public static void displayUsage(JSAP jsap){
		System.out.println(JASTutils.getCopyright());
		System.out.println("Description : ");
		System.out.println(JASTutils.getDescription());
		System.out.println("Usage: java -jar jast.jar "+ jsap.getUsage()+"\n");
		System.out.println(jsap.getHelp());
		//System.out.println(booleanRules());
		System.exit(2);
	}

	/**
	 * Could be usefull to inform the user of what is "false" and what is "true".
	 * @return rules
	 */
	private static String booleanRules() {
		return " \nThe following arguments are interpreted as TRUE: 1 t true y yes (case-insensitive)\nThe following arguments are interpreted as FALSE: 0 f false n no (case-insensitive)\n\n";
	}
}