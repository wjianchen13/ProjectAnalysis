package com.cold.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileUtil {

	/** 
	 * DeCompress the ZIP to the parentPath 把zip文件解压到某个路径下
	 * @param zipFileString  name of ZIP 
	 * @param outPathString   parentPath to be unZIP
	 * @throws IOException 
	 * @throws Exception 
	 */  
	public static void UnZipFolder (final String zipFileString, final String outPathString) throws IOException{  
		ZipInputStream inZip = null;
			inZip = new ZipInputStream(new FileInputStream(zipFileString));
			ZipEntry zipEntry;  
			String szName = "";  
			while ((zipEntry = inZip.getNextEntry()) != null) {  
				szName = zipEntry.getName();  
				if (zipEntry.isDirectory()) {  
					// get the folder name of the widget  
					szName = szName.substring(0, szName.length() - 1);  
					File folder = new File(outPathString + File.separator + szName);  
					folder.mkdirs();  
				} else {  
					File file = new File(outPathString + File.separator + szName);  
					file.createNewFile();  
					// get the output stream of the file  
					FileOutputStream out = new FileOutputStream(file);  
					int len;  
					byte[] buffer = new byte[1024];  
					// read (len) bytes into buffer  
					while ((len = inZip.read(buffer)) != -1) {  
						// write (len) byte from buffer at the position 0  
						out.write(buffer, 0, len);  
						out.flush();  
					}  
					out.close();  
				}  
			} 
			try {
				if(inZip != null) inZip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  

	}

	/** 
	 * Compress file and folder
	 * @param srcFileString   file or folder to be Compress
	 * @param zipFileString   the parentPath name of result ZIP
	 * @throws Exception 
	 */  
	public static void ZipFolder(String srcFileString, String zipFileString)throws Exception {  
		//create ZIP 
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));  
		//create the file 
		File file = new File(srcFileString);  
		//compress
		ZipFiles(file.getParent()+File.separator, file.getName(), outZip);  
		//finish and close
		outZip.finish();  
		outZip.close();  
	}

	/** 
	 * compress files
	 * @param folderString 
	 * @param fileString 
	 * @param zipOutputSteam 
	 * @throws Exception 
	 */  
	private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam)throws Exception{  
		if(zipOutputSteam == null)  
			return;  
		File file = new File(folderString+fileString);  
		if (file.isFile()) {  
			ZipEntry zipEntry =  new ZipEntry(fileString);  
			FileInputStream inputStream = new FileInputStream(file);  
			zipOutputSteam.putNextEntry(zipEntry);  
			int len;  
			byte[] buffer = new byte[4096];   
			while((len=inputStream.read(buffer)) != -1)  
			{  
				zipOutputSteam.write(buffer, 0, len);  
			}  
			zipOutputSteam.closeEntry();  
		}  
		else {  
			//folder
			String fileList[] = file.list();  
			//no child file and compress  
			if (fileList.length <= 0) {  
				ZipEntry zipEntry =  new ZipEntry(fileString+File.separator);  
				zipOutputSteam.putNextEntry(zipEntry);  
				zipOutputSteam.closeEntry();                  
			}  
			//child files and recursion  
			for (int i = 0; i < fileList.length; i++) {  
				ZipFiles(folderString, fileString+ File.separator+fileList[i], zipOutputSteam);
			}//end of for  
		}    
	}

	/** 
	 * return the InputStream of file in the ZIP
	 * @param zipFileString  name of ZIP 
	 * @param fileString     name of file in the ZIP 
	 * @return InputStream 
	 * @throws Exception 
	 */  
	public static InputStream UpZip(String zipFileString, String fileString)throws Exception {  
		ZipFile zipFile = new ZipFile(zipFileString);  
		ZipEntry zipEntry = zipFile.getEntry(fileString);  
		return zipFile.getInputStream(zipEntry);  
	}  

	/** 
	 * return files list(file and folder) in the ZIP
	 * @param zipFileString     ZIP name
	 * @param bContainFolder    contain folder or not
	 * @param bContainFile      contain file or not
	 * @return 
	 * @throws Exception 
	 */  
	public static List<File> GetFileList(String zipFileString, boolean bContainFolder, boolean bContainFile)throws Exception {  
		List<File> fileList = new ArrayList<File>();  
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));  
		ZipEntry zipEntry;  
		String szName = "";  
		while ((zipEntry = inZip.getNextEntry()) != null) {  
			szName = zipEntry.getName();  
			if (zipEntry.isDirectory()) {  
				// get the folder name of the widget  
				szName = szName.substring(0, szName.length() - 1);  
				File folder = new File(szName);  
				if (bContainFolder) {  
					fileList.add(folder);  
				}  

			} else {  
				File file = new File(szName);  
				if (bContainFile) {  
					fileList.add(file);  
				}  
			}  
		}
		inZip.close();  
		return fileList;  
	}  
}
