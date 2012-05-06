package nl.bneijt.javapjson;

import static org.junit.Assert.assertTrue;
import static org.parboiled.support.ParseTreeUtils.printNodeTree;

import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;
public class JavapLParserTest {
    @Test
    public void shouldParseNormalInput() {
        JavapLParser parser = Parboiled.createParser(JavapLParser.class);
        ParsingResult<?> parsingResult = new BasicParseRunner(parser.JavapLOutput()).run(JavapParserTest.NORMAL_JAVAP_OUTPUT);
        printNodeTree(parsingResult);
        assertTrue(parsingResult.matched);
    }
}
