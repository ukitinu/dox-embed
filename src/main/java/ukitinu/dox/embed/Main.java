package ukitinu.dox.embed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xwpf.usermodel.*;
import ukitinu.dox.embed.docxml.DocXml;
import ukitinu.dox.embed.docxml.DocXmlBuilder;
import ukitinu.dox.embed.utils.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public final class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    private static final String AR_DIR = "_ar";
    private static final String[] DIRS = {AR_DIR};

    private static final String LOG_NO_NAME = "Missing filepath argument";
    private static final String DOCX_EXT = ".docx";
    private static final String LOG_NO_DOCX = "Filepath must be complete, missing .docx ending";
    private static final String LOG_NO_FILE = "No file found in {}";


    private Main() {
    }

    public static void main(String[] args) throws IOException, OpenXML4JException {
        if (canProceed(args)) {
            prepare();
            parse(args[0]);
        }
    }

    private static boolean canProceed(String[] args) {
        if (args.length == 0) {
            LOG.fatal(LOG_NO_NAME);
            return false;
        }
        if (!args[0].endsWith(DOCX_EXT)) {
            LOG.fatal(LOG_NO_DOCX);
            return false;
        }
        if (!Files.exists(Path.of(args[0]))) {
            LOG.fatal(LOG_NO_FILE, args[0]);
            return false;
        }
        return true;
    }

    private static void prepare() throws IOException {
        for (String dir : DIRS) {
            Utils.checkDir(dir);
        }
    }

    private static void parse(String filepath) throws IOException, OpenXML4JException {
        XWPFDocument doc = readDoc(filepath);
        DocXml docXml = DocXmlBuilder.build(filepath, AR_DIR, Path.of(filepath).getFileName().toString());
//        List<String> embeddingNames = Embeddings.getEmbeddingNames(doc);

        saveExtractions(doc, docXml);
        compareParagraphs(doc, docXml);
        compareValues(doc, docXml);
    }

    private static XWPFDocument readDoc(String filepath) throws IOException {
        File file = new File(filepath);
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XWPFDocument(fis);
        }
    }

    private static void saveExtractions(XWPFDocument doc, DocXml docXml) throws FileNotFoundException, OpenXML4JException {
        Utils.saveListToFile("poi_paragraphs", doc.getParagraphs()
                .stream()
                .map(XWPFParagraph::getText)
                .collect(Collectors.toList())
        );
        Utils.saveListToFile("dox_paragraphs", docXml.getTextLines());

        Utils.saveListToFile("dox_embeddings", Embeddings.buildEmbeddingList(doc, docXml));
    }

    private static void compareValues(XWPFDocument doc, DocXml docXml) {
        LOG.info("poi paragraphs: {}", doc.getParagraphs().size());
        LOG.info("dox paragraphs: {}", docXml.getParagraphs().size());
        LOG.info("poi tables: {}", doc.getTables().size());
        LOG.info("dox tables: {}", docXml.getTables().size());
    }

    private static void compareParagraphs(XWPFDocument doc, DocXml docXml) {
        List<String> poiLines = doc.getParagraphs().stream().map(XWPFParagraph::getText).collect(Collectors.toList());
        List<String> doxLines = docXml.getTextLines();

        int equalCount = 0;
        int squashCount = 0;
        int diffCount = 0;

        for (int i = 0; i < Math.min(poiLines.size(), doxLines.size()); i++) {
            String poi = poiLines.get(i);
            String dox = doxLines.get(i);
            String poiSquashed = poi.replaceAll("\\s+", "");
            String doxSquashed = dox.replaceAll("\\s+", "");

            if(poi.equals(dox)) {
                equalCount++;
            } else if (poiSquashed.equals(doxSquashed)) {
                squashCount++;
            } else {
                diffCount++;
            }
        }

        LOG.info("Equal paragraphs: {}", equalCount);
        LOG.info("Equal (after removing blank spaces) paragraphs: {}", squashCount);
        LOG.info("Different paragraphs: {}", diffCount);
    }
}

