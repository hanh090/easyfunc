package edu.hcmut.easyfunc.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.CompletionContext;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;

import Generator.CodeGeneration;

/**
 * 
 * @author LEHANH Visitor a node and query to GenerationCode
 */
public class EFEditor extends JavaEditor {
	
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";

	List<MethodInvocation> methodInvocations = new ArrayList<MethodInvocation>();
	CodeGeneration codeGeneration;

	public EFEditor() {
		// TODO Auto-generated constructor stub
		System.out.println("EFEditor.EFEditor()");
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects(IContainer.INCLUDE_HIDDEN);
		
		for (IProject project : projects) {
			try {
				if (project.isNatureEnabled(JDT_NATURE)) {
					analyseMethods(project);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void analyseMethods(IProject project) throws JavaModelException {
		System.out.println("EFEditor.analyseMethods()");
		// TODO Auto-generated method stub
		IPackageFragment[] packages = JavaCore.create(project)
				.getPackageFragments();

		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				createQuery(mypackage);
			}

		}
	}

	private void createQuery(IPackageFragment mypackage)
			throws JavaModelException {
		System.out.println("EFEditor.createQuery()");
		// TODO Auto-generated method stub
		
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			// Now create the AST for the ICompilationUnits
			//TODO:Test
			
			JavaContentAssistInvocationContext jcaic= new JavaContentAssistInvocationContext(unit);
			CompletionContext cc = jcaic.getCoreContext();
			//End Test
			CompilationUnit parse = parse(unit);
			
			//Scope
			EFVisitor visitor = new EFVisitor();
			parse.accept(visitor);
			
			
			List<VariableDeclarationStatement> variableDeclarationStatements = visitor.getVdss();
			System.out.println("VariableDeclarationStatement is\n ");
			for (VariableDeclarationStatement variableDeclarationStatement : variableDeclarationStatements) {
				System.out.println(variableDeclarationStatement);
			}
			
			List<VariableDeclarationExpression> variableDeclarationExpressions = visitor.getVdes();
			System.out.println("VariableDeclarationExpression is\n: ");
			for (VariableDeclarationExpression variableDeclarationExpression : variableDeclarationExpressions) {
				System.out.println(variableDeclarationExpression);
			}
			
			
		}
	}

	

	public CodeGeneration getCodeGeneration() {
		return codeGeneration;
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the
	 * Java source file
	 * 
	 * @param unit
	 * @return
	 */

	private static CompilationUnit parse(ICompilationUnit unit) {
		System.out.println("EFEditor.parse()");
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

	@Override
	protected IJavaElement getElementAt(int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IJavaElement getCorrespondingElement(IJavaElement element) {
		// TODO Auto-generated method stub
		return null;
	}

}
