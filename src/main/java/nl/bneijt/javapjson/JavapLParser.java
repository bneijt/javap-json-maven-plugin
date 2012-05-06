package nl.bneijt.javapjson;

import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.examples.java.JavaParser;

@SuppressWarnings({"InfiniteRecursion"})
@BuildParseTree
public class JavapLParser extends JavaParser  {

    final Rule NEWLINE = FirstOf("\r\n", '\r', '\n');

    public Rule JavapLOutput() {
        return Sequence(
                "Compiled from ", StringLiteral(), NEWLINE,
                JavapLClassDeclaration(), "{", NEWLINE,
                OneOrMore(JavapInterfaceDeclaration()));
    }

    public Rule JavapInterfaceDeclaration() {
        return Sequence(
            ZeroOrMore(Member()),
            ZeroOrMore(NEWLINE),
            Modifier(), JavapQualifiedIdentifier(), "();", NEWLINE
            ,LineNumberTable()
            ,LocalVariableTable()
            );
    }

    public Rule JavapQualifiedIdentifier() {
        return Sequence(JavapIdentifier(), ZeroOrMore(DOT, JavapIdentifier()));
    }

    @SuppressSubnodes
    @MemoMismatches
    public Rule JavapIdentifier() {
        return Sequence(TestNot(Keyword()), Letter(), ZeroOrMore(LetterOrDigit()), ZeroOrMore(MangelPattern()), Spacing());
    }

    public Rule MangelPattern() {
        return Sequence("$", IntegerLiteral());
    }

    public Rule Member() {
        return Sequence(Modifier(), JavapQualifiedIdentifier(), VariableDeclarator(), ";", NEWLINE);
    }

    public Rule LocalVariableTable() {
        return Sequence("  LocalVariableTable: ", NEWLINE,
                "   Start  Length  Slot  Name   Signature", NEWLINE
                ,OneOrMore(LocalVariableTableRow()), NEWLINE);
    }
    public Rule LocalVariableTableRow() {
        return Sequence(WhiteSpace(), IntegerLiteral(), WhiteSpace(), IntegerLiteral(), WhiteSpace(), IntegerLiteral(), WhiteSpace(), "this", WhiteSpace(), LinkReferencePath(), ";", NEWLINE);
    }

    public Rule LinkReferencePath() {
        return Sequence("L", OneOrMore(NoneOf(";\n")));
    }
    public Rule WhiteSpace() {
        return OneOrMore(AnyOf(" \n\r\t"));
    }

    public Rule LineNumberTable() {
        return Sequence("  LineNumberTable: ", NEWLINE,
            OneOrMore(LineNumberTableRow()), NEWLINE);
    }
    public Rule LineNumberTableRow() {
        return Sequence("   line ", IntegerLiteral(), ": ", IntegerLiteral(), NEWLINE);
    }

    public Rule JavapLClassDeclaration() {
        return Sequence(
                ZeroOrMore(Modifier()),
                CLASS,
                JavapQualifiedIdentifier(),
                Optional(TypeParameters()),
                Optional(EXTENDS, JavapQualifiedIdentifier()),
                Optional(IMPLEMENTS, QualifiedTypeList())
        );
    }

    public Rule QualifiedTypeList() {
        return Sequence(JavapQualifiedIdentifier(), ZeroOrMore(COMMA, JavapQualifiedIdentifier()));
    }


}
