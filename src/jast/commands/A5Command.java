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

import jast.Command;
import java.nio.file.Path;
import java.nio.file.Paths;

public class A5Command extends Command{
	public A5Command(Path config,Path input,String outputroot,String [] arrayOfForbbidenOptions) {
		super(config,arrayOfForbbidenOptions);
		command="a5_pipeline.pl";
		totalCommand.add(input.toString()+"_1.fastq");
		totalCommand.add(input.toString()+"_2.fastq");
		totalCommand.add(outputroot+"_scaffoldsAfterA5");
		outputFile=Paths.get( outputroot+"_scaffoldsAfterA5.final.scaffolds.fasta");
	}
}
