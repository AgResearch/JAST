/**
 *  JAST (Java Assembling and Scaffolding Tool) is a program do assembling and scaffolding from paired-end Illumina files.
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

import java.nio.file.Path;
import java.nio.file.Paths;

import jast.Command;

public class ColombusCommand  extends Command {
	public ColombusCommand(Path config, Path contigs,Path reference,Path sam,Path flexbarOutput,String output,String [] arrayOfForbbidenOptions) {
		super(config,arrayOfForbbidenOptions);
		command="VelvetOptimiser.pl";
		totalCommand.add("-f");
		totalCommand.add("-reference "+reference+" -shortPaired -sam "+sam+" -separate -fastq "+flexbarOutput+"_1.fastq "+flexbarOutput+"_2.fastq -long -fasta "+contigs);
		totalCommand.add("-p");
		totalCommand.add("JASTColombusDir_"+output);
		outputFile=Paths.get( "JASTColombusDir_"+output+"_data_(\\d+)");
	}
}
