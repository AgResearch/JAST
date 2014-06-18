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

import java.nio.file.Path;
import java.nio.file.Paths;

import jast.Command;

public class FlexbarCommand extends Command {

	public FlexbarCommand(Path config, Path readsOne, Path readsTwo,String [] arrayOfForbbidenOptions) {
		super(config,arrayOfForbbidenOptions);
		command="flexbar";
		String outputFileName = readsOne.toString().replace(".fastq", "");
		outputFileName = outputFileName.replace(".fq","");
		outputFileName = outputFileName.replace("_1","");
		outputFileName+="_afterFlexbar"; // results will be outputFileName_1.fastq and outputFileName_2.fastq
		outputFile = Paths.get(outputFileName);
		totalCommand.add("-t");
		totalCommand.add(outputFileName);
		totalCommand.add("-r");
		totalCommand.add(readsOne.toString());
		totalCommand.add("-p");
		totalCommand.add(readsTwo.toString());
	}
}
