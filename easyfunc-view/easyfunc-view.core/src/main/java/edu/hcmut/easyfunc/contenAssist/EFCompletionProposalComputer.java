package edu.hcmut.easyfunc.contenAssist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.CompletionProposalCollector;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.ui.IEditorPart;

import Generator.CodeGeneration;
import Generator.SolvedFunction;
import Rank.Ranking;

public class EFCompletionProposalComputer extends
		JavaCompletionProposalComputer implements
		IJavaCompletionProposalComputer {
	
	private static final String SPLIT_VARIABLE_START = "<";
	private static final String SPLIT_VARIABLE_END = ">\n";
	private static final String VOID_TYPE = "void";
	
	private static final long JAVA_CODE_ASSIST_TIMEOUT = Long.getLong(
			"org.eclipse.jdt.ui.codeAssistTimeout", 5000).longValue() * 10; // ms //$NON-NLS-1$

	private String fErrorMessage;

	private final IProgressMonitor fTimeoutProgressMonitor;

	List<Proposal> proposals = new ArrayList<EFCompletionProposalComputer.Proposal>();

	class Proposal {
		public int index;
		public String comment;
		public String displayString;
		public String additionalProposalInfo;
		public String replacementString;
	}

	public EFCompletionProposalComputer() {

		// TODO Auto-generated constructor stub
		System.out
				.println("EFCompletionProposalComputer.EFCompletionProposalComputer()");
		fTimeoutProgressMonitor = createTimeoutProgressMonitor(JAVA_CODE_ASSIST_TIMEOUT);

	}

	@Override
	public void sessionStarted() {
		// TODO Auto-generated method stub
		System.out.println("EFCompletionProposalComputer.sessionStarted()");

	}

	@Override
	public List<ICompletionProposal> computeCompletionProposals(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		List<ICompletionProposal> results = new ArrayList<ICompletionProposal>();
		EFContentAssistInvocationContext efContext = new EFContentAssistInvocationContext(
				context);
		
		try {

			 CharSequence query = efContext.computeIdentifierPrefix();
			// System.out.println(query);
			String listVariable = getVisibleVariableAndMethodCall(efContext,query.length());
			
			CodeGeneration cg = new CodeGeneration(listVariable,
					query.toString());
			cg.loadFunctionAPI();
			cg.generateFuncCall();

			ArrayList<SolvedFunction> funcList = cg.getFunList();
			if (funcList.size() == 0) {
				return results;
			}

			Ranking r = new Ranking(funcList);
			r.sort();

			String[] sResults = r.toString().split("\n\n");

			for (String result : sResults) {
				Proposal calculateProposal = new Proposal();
				String[] parseResult = result.split("\n");

				// calculateProposal.index = parseResult[1].;
				calculateProposal.displayString = calculateProposal.comment = parseResult[2];
				calculateProposal.additionalProposalInfo = calculateProposal.replacementString = parseResult[2]
						+ "\n\t" + parseResult[1];

				proposals.add(calculateProposal);
			}
			
			for (Proposal proposal : proposals) {

				int replacementLength = efContext.getIgnoreSpace() ? query
						.length() + 2 : query.length();

				results.add(new org.eclipse.jface.text.contentassist.CompletionProposal(
						proposal.replacementString, efContext.getInvocationOffset()
								- replacementLength, replacementLength,
						proposal.replacementString.length(), null,
						proposal.displayString, null,
						proposal.additionalProposalInfo));
			}

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		proposals.clear();
		return results;
	}

	private String getVisibleVariableAndMethodCall(EFContentAssistInvocationContext efContext,int maxOffset) 
			throws JavaModelException {

		ICompilationUnit unit = efContext.getCompilationUnit();
		if (unit == null)
			return null;

		ITextViewer viewer = efContext.getViewer();
		IEditorPart editor = JavaPlugin.getActivePage().getActiveEditor();
		int offset = efContext.getInvocationOffset();

		JavaContentAssistInvocationContext jcaic = new JavaContentAssistInvocationContext(viewer, offset,editor);
		CompletionProposalCollector collector = createCollector(jcaic);
		
		// Allow completions for unresolved types - since 3.3
		collector.setAllowsRequiredProposals(CompletionProposal.FIELD_REF,CompletionProposal.TYPE_REF, true);
		collector.setAllowsRequiredProposals(CompletionProposal.FIELD_REF,CompletionProposal.TYPE_IMPORT, true);
		collector.setAllowsRequiredProposals(CompletionProposal.FIELD_REF,CompletionProposal.FIELD_IMPORT, true);

		collector.setAllowsRequiredProposals(CompletionProposal.METHOD_REF,CompletionProposal.TYPE_REF, true);
		collector.setAllowsRequiredProposals(CompletionProposal.METHOD_REF,CompletionProposal.TYPE_IMPORT, true);
		collector.setAllowsRequiredProposals(CompletionProposal.METHOD_REF,CompletionProposal.METHOD_IMPORT, true);

		collector.setAllowsRequiredProposals(CompletionProposal.CONSTRUCTOR_INVOCATION,CompletionProposal.TYPE_REF, true);

		collector.setAllowsRequiredProposals(CompletionProposal.ANONYMOUS_CLASS_CONSTRUCTOR_INVOCATION,CompletionProposal.TYPE_REF, true);
		collector.setAllowsRequiredProposals(CompletionProposal.ANONYMOUS_CLASS_DECLARATION,CompletionProposal.TYPE_REF, true);

		collector.setAllowsRequiredProposals(CompletionProposal.TYPE_REF,CompletionProposal.TYPE_REF, true);
		
		//Set ignore
		collector.setIgnored(CompletionProposal.FIELD_REF, false);

		collector.setIgnored(CompletionProposal.LOCAL_VARIABLE_REF, false);

		//collector.setIgnored(CompletionProposal.METHOD_NAME_REFERENCE, false);
		//collector.setIgnored(CompletionProposal.METHOD_REF, false);
		//collector.setIgnored(CompletionProposal.CONSTRUCTOR_INVOCATION, false);
		
		collector.setIgnored(CompletionProposal.VARIABLE_DECLARATION, false);

		try {
			unit.codeComplete(offset-maxOffset, collector, fTimeoutProgressMonitor);
		} catch (OperationCanceledException x) {
			
		} 

		ICompletionProposal[] javaProposals = collector.getJavaCompletionProposals();
		
		String result = new String();
		for (ICompletionProposal iCompletionProposal : javaProposals) {
			String filterVarible = filter(iCompletionProposal.getDisplayString());
			if (filterVarible != null) {
				result = result + filterVarible;
			}
		}
		
		return result;
	}
	/**
	 * A filter name have model is: name:type-classDeclaration and have 2 kind: variable and method call.<br>
	 * Example: <code>test1:String, test2():String, test3():void </code><br>
	 * We should filter the methods which have return type is void and user type definition because<br> 
	 * it is useless in parameter of method
	 * TODO: Not complete user type definition
	 * @return Return all variable and method comparable and have model "<"name:type">" 
	 */
	private String filter(String filterName) {
		String[] parse = filterName.split(":");
		
		//Filter name not is model
		if(parse.length >2)
			return null;
		String name = parse[0].trim();
		String[] typeAndDeclare = parse[1].split("-");
		String type = typeAndDeclare[0].trim();
		
		if(type.equals(VOID_TYPE))
			return null;
		else{
			return SPLIT_VARIABLE_START + name + ":" + type +SPLIT_VARIABLE_END;
		}
	}
	@Override
	public List<IContextInformation> computeContextInformation(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sessionEnded() {
		// TODO Auto-generated method stub
		proposals.clear();
	}

	private IProgressMonitor createTimeoutProgressMonitor(final long timeout) {
		return new IProgressMonitor() {

			private long fEndTime;

			public void beginTask(String name, int totalWork) {
				fEndTime = System.currentTimeMillis() + timeout * 100;
			}

			public boolean isCanceled() {
				return fEndTime <= System.currentTimeMillis();
			}

			public void done() {
			}

			public void internalWorked(double work) {
			}

			public void setCanceled(boolean value) {
			}

			public void setTaskName(String name) {
			}

			public void subTask(String name) {
			}

			public void worked(int work) {
			}
		};
	}

	
}
