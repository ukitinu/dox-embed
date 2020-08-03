package ukitinu.dox.embed.docxml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlElementTest {

    private static XmlElement ELEMENT_01;
    private static XmlElement ELEMENT_02;

    @BeforeAll
    static void initAll() {
        int closingIndex = XmlExplorer.findOuterCharIndex(W_OBJ_01, '>', 0) + 1;
        ELEMENT_01 = new XmlElement(W_OBJ_01, new XmlTag("<w:object>"), closingIndex, 0, W_OBJ_01.length());
        closingIndex = XmlExplorer.findOuterCharIndex(W_OBJ_02, '>', 0) + 1;
        ELEMENT_02 = new XmlElement(W_OBJ_02, new XmlTag("<w:rPr>"), closingIndex, 0, W_OBJ_02.length());
    }

    @Test
    void getText() {
        assertEquals("", ELEMENT_01.getText());
        assertEquals("This is a unit test", ELEMENT_02.getText());
    }

    @Test
    void getInner() {
        assertEquals(W_OBJ_01_INNER, ELEMENT_01.getInner());
        assertEquals(W_OBJ_02_INNER, ELEMENT_02.getInner());
    }

    @Test
    void getEmbeddings() {
        assertEquals(1, ELEMENT_01.getEmbeddings().size());
        assertEquals(0, ELEMENT_02.getEmbeddings().size());
    }

    @Test
    void hasEmbeddings() {
        assertTrue(ELEMENT_01.hasEmbeddings());
        assertFalse(ELEMENT_02.hasEmbeddings());
    }

    @Test
    void getInnerElement() {
        XmlElement oleObj = ELEMENT_01.getInnerElement(new XmlTag("<o:OLEObject>"));
        String oleObjString = "<o:OLEObject Type=\"Embed\" ProgID=\"Excel.Sheet.12\" ShapeID=\"_x0000_s4118078\" DrawAspect=\"Content\" ObjectID=\"_1655617047\" r:id=\"rId25\"/>";
        assertEquals(oleObjString, oleObj.getFullContent());

        XmlElement nullObj = ELEMENT_02.getInnerElement(new XmlTag("<o:fakeTag>"));
        assertNull(nullObj);
    }

    @Test
    void getAttrValue() {
        XmlElement oleObj = ELEMENT_01.getInnerElement(new XmlTag("<o:OLEObject>"));
        assertEquals("", oleObj.getAttrValue("FakeAttribute"));
        assertEquals("Excel.Sheet.12", oleObj.getAttrValue("ProgID"));

        XmlElement testObj = ELEMENT_02.getInnerElement(new XmlTag("<w:testTag>"));
        assertEquals("", testObj.getAttrValue("FakeInside"));
        assertEquals("ArFakeInside='fake value'ial", testObj.getAttrValue("w:ascii"));
    }

    private static final String W_OBJ_01 = "<w:object w:dxaOrig=\"1440\" w:dyaOrig=\"1440\"><v:shapetype id=\"_x0000_t75\" coordsize=\"21600,21600\" o:spt=\"75\" o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\"><v:stroke joinstyle=\"miter\"/><v:formulas><v:f eqn=\"if lineDrawn pixelLineWidth 0\"/><v:f eqn=\"sum @0 1 0\"/><v:f eqn=\"sum 0 0 @1\"/><v:f eqn=\"prod @2 1 2\"/><v:f eqn=\"prod @3 21600 pixelWidth\"/><v:f eqn=\"prod @3 21600 pixelHeight\"/><v:f eqn=\"sum @0 0 1\"/><v:f eqn=\"prod @6 1 2\"/><v:f eqn=\"prod @7 21600 pixelWidth\"/><v:f eqn=\"sum @8 21600 0\"/><v:f eqn=\"prod @7 21600 pixelHeight\"/><v:f eqn=\"sum @10 21600 0\"/></v:formulas><v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/><o:lock v:ext=\"edit\" aspectratio=\"t\"/></v:shapetype><v:shape id=\"_x0000_s4118078\" type=\"#_x0000_t75\" style=\"position:absolute;margin-left:36pt;margin-top:14.35pt;width:355.65pt;height:437.95pt;z-index:251701248\" stroked=\"t\" strokeweight=\"1.5pt\"><v:imagedata r:id=\"rId24\" o:title=\"\"/></v:shape><o:OLEObject Type=\"Embed\" ProgID=\"Excel.Sheet.12\" ShapeID=\"_x0000_s4118078\" DrawAspect=\"Content\" ObjectID=\"_1655617047\" r:id=\"rId25\"/></w:object>";
    private static final String W_OBJ_01_INNER = "<v:shapetype id=\"_x0000_t75\" coordsize=\"21600,21600\" o:spt=\"75\" o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\"><v:stroke joinstyle=\"miter\"/><v:formulas><v:f eqn=\"if lineDrawn pixelLineWidth 0\"/><v:f eqn=\"sum @0 1 0\"/><v:f eqn=\"sum 0 0 @1\"/><v:f eqn=\"prod @2 1 2\"/><v:f eqn=\"prod @3 21600 pixelWidth\"/><v:f eqn=\"prod @3 21600 pixelHeight\"/><v:f eqn=\"sum @0 0 1\"/><v:f eqn=\"prod @6 1 2\"/><v:f eqn=\"prod @7 21600 pixelWidth\"/><v:f eqn=\"sum @8 21600 0\"/><v:f eqn=\"prod @7 21600 pixelHeight\"/><v:f eqn=\"sum @10 21600 0\"/></v:formulas><v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/><o:lock v:ext=\"edit\" aspectratio=\"t\"/></v:shapetype><v:shape id=\"_x0000_s4118078\" type=\"#_x0000_t75\" style=\"position:absolute;margin-left:36pt;margin-top:14.35pt;width:355.65pt;height:437.95pt;z-index:251701248\" stroked=\"t\" strokeweight=\"1.5pt\"><v:imagedata r:id=\"rId24\" o:title=\"\"/></v:shape><o:OLEObject Type=\"Embed\" ProgID=\"Excel.Sheet.12\" ShapeID=\"_x0000_s4118078\" DrawAspect=\"Content\" ObjectID=\"_1655617047\" r:id=\"rId25\"/>";
    private static final String W_OBJ_02 = "<w:rPr><w:testTag w:ascii=\"ArFakeInside='fake value'ial\" w:eastAsia=\"Calibri\" w:hAnsi=\"Arial\"/><w:b/><w:color w:val=\"000000\" w:themeColor=\"text1\"/><w:t>This is a unit test</w:t></w:rPr>";
    private static final String W_OBJ_02_INNER = "<w:testTag w:ascii=\"ArFakeInside='fake value'ial\" w:eastAsia=\"Calibri\" w:hAnsi=\"Arial\"/><w:b/><w:color w:val=\"000000\" w:themeColor=\"text1\"/><w:t>This is a unit test</w:t>";
}