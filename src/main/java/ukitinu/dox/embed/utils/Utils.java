package ukitinu.dox.embed.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    private static final int BUFFER_SIZE = 1024;

    public static void checkDir(String dir) throws IOException {
        if (Files.notExists(Paths.get(dir))) {
            Files.createDirectories(Paths.get(dir));
        }
    }

    public static void saveFile(String fileName, String text) throws FileNotFoundException {
        File save = new File(fileName);
        try (PrintWriter pw = new PrintWriter(save)) {
            pw.println(text);
        }
    }

    public static void saveListToFile(String fileName, Iterable<?> list) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        for (Object o : list) {
            sb.append(o.toString()).append(System.lineSeparator());
        }
        saveFile(fileName, sb.toString());
    }

    //region zip_unzip
    public static String convertToZip(String originalFile, String zipDir, String zipName) throws IOException {
        File docFile = new File(originalFile);
        File zipFile = new File(zipDir + File.separator + zipName + ".zip");
        FileUtils.copyFile(docFile, zipFile);
        return zipFile.getPath();
    }

    public static void extractZip(String zipFile, String unzipDir) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        try (var zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newValidFile(new File(unzipDir), zipEntry);
                makeDirs(zipEntry, newFile);
                try (var fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

    private static File newValidFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry outside target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void makeDirs(ZipEntry zipEntry, File newFile) throws IOException {
        if (zipEntry.isDirectory()) {
            newFile.mkdirs();
        } else {
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
    }
    //endregion

    public static int countSubstring(String string, String sub) {
        int i = 0;
        int count = 0;
        while (i != -1) {
            i = string.indexOf(sub, i);
            if (i != -1) {
                count++;
                i += sub.length();
            }
        }
        return count;
    }
}
