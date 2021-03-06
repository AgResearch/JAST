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
package jast.commands;

import jast.Command;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BowtieMapCommand extends Command {

	public BowtieMapCommand (Path config, Path genome,Path indexFile,Path readOne,String [] arrayOfForbbidenOptions){
		super(config,arrayOfForbbidenOptions);
		command="bowtie2";
		String outputName = genome.toString().replaceAll("\\.fasta", "")+"_VSreads.sam";
		totalCommand.add("-x");
		totalCommand.add(indexFile.toString());
		totalCommand.add("-q");
		totalCommand.add("-1");
		totalCommand.add(readOne.toString()+"_1.fastq");
		totalCommand.add("-2");
		totalCommand.add(readOne.toString()+"_2.fastq");
		totalCommand.add("-S");
		totalCommand.add(outputName);
		outputFile=Paths.get(outputName);
	}

}
