package com.example.linter;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CloseableLinter {

    private final JavaParser javaParser;
    private final List<LintIssue> issues;

    public CloseableLinter() {
        // Set up the symbol solver for type resolution
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        
        // Configure JavaParser with the symbol solver
        javaParser = new JavaParser();
        javaParser.getParserConfiguration().setSymbolResolver(symbolSolver);
        
        issues = new ArrayList<>();
    }

    public List<LintIssue> lintFile(File javaFile) throws FileNotFoundException {
        issues.clear();
        
        // Parse the Java file
        CompilationUnit cu = javaParser.parse(javaFile)
                .getResult()
                .orElseThrow(() -> new RuntimeException("Failed to parse " + javaFile.getName()));
        
        // Visit the compilation unit to find issues
        cu.accept(new CloseableResourceVisitor(), null);
        
        return issues;
    }

    private class CloseableResourceVisitor extends VoidVisitorAdapter<Void> {
        
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            
            // Skip if the method has no body
            if (!md.getBody().isPresent()) {
                return;
            }
            
            // Track all object creation expressions in the method body
            List<ObjectCreationExpr> allCreations = new ArrayList<>();
            md.getBody().get().accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(ObjectCreationExpr objectCreationExpr, Void arg) {
                    super.visit(objectCreationExpr, arg);
                    allCreations.add(objectCreationExpr);
                }
            }, null);
            
            // Track all try statements with resources in the method body
            List<TryStmt> tryStmts = new ArrayList<>();
            md.getBody().get().accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(TryStmt tryStmt, Void arg) {
                    super.visit(tryStmt, arg);
                    tryStmts.add(tryStmt);
                }
            }, null);
            
            // Check each object creation to see if it's a Closeable
            for (ObjectCreationExpr oce : allCreations) {
                try {
                    ResolvedReferenceTypeDeclaration typeDecl = oce.getType().resolve().asReferenceType().getTypeDeclaration().get();
                    
                    // Check if this type implements Closeable
                    if (isCloseable(typeDecl)) {
                        // Check if this creation is within a try-with-resources
                        if (!isInsideTryWithResources(oce, tryStmts)) {
                            issues.add(new LintIssue(
                                    oce.getBegin().get().line,
                                    "Closeable resource not managed with try-with-resources: " + oce.getType().getName()
                            ));
                        }
                    }
                } catch (Exception e) {
                    // Skip this object creation if we can't resolve its type
                    System.err.println("Failed to resolve type for: " + oce);
                }
            }
        }
        
        private boolean isCloseable(ResolvedReferenceTypeDeclaration typeDecl) {
        	ResolvedClassDeclaration classDcl;
        	if (typeDecl instanceof ResolvedClassDeclaration) {
        		classDcl = (ResolvedClassDeclaration) typeDecl;
        	} else {
        		return false;
        	}
        	
        	// Check if this type directly implements Closeable
            for (ResolvedReferenceType interfaceType : classDcl.getAllInterfaces()) {
                if (interfaceType.getQualifiedName().equals(Closeable.class.getName())) {
                    return true;
                }
            }
            
            // Check if any superclass implements Closeable
            if (classDcl.getSuperClass().isPresent()) {
                try {
                    return isCloseable(classDcl.getSuperClass().get().getTypeDeclaration().get());
                } catch (Exception e) {
                    return false;
                }
            }
            
            return false;
        }
        
        private boolean isInsideTryWithResources(ObjectCreationExpr oce, List<TryStmt> tryStmts) {
            for (TryStmt tryStmt : tryStmts) {
                // Check if this try statement has resources
                if (!tryStmt.getResources().isEmpty()) {
                    // Check if this object creation is one of the resources
                    for (Expression resource : tryStmt.getResources()) {
                        if (resource.isVariableDeclarationExpr()) {
                            VariableDeclarationExpr vde = resource.asVariableDeclarationExpr();
                            for (var variable : vde.getVariables()) {
                                if (variable.getInitializer().isPresent() && 
                                    variable.getInitializer().get().equals(oce)) {
                                    return true;
                                }
                            }
                        }
                    }
                    
                    // Check if this object creation is inside the try block
                    if (tryStmt.getTryBlock().containsWithinRange(oce)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class LintIssue {
        private final int lineNumber;
        private final String message;

        public LintIssue(int lineNumber, String message) {
            this.lineNumber = lineNumber;
            this.message = message;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Line " + lineNumber + ": " + message;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java CloseableLinter <path-to-java-file>");
            return;
        }

        File javaFile = new File(args[0]);
        if (!javaFile.exists() || !javaFile.isFile()) {
            System.err.println("File not found: " + args[0]);
            return;
        }

        CloseableLinter linter = new CloseableLinter();
        try {
            List<LintIssue> issues = linter.lintFile(javaFile);
            if (issues.isEmpty()) {
                System.out.println("No issues found!");
            } else {
                System.out.println("Found " + issues.size() + " issues:");
                for (LintIssue issue : issues) {
                    System.out.println(issue);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}