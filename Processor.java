import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import mp3.Main;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;



public class Processor {

	private static void convertFileOnWinToMP3(File actualFile, File inputFile){
		String mp3Filename = null;
	    try
	    {
	    	String filename = FilenameUtils.removeExtension(actualFile.getAbsolutePath());
	    	mp3Filename = filename + ".mp3";
	    	
	    	String[] mp3Args = {"--preset","standard",
	    			"-q","0",
	    			"-m","s",
	    			inputFile.toString(),
	    			mp3Filename};
	    	Main m = new mp3.Main();
	        m.run(mp3Args);
	        if(inputFile.exists()){
	        	try {
		        	FileUtils.deleteDirectory(new File(inputFile.getParent()));
	        	} catch(Exception e) {
	        		//LOG.info(e.getMessage());
	        	}
	        }
	    }
	    catch(Exception e)
	    {
	    	System.err.println("Unable to convert audio to mp3 -> " + e.getMessage());
	    }  
	}
	
	public static void main(String... strings) {
		try{
			
			//expected input is file path
			for(String filepath:strings) {
				
				File file = new File(new File(filepath).getCanonicalPath());
				if(null!=file && file.exists() && file.isFile() && file.canRead()) {
					
					String tempDirStr = "PCMWAV";
					if(file.getParent() != null){
						tempDirStr = file.getParent()+File.separator+"PCMWAV";
					}
					
					File tempDir = new File(tempDirStr);
					
					if(!tempDir.exists()){
						tempDir.mkdirs();
					}
					
					File outFile = new File(tempDir,file.getName());
					AudioInputStream inFileAIS = 
					        AudioSystem.getAudioInputStream(file);
					AudioInputStream targetAudioInputStream = 
					        AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, inFileAIS);
					  AudioSystem.write(targetAudioInputStream,
					           AudioFileFormat.Type.WAVE, outFile);
					convertFileOnWinToMP3(file,outFile);
					
					
				}else{
					System.err.println(String.format("GIVEN PATH -> %s IS INVALID.",file));
				}
			}
		}
		catch(Exception ee) {
			System.err.println("Exception Occured -> " + ee.getMessage());
		}

		
	}
	
}
