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

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;

public class Pipeline {
	
	/**
	 * Forbidden options for SSPACE command
	 */
	public static String[] forbiddenSSPACE={"-l","-s","-b"};
	/**
	 * Forbidden options for Colombus command
	 */
	public static String[] forbiddenColombus={"-f","-p"};
	/**
	 * Forbidden options for Flexbar command
	 */
	public static String[] forbiddenFlexbar={"-t","-r","-p"};
	/**
	 * Forbidden options for A5 command
	 */
	public static String[] forbiddenA5=null;
	/**
	 * Forbidden options for Bowtie build command
	 */
	public static String[] forbiddenBowtieIndex=null;
	/**
	 * Forbidden options for Bowtie-x command
	 */
	public static String[] forbiddenBowtieMap={"-x","-q","-1","-2","-S"};
	/**
	 * The SSPACE's file created will finish by this extension
	 */
	public static final String libSSPACEext="_JAST";
	/**
	 * Class for command lines arguments : http://www.martiansoftware.com/jsap/
	 * @see com.martiansoftware.jsap.JSAP
	 */
	protected JSAP jsap;
	/**
	 * Command line arguments
	 */
	protected String[] args;
	/**
	 * Flebar command
	 */
	protected FlexbarCommand flexbar;
	/**
	 * A5 command
	 */
	protected A5Command a5;
	/**
	 * Bowtie command (index)
	 */
	protected BowtieIndexCommand bowtieIndex;
	/**
	 * Bowtie command (mapping)
	 */
	protected BowtieMapCommand bowtieMap;
	/**
	 * Colombus command
	 */
	protected ColombusCommand colombus;
	/**
	 * SSPACE command
	 */
	protected SSPACEcommand sspaceFinal;
	/**
	 * Class to launch the JAST pipeline
	 * @param jsap
	 * @param args
	 */
	public Pipeline(JSAP jsap,String[] args){
		this.jsap=jsap;
		this.args=args;
	}
	/**
	 * Launch the pipeline
	 */
	public void runPipeline(){
		JSAPResult config = jsap.parse(args);
		if  (config.getFile("flexbar config file") == null ){
			flexbar = new FlexbarCommand(null,config.getFile("First input reads").toPath(),config.getFile("Second input reads").toPath(),forbiddenFlexbar);
		}
		else {
			flexbar = new FlexbarCommand(config.getFile("flexbar config file").toPath(),config.getFile("First input reads").toPath(),config.getFile("Second input reads").toPath(),forbiddenFlexbar);
		}
		flexbar.exec();
		a5 = new A5Command(null,flexbar.getOutputFile(),config.getString("Output file"),forbiddenA5);
		a5.exec();
		//We must create a new file for SSPACE
		File NewSSPACElib = new File("");
		try {
			NewSSPACElib=computeSSPACELibrairy(config.getFile("SSPACE library").toPath(), flexbar.getOutputFile());
		} catch (IOException e) {
			System.err.println("[JAST_Error] I/O excpetion with file "+config.getFile("SSPACE library").getPath()+" and/or "+flexbar.getOutputFile());
			e.printStackTrace();
			System.err.println("[JAST_Error] System will exit.");
			System.exit(1);
		}
		if (config.getFile("Bowtie config Build")==null){
			bowtieIndex=new BowtieIndexCommand(null, config.getFile("Reference file").toPath(),forbiddenBowtieIndex);

		}
		else {
			bowtieIndex=new BowtieIndexCommand(config.getFile("Bowtie config Build").toPath(), config.getFile("Reference file").toPath(),forbiddenBowtieIndex);
		}
		bowtieIndex.exec();
		if (config.getFile("Bowtie config Map")==null){
			bowtieMap=new BowtieMapCommand(null, config.getFile("Reference file").toPath(), bowtieIndex.getOutputFile() , flexbar.getOutputFile(),forbiddenBowtieMap);
		}
		else {
			bowtieMap=new BowtieMapCommand(config.getFile("Bowtie config Map").toPath(), config.getFile("Reference file").toPath(), bowtieIndex.getOutputFile() , flexbar.getOutputFile(),forbiddenBowtieMap);
		}
		bowtieMap.exec();
		colombus = new ColombusCommand(config.getFile("Colombus config file").toPath(), a5.getOutputFile(), config.getFile("Reference file").toPath(), bowtieMap.getOutputFile(), flexbar.getOutputFile(),config.getString("Output file"),forbiddenColombus);

		colombus.exec();
		File[] files=new File(".").listFiles(new Filter(colombus.getOutputFile().toString()));
		File contigsAfterColombus=new File("");
		if (files.length!=1){
			System.err.println("[JAST_Error] Only one directory should match with this pattern : "+colombus.getOutputFile()+" (instead of "+files.length+"))\nIf directories exist beacause of early use, please either change the output name or delete the directory.\nSystem will exit");
			System.exit(1);
		}
		else {
			contigsAfterColombus = new File(files[0]+"/contigs.fa");
		}
		if ( config.getFile("SSPACE config file")==null){
			sspaceFinal = new SSPACEcommand(null, contigsAfterColombus.toPath(),config.getString("Output file"),NewSSPACElib.toPath(),forbiddenSSPACE);
		}
		else {
			sspaceFinal = new SSPACEcommand(config.getFile("SSPACE config file").toPath(), contigsAfterColombus.toPath(),config.getString("Output file"),NewSSPACElib.toPath(),forbiddenSSPACE);
		}
		sspaceFinal.exec();
		System.out.println("[JAST] Finsih ! output file : "+sspaceFinal.getOutputFile());

	}

	private File computeSSPACELibrairy(Path library,Path flexbaRoot) throws IOException {
		BufferedReader br =  Files.newBufferedReader(library,StandardCharsets.UTF_8);
		File newLib = new File(library+libSSPACEext);
		BufferedWriter writer = Files.newBufferedWriter(newLib.toPath(), StandardCharsets.UTF_8);
		String linetemp;
		boolean first=true;
		while ((linetemp=br.readLine())!=null){
			if (first){ // We only change the first line
				String newLine = "JASTlib "+flexbaRoot+"_1.fastq " +flexbaRoot+"_2.fastq "+linetemp;
				writer.write(newLine);
				first=false;
			}
			else {
				writer.write("\n"+linetemp);
			}
		}
		br.close();
		writer.close();
		return newLib;
	}
}