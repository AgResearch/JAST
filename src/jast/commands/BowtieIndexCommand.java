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

public class BowtieIndexCommand  extends Command  {

	public BowtieIndexCommand (Path config, Path ref,String[] arrayOfForbbidenOptions){
		super(config,arrayOfForbbidenOptions);
		String outputName = ref.toString()+"_BowtieIndex";
		command="bowtie2-build";
		totalCommand.add(ref.toString());
		totalCommand.add(outputName);
		outputFile=Paths.get(outputName);
	}

}
