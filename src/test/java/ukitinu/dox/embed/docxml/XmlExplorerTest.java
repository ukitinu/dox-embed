package ukitinu.dox.embed.docxml;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XmlExplorerTest {

    @Test
    void find_short() {
        List<XmlElement> list = XmlExplorer.findTag(SEC_01, "<w:p>");
        assertEquals(1, list.size());
        assertEquals(VAL_01, list.get(0).getFullContent());
    }

    @Test
    void find_long() {
        List<XmlElement> list = XmlExplorer.findTag(SEC_02, "<w:p>");
        assertEquals(3, list.size());
        assertEquals(SEC_02, list.get(0).getFullContent());
    }

    @Test
    void find_simple() {
        List<XmlElement> list = XmlExplorer.findTag(SEC_03, "<o:OLEObject>");
        assertEquals(1, list.size());
        assertEquals(VAL_03, list.get(0).getFullContent());
    }


    private static final String SEC_01 = "<v:textbox style=\"mso-next-textbox:#_x0000_s24513\"><w:txbxContent><w:p w:rsidR=\"003D4C6A\" w:rsidRDefault=\"003D4C6A\" w:rsidP=\"00520818\"/></w:txbxContent></v:textbox>";
    private static final String VAL_01 = "<w:p w:rsidR=\"003D4C6A\" w:rsidRDefault=\"003D4C6A\" w:rsidP=\"00520818\"/>";
    private static final String SEC_02 = "<w:p w:rsidR=\"00520818\" w:rsidRPr=\"002843FA\" w:rsidRDefault=\"00A86D8C\" w:rsidP=\"002158F5\"><w:pPr><w:spacing w:before=\"0\"/><w:ind w:left=\"1276\"/><w:rPr><w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\"/><w:b/><w:bCs/><w:color w:val=\"000000\" w:themeColor=\"text1\"/><w:sz w:val=\"22\"/><w:szCs w:val=\"22\"/><w:u w:val=\"single\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\"/><w:b/><w:color w:val=\"000000\" w:themeColor=\"text1\"/><w:sz w:val=\"22\"/><w:szCs w:val=\"22\"/><w:lang w:eastAsia=\"it-IT\"/></w:rPr><w:pict><v:group id=\"_x0000_s24515\" style=\"position:absolute;left:0;text-align:left;margin-left:-107.45pt;margin-top:131.6pt;width:18.25pt;height:16.9pt;z-index:251657216\" coordorigin=\"910,11845\" coordsize=\"365,338\"><v:oval id=\"_x0000_s24516\" style=\"position:absolute;left:910;top:11845;width:365;height:338\" fillcolor=\"#92d050\"/><v:rect id=\"_x0000_s24517\" style=\"position:absolute;left:1035;top:11880;width:105;height:288;visibility:visible\" filled=\"f\" stroked=\"f\"><o:lock v:ext=\"edit\" aspectratio=\"t\"/><v:textbox style=\"mso-next-textbox:#_x0000_s24517;mso-rotate-with-shape:t\" inset=\"0,0,0,0\"><w:txbxContent><w:p w:rsidR=\"003D4C6A\" w:rsidRDefault=\"003D4C6A\" w:rsidP=\"00520818\"><w:pPr><w:autoSpaceDE w:val=\"0\"/><w:autoSpaceDN w:val=\"0\"/><w:adjustRightInd w:val=\"0\"/><w:rPr><w:rFonts w:ascii=\"Calibri\" w:hAnsi=\"Calibri\"/><w:b/><w:bCs/><w:color w:val=\"FFFFFF\"/><w:lang w:val=\"en-US\"/></w:rPr></w:pPr><w:r w:rsidRPr=\"00910C3B\"><w:rPr><w:rFonts w:ascii=\"Calibri\" w:hAnsi=\"Calibri\"/><w:b/><w:bCs/><w:lang w:val=\"en-US\"/></w:rPr><w:t>1</w:t></w:r><w:r><w:rPr><w:rFonts w:ascii=\"Calibri\" w:hAnsi=\"Calibri\"/><w:b/><w:bCs/><w:color w:val=\"FFFFFF\"/><w:lang w:val=\"en-US\"/></w:rPr><w:t>4</w:t></w:r></w:p></w:txbxContent></v:textbox></v:rect></v:group></w:pict></w:r><w:r><w:rPr><w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\"/><w:b/><w:color w:val=\"000000\" w:themeColor=\"text1\"/><w:sz w:val=\"22\"/><w:szCs w:val=\"22\"/></w:rPr><w:pict><v:oval id=\"_x0000_s24514\" style=\"position:absolute;left:0;text-align:left;margin-left:-135.6pt;margin-top:218.25pt;width:18pt;height:16.3pt;z-index:251656192\" fillcolor=\"#9c0\"/></w:pict></w:r><w:r><w:rPr><w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\"/><w:b/><w:color w:val=\"000000\" w:themeColor=\"text1\"/><w:sz w:val=\"22\"/><w:szCs w:val=\"22\"/></w:rPr><w:pict><v:group id=\"_x0000_s24511\" style=\"position:absolute;left:0;text-align:left;margin-left:-126pt;margin-top:234.55pt;width:29.25pt;height:27.75pt;z-index:251655168\" coordorigin=\"1422,2699\" coordsize=\"585,555\"><v:oval id=\"_x0000_s24512\" style=\"position:absolute;left:1422;top:2699;width:540;height:540\" fillcolor=\"lime\"/><v:shape id=\"_x0000_s24513\" type=\"#_x0000_t202\" style=\"position:absolute;left:1467;top:2714;width:540;height:540\" filled=\"f\" stroked=\"f\"><v:textbox style=\"mso-next-textbox:#_x0000_s24513\"><w:txbxContent><w:p w:rsidR=\"003D4C6A\" w:rsidRDefault=\"003D4C6A\" w:rsidP=\"00520818\"/></w:txbxContent></v:textbox></v:shape></v:group></w:pict></w:r><w:r w:rsidR=\"00520818\" w:rsidRPr=\"002843FA\"><w:rPr><w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\"/><w:b/><w:bCs/><w:color w:val=\"000000\" w:themeColor=\"text1\"/><w:sz w:val=\"22\"/><w:szCs w:val=\"22\"/><w:u w:val=\"single\"/></w:rPr><w:t>Composizione ed articolazione delle forze</w:t></w:r></w:p>";
    private static final String SEC_03 = "<w:object w:dxaOrig=\"1440\" w:dyaOrig=\"1440\"><v:shapetype id=\"_x0000_t75\" coordsize=\"21600,21600\" o:spt=\"75\" o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\"><v:stroke joinstyle=\"miter\"/><v:formulas><v:f eqn=\"if lineDrawn pixelLineWidth 0\"/><v:f eqn=\"sum @0 1 0\"/><v:f eqn=\"sum 0 0 @1\"/><v:f eqn=\"prod @2 1 2\"/><v:f eqn=\"prod @3 21600 pixelWidth\"/><v:f eqn=\"prod @3 21600 pixelHeight\"/><v:f eqn=\"sum @0 0 1\"/><v:f eqn=\"prod @6 1 2\"/><v:f eqn=\"prod @7 21600 pixelWidth\"/><v:f eqn=\"sum @8 21600 0\"/><v:f eqn=\"prod @7 21600 pixelHeight\"/><v:f eqn=\"sum @10 21600 0\"/></v:formulas><v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/><o:lock v:ext=\"edit\" aspectratio=\"t\"/></v:shapetype><v:shape id=\"_x0000_s4118078\" type=\"#_x0000_t75\" style=\"position:absolute;margin-left:36pt;margin-top:14.35pt;width:355.65pt;height:437.95pt;z-index:251701248\" stroked=\"t\" strokeweight=\"1.5pt\"><v:imagedata r:id=\"rId24\" o:title=\"\"/></v:shape><o:OLEObject Type=\"Embed\" ProgID=\"Excel.Sheet.12\" ShapeID=\"_x0000_s4118078\" DrawAspect=\"Content\" ObjectID=\"_1655617047\" r:id=\"rId25\"/></w:object>";
    private static final String VAL_03 = "<o:OLEObject Type=\"Embed\" ProgID=\"Excel.Sheet.12\" ShapeID=\"_x0000_s4118078\" DrawAspect=\"Content\" ObjectID=\"_1655617047\" r:id=\"rId25\"/>";
}