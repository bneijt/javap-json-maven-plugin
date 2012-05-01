package nl.bneijt.javapjson;

import org.junit.Test;


public class JavapParserTest {
    public static final String NORMAL_JAVAP_OUTPUT = "Compiled from \"JavapJsonMojo.java\"\n" +
                    "public class nl.bneijt.javapjson.JavapJsonMojo extends org.apache.maven.plugin.AbstractMojo{\n" +
                    "public nl.bneijt.javapjson.JavapJsonMojo();\n" +
                    "  LineNumberTable: \n" +
                    "   line 41: 0\n" +
                    "\n" +
                    "  LocalVariableTable: \n" +
                    "   Start  Length  Slot  Name   Signature\n" +
                    "   0      5      0    this       Lnl/bneijt/javapjson/JavapJsonMojo;\n" +
                    "\n" +
                    "\n" +
                    "public void execute()   throws org.apache.maven.plugin.MojoExecutionException;\n" +
                    "  LineNumberTable: \n" +
                    "   line 92: 0\n" +
                    "\n" +
                    "  LocalVariableTable: \n" +
                    "   Start  Length  Slot  Name   Signature\n" +
                    "   0      10      0    this       Lnl/bneijt/javapjson/JavapJsonMojo;\n" +
                    "\n" +
                    "\n" +
                    "}\n" +
                    "\n";
    @Test
    public void shouldParseValidOutput() {
        JavapParser javapParser = new JavapParser();
        JavapLOutput parseL = javapParser.parseL(NORMAL_JAVAP_OUTPUT);
    }
}
