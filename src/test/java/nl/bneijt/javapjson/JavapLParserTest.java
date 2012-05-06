package nl.bneijt.javapjson;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;

@RunWith(Parameterized.class)
public class JavapLParserTest {
    private File target;

    public JavapLParserTest(File target) {
       this.target = target;
    }

    @Parameters
    public static Collection<Object[]> data() {
        String outputDirectory = ClassLoader.getSystemClassLoader().getResource("output").getFile();
        Collection<File> outputFiles = FileUtils.listFiles(new File(outputDirectory), null, false);
        ArrayList<Object[]> parameters = new ArrayList<Object[]>();
        for (File file : outputFiles) {
            parameters.add(new Object[]{ file });
        }
        return parameters;
    }

    @Test
    public void shouldParseAllNormalInput() {
            String contents = org.parboiled.common.FileUtils.readAllText(target);
            assertNotNull("Should be able to load output file", contents);
            JavapLParser parser = Parboiled.createParser(JavapLParser.class);
            ParsingResult<?> parsingResult = new BasicParseRunner(parser.JavapLOutput()).run(contents);
            ParseTreeUtils.printNodeTree(parsingResult);
            assertTrue("Parser should match " + target.getName(), parsingResult.matched);
    }
}
