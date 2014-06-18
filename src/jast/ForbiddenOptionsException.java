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

package jast;

/**
 * Exception throwed when user specify a forbidden option in a config file.
 * @author Clément DELESTRE
 * @version 1.0
 * @since 1.0
 */
public class ForbiddenOptionsException extends Exception{
	public ForbiddenOptionsException(String option,String command){
		super("The following option : "+option+" is forbidden for the command "+command);
	}
}
