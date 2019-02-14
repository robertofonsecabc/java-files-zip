package br.com.rhfactor.microservices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JobZipDownloadApp {
	
	public static class FileContent{
		public FileContent(){}
		
		public FileContent(String name, String content){
			this.name = name;
			this.content = content;
		}
		
		private String name;
		private String content;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		
		JsonReader reader = new JsonReader(new FileReader("input.json"));
		
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<FileContent>>(){}.getType();
		List<FileContent> filelist = gson.fromJson(reader, listType );
		
		//List<FileContent> filelist = gson.fromJson("[{'name' : 'nome', 'content' : 'contentudo' }]", listType );
		//List<FileContent> filelist = new ArrayList<FileContent>();
		//filelist.add( new FileContent("nome","conteudo") );
		
		List<String> files = new ArrayList<String>();
		
		//Receber uma lista com nome e conteudo
		filelist.forEach( file -> {
			createFile( file.getName() + ".xml", file.getContent() );
			files.add( file.getName() + ".xml" );
		});
		
		//Criar arquivo zip e pegar todos os arquivos gerados e mandar pro ZIP
		FileOutputStream fos = new FileOutputStream("files.zip");
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (String srcFile : files) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
 
            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
		
        for (String srcFile : files) {
        	File fileToZip = new File(srcFile);
        	fileToZip.delete();
        }
		
	}
	
	public static void createFile(String name, String content) {
		
		BufferedWriter writer = null;
        try {
            File logFile = new File(name);
            System.out.println(logFile.getCanonicalPath());
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
		
	}

}
