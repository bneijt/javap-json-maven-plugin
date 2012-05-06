package nl.bneijt.javapjson;

import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
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
            ZeroOrMore(MemberDecl()),
            Modifier(), QualifiedIdentifier(), "();", NEWLINE
            ,LineNumberTable()
            ,LocalVariableTable()
            );
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
                Modifier(),
                CLASS,
                QualifiedIdentifier(),
                Optional(TypeParameters()),
                Optional(EXTENDS, QualifiedIdentifier()),
                Optional(IMPLEMENTS, ClassTypeList())
        );
    }



}
