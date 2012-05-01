package nl.bneijt.javapjson;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

public class JavapParser {

    public static JavapLOutput parseL(String input) {

        JavapLParser parser = Parboiled.createParser(JavapLParser.class);
        ParsingResult<?> result = new BasicParseRunner(parser.JavapLOutput()).run(input);
        System.out.println(result.parseTreeRoot);


        return new JavapLOutput();

    }

}
