package br.com.jdorigao.apidfe;

import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@SpringBootTest
class ApiNFeApplicationTests {

    public static void main(String[] args) throws IOException {
        System.out.println(fileToByte("/mnt/ssd/Workspace/certificado.pfx"));
    }

    private static String fileToByte(String caminhoArquivo) throws IOException {
        byte[] fileContent = Files.readAllBytes(new File(caminhoArquivo).toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

}
