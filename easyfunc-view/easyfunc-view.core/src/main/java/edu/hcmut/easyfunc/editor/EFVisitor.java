package edu.hcmut.easyfunc.editor;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;



public class EFVisitor extends ASTVisitor {
	private List<VariableDeclarationExpression> vdes = new ArrayList<VariableDeclarationExpression>();
	
	private List<VariableDeclarationStatement> vdss = new ArrayList<VariableDeclarationStatement>();
	
	private List<SingleVariableDeclaration> svds = new ArrayList<SingleVariableDeclaration>(); 
	private List<VariableDeclarationFragment> vdfs = new ArrayList<VariableDeclarationFragment>();
	
	private CompilationUnit compilationUnit;
	
	@Override
	public boolean visit(VariableDeclarationExpression node) {
		
		vdes.add(node);
		return super.visit(node);
	};
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		vdss.add(node);
		return super.visit(node);
	};
	
	@Override
	public boolean visit(SingleVariableDeclaration node) {
		svds.add(node);
		return super.visit(node);
	};
	
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		vdfs.add(node);
		return super.visit(node);
	};
	
	@Override
	public void preVisit(ASTNode node) {
		
		// TODO Auto-generated method stub
		System.out.println("node is \"" + node + "\" kind is\" " + node.getNodeType() + 
							" \" flag is \"" + node.getFlags() + "\""
							+"root is \" " + node.getRoot() +"\""
							);
		super.preVisit(node);
	}
	public List<SingleVariableDeclaration> getSvds() {
		return svds;
	}
	
	public List<VariableDeclarationExpression> getVdes() {
		return vdes;
	}
	
	public List<VariableDeclarationFragment> getVdfs() {
		return vdfs;
	}
	
	public List<VariableDeclarationStatement> getVdss() {
		return vdss;
	}
}
